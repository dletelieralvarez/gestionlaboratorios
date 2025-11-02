package com.example.gestionlaboratorios.Usuarios.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.gestionlaboratorios.Roles.model.Rol;
import com.example.gestionlaboratorios.Usuarios.controllers.UsuarioController;
import com.example.gestionlaboratorios.Usuarios.model.Usuario;
import com.example.gestionlaboratorios.Usuarios.services.UsuarioService;
import com.example.gestionlaboratorios.comun.model.ApiResult;
import com.example.gestionlaboratorios.Roles.repository.RolRepository;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.Link;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/usuarios")
// @CrossOrigin(origins = "*")

public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepository rolRepository;

    @GetMapping
    public ResponseEntity<ApiResult<List<Usuario>>> retornaTodosLosUsuarios() {        
        try
        {
            log.info("Get / retornaTodosLosUsuarios - Se obtiene lista con todos los usuarios");
            List<Usuario> usuarios = usuarioService.getAllUsuarios();
            //si lista de usuarios esta vacia mostrará el mensaje
            if(usuarios.isEmpty())
            {
                 return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResult<>("No se encontraron usuarios", null, HttpStatus.NOT_FOUND.value())); 
            }
            
            //si lista tiene datos retorna el mensaje encontrados mas la lista y el estado
            return ResponseEntity.ok(
                new ApiResult<>("Usuarios encontrados", usuarios, HttpStatus.OK.value())
            );
        }
        catch(Exception e){
            log.error("Error al obtener los usuarios: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>("Error al obtener los usuarios: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<Usuario>> retornaUsuarioPorId(@Valid @PathVariable Long id){
        try{

            log.info("Get / retornaUsuarioPorId - Se obtiene el usuario por ID: ", id);
            Optional<Usuario> usuarioOpt = usuarioService.getUsuarioPorId(id);

            if(usuarioOpt.isPresent())
            {
                Usuario usuario = usuarioOpt.get();
                log.info("Usuario encontrado con ID: ", id);
                //aqui genero link HATEOAS
                List<Link> links = List.of(
                    linkTo(methodOn(UsuarioController.class).retornaUsuarioPorId(id)).withSelfRel(),
                    linkTo(methodOn(UsuarioController.class).retornaTodosLosUsuarios()).withRel("all"),
                    linkTo(methodOn(UsuarioController.class).actualizarUsuario(id, usuario)).withRel("update"),
                    linkTo(methodOn(UsuarioController.class).eliminarUsuario(id)).withRel("delete")
                );
                //si encuentra el usuario por id retorna el mensaje encontrado mas el usuario y el estado y links
                return ResponseEntity.ok(
                    new ApiResult<>("Usuario encontrado", usuario, HttpStatus.OK.value(), links));
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResult<>("Usuario no encontrado con ID: " + id, null, HttpStatus.NOT_FOUND.value(), null));
            }

        }
        catch(Exception e){
            log.error("Error al obtener el usuario por ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>("Error al obtener el usuario: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResult<List<Usuario>>> guardarNuevoUsuario(@Valid @RequestBody Usuario nuevoUsuario) {
        try{
            log.info("Post / guardarNuevoUsuario - Se guarda un nuevo usuario: {}", nuevoUsuario.toString());
            Usuario usuario = usuarioService.insertarUsuario(nuevoUsuario);
            
            ApiResult<List<Usuario>> respuesta = new ApiResult<List<Usuario>>(
               "Usuario insertado con éxito", List.of(usuario), HttpStatus.CREATED.value()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }
        catch(Exception e){
            ApiResult<List<Usuario>> respuesta = new ApiResult<List<Usuario>>(
               "Error al insertar el usuario: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<List<Usuario>>> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        try{
            log.info("Put / actualizarUsuario - Se actualiza el usuario con ID: " + id);
            Optional<Usuario> buscaUsuario = usuarioService.getUsuarioPorId(id);

            if(!buscaUsuario.isPresent()){
                log.warn("No se encontró Usuario con el id : " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResult<>("Usuario no encontrado", null, HttpStatus.NOT_FOUND.value(), null));
            }

            Long idRol = usuario.getRol().getId();
            Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Rol no encontrado con ID: " + idRol
                ));

            //solo permitiré modificar el email del usuario, contraseña y rol por el id del usuario
            Usuario actualiza = buscaUsuario.get();
            actualiza.setEmail(usuario.getEmail());
            actualiza.setContrasena(usuario.getContrasena());
            actualiza.setRol(rol);
            //guarda la modificacion de email y contrasena
            log.info("Usuario actualizado con el id: " + id);
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(actualiza, id);

            //links HATEOAS
            List<Link> links = List.of(
                linkTo(methodOn(UsuarioController.class).actualizarUsuario(id, usuario)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).retornaTodosLosUsuarios()).withRel("all"), 
                linkTo(methodOn(UsuarioController.class).retornaUsuarioPorId(id)).withRel("get"), 
                linkTo(methodOn(UsuarioController.class).eliminarUsuario(id)).withRel("delete")
            );

            //retorna el mensaje con apiresult
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResult<>("Usuario actualizado con éxito", List.of(usuarioActualizado), HttpStatus.OK.value(), links));
        }
        catch(Exception e){
            log.error("Error al actualizar Usuario con el id" + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>("Error al actualizar el usuario " , null, HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> eliminarUsuario(@Valid @PathVariable Long id) {
        try{
            log.info("Delete / eliminarUsuario - se elimina el usuario con ID: " + id);
            //retorna los datos de USUARIO segun el id 
            Optional<Usuario> usuarioExistente = usuarioService.getUsuarioPorId(id);
            //si no lo encuentra entra en el if y retorna el mensaje
            if(!usuarioExistente.isPresent()){
                log.warn("No se encontró Usuario con el id : " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResult<>("Usuario no encontrado", null, HttpStatus.NOT_FOUND.value(), null));
            }            
            //si encuentra el usuario lo elimina y retorna el mensaje de exito 
            log.info("Usuario encontrado con id " + id);
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResult<>("Usuario eliminado con éxito", "Usuario con ID " + id + " eliminado.", HttpStatus.OK.value()));

        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>("Error al eliminar el usuario: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

}
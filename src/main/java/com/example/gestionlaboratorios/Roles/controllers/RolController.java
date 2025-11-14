package com.example.gestionlaboratorios.Roles.controllers;

import com.example.gestionlaboratorios.Roles.model.Rol;
import com.example.gestionlaboratorios.Roles.services.RolService;
import com.example.gestionlaboratorios.comun.model.ApiResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.Link;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:4200")
// @CrossOrigin(origins = "*")

public class RolController {
    
    @Autowired
    private RolService serviceRol;

    @GetMapping
    public ResponseEntity<ApiResult<List<Rol>>> retornaTodosLosRoles() {        
        try
        {
            log.info("Get / retornaTodosLosRoles - Se obtiene lista con todos los roles");
            List<Rol> roles = serviceRol.getAllRoles();
            //si lista de roles esta vacia mostrará el mensaje
            if(roles.isEmpty())
            {
                 return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResult<>("No se encontraron roles", null, HttpStatus.NOT_FOUND.value())); 
            }
            
            //si lista tiene datos retorna el mensaje encontrados mas la lista y el estado
            return ResponseEntity.ok(
                new ApiResult<>("Roles encontrados", roles, HttpStatus.OK.value())
            );
        }
        catch(Exception e){
            log.error("Error al obtener los roles: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>("Error al obtener los roles: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<Rol>> retornaRolPorId(@PathVariable Long id){
        try{
            
            log.info("Get / retornaRolPorId - Se obtiene el rol por ID: ", id);
            Optional<Rol> rolOpt = serviceRol.getRolById(id);
            
            if(rolOpt.isPresent())
            {
                Rol rol = rolOpt.get();
                log.info("Rol encontrado con ID: ", id);
                //aqui genero link HATEOAS
                List<Link> links = List.of(
                    linkTo(methodOn(RolController.class).retornaRolPorId(id)).withRel("Retorna Rol por ID"),
                    linkTo(methodOn(RolController.class).retornaTodosLosRoles()).withRel("Retorna Todos los Roles"), 
                    linkTo(methodOn(RolController.class).eliminarRol(id)).withRel("Eliminar Rol"),
                    linkTo(methodOn(RolController.class).actualizarRol(id, rol)).withRel("Actualizar Rol")
                );
                //si encuentra el rol por id retorna el mensaje encontrado mas el rol y el estado y links
                return ResponseEntity.ok(
                    new ApiResult<>("Rol encontrado", rol, HttpStatus.OK.value(), links));
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResult<>("Rol no encontrado con ID: " + id, null, HttpStatus.NOT_FOUND.value(), null));
            }

        }
        catch(Exception e){
            log.error("Error al obtener el rol por ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>("Error al obtener el rol: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }


    @PostMapping
    public ResponseEntity<ApiResult<List<Rol>>> guardarNuevoRol(@Valid @RequestBody Rol nuevoRol) {
        try{
            log.info("Post / guardarNuevoRol - Se guarda un nuevo rol" + nuevoRol.toString());
            Rol rol = serviceRol.insertarRol(nuevoRol);
            
            ApiResult<List<Rol>> respuesta = new ApiResult<List<Rol>>(
               "Rol insertado con éxito", List.of(rol), HttpStatus.CREATED.value()
            ); 

            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }
        catch(Exception e){
            ApiResult<List<Rol>> respuesta = new ApiResult<List<Rol>>(
               "Error al insertar el rol: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<List<Rol>>> actualizarRol(@PathVariable Long id, @Valid @RequestBody Rol rol) {
        try{
            log.info("Put / actualizarRol - Se actualiza el rol con ID: " + id);
            Optional<Rol> buscaRol = serviceRol.getRolById(id); 

            if(!buscaRol.isPresent()){
                log.warn("No se encontró Rol con el id : " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResult<>("Rol no encontrado", null, HttpStatus.NOT_FOUND.value(), null));
            }

            Rol actualiza = serviceRol.getRolById(id).get();
            actualiza.setNombreRol(rol.getNombreRol());
            //guarda la modificacion de nombre 
            log.info("Rol actualizado con el id: " + id); 
            Rol rolActualizado = serviceRol.actualizarRol(actualiza, id); 

            //links HATEOAS
            List<Link> links = List.of(
                linkTo(methodOn(RolController.class).actualizarRol(id, rol)).withRel("Actualizar Rol"),
                linkTo(methodOn(RolController.class).retornaTodosLosRoles()).withRel("Retorna Todos los Roles"),
                linkTo(methodOn(RolController.class).eliminarRol(id)).withRel("Eliminar Rol"),
                linkTo(methodOn(RolController.class).retornaRolPorId(id)).withRel("Retorna Rol por ID")
            );

            //retorna el mensaje con apiresult
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResult<>("Rol actualizado con éxito", List.of(rolActualizado), HttpStatus.OK.value(), links));        
        }
        catch(Exception e){
            log.error("Error al actualizar Rol con el id" + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>("Error al actualizar el rol " , null, HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> eliminarRol(@PathVariable Long id) {
        try{
            log.info("Delete / eliminaRol - se elimina el rol con ID: " + id);
            //retorna los datos de ROL segun el id 
            Optional<Rol> rolExistente = serviceRol.getRolById(id);
            //si no lo encuentra entra en el if y retorna el mensaje
            if(!rolExistente.isPresent()){
                log.warn("No se encontró Rol con el id : " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResult<>("Rol no encontrado", null, HttpStatus.NOT_FOUND.value(), null));
            }            
            
            //si encuentra el rol lo elimina y retorna el mensaje de exito 
            log.info("Rol encontrado con id " + id);
            serviceRol.eliminarRol(id);
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResult<>("Rol eliminado con éxito", "Rol con ID " + id + " eliminado.", HttpStatus.OK.value())); 
        
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>("Error al eliminar el rol: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }
}

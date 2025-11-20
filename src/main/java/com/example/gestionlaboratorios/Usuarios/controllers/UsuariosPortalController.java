package com.example.gestionlaboratorios.Usuarios.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.gestionlaboratorios.Usuarios.controllers.UsuariosPortalController;
import com.example.gestionlaboratorios.Usuarios.model.UsuariosPortal;
import com.example.gestionlaboratorios.Usuarios.services.UsuariosPortalService;
import com.example.gestionlaboratorios.comun.model.ApiResult;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/portal/usuarios")
public class UsuariosPortalController {

    @Autowired
    private UsuariosPortalService usuariosService;


    @PostMapping("/registro")
    public ResponseEntity<ApiResult<UsuariosPortal>> registrar(@RequestBody UsuariosPortal user) {
        
        try{
            log.info("POST /registro - Nuevo usuario: {}", user.getEmail());

            UsuariosPortal nuevo = new UsuariosPortal(); 
            nuevo.setRut(user.getRut());
            nuevo.setNombres(user.getNombres());
            nuevo.setApellidos(user.getApellidos());
            nuevo.setEmail(user.getEmail());
            /*service se encarga de encriptar la pass */
            nuevo.setPasswordHash(user.getPasswordHash());

            UsuariosPortal usuarioGuardado = usuariosService.registrarCliente(nuevo); 
            usuarioGuardado.setPasswordHash(null);

            return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResult<>("Usuario registrado correctamente", usuarioGuardado, HttpStatus.CREATED.value()));
            
        } catch(IllegalArgumentException e){
            log.warn("Error en la validación al registrar usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResult<>(e.getMessage(), null, HttpStatus.BAD_REQUEST.value()));
        } catch (Exception e) {
            log.error("Error inesperado al registrar usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResult<>("Error interno del servidor", null,
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResult<UsuariosPortal>> login(@RequestBody UsuariosPortal user) {
        try{
            UsuariosPortal usuario = usuariosService.login(user.getRut(), user.getPasswordHash()); 
            usuario.setPasswordHash(null);

            return ResponseEntity.ok(new ApiResult<>("Login Correcto", usuario, HttpStatus.OK.value()));             
        
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ApiResult<>(e.getMessage(), null, HttpStatus.UNAUTHORIZED.value())); 
        }catch(Exception e){
            log.error("Error al hacer login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResult<>("Error interno al hacer login", null, HttpStatus.INTERNAL_SERVER_ERROR.value())); 
        }
    }

    @GetMapping
    public ResponseEntity<ApiResult<List<UsuariosPortal>>> listarUsuarios(){
        List<UsuariosPortal> usuarios = usuariosService.listarTodos(); 
        usuarios.forEach(u-> u.setPasswordHash(null));
        return ResponseEntity.ok(
            new ApiResult<>("Usuarios encontrados", usuarios, HttpStatus.OK.value())); 
    }
    
    @GetMapping("/id")
    public ResponseEntity<ApiResult<UsuariosPortal>> obtenerPorId(@PathVariable Long id) {
        return usuariosService.buscarPorId(id)
        .map(u-> {
            u.setPasswordHash(null);
            return ResponseEntity.ok(
                new ApiResult<>("Usuario encontrado", u, HttpStatus.OK.value()));             
        })
        .orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ApiResult<>("Usuario no encontrado", null, HttpStatus.NOT_FOUND.value())));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<UsuariosPortal>> actualizarUsuario(@PathVariable Long id,
            @RequestBody UsuariosPortal user) {
        
        try{
            UsuariosPortal datos = new UsuariosPortal(); 
            datos.setNombres(user.getNombres());
            datos.setApellidos(user.getApellidos());
            datos.setEmail(user.getEmail());
            datos.setPasswordHash(user.getPasswordHash());

            UsuariosPortal actualizado= usuariosService.actualizarDatos(id, datos); 
            actualizado.setPasswordHash(null);

            return ResponseEntity.ok(
                new ApiResult<>("Usuario actualizado", actualizado, HttpStatus.OK.value())
            );
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResult<>(e.getMessage(), null, HttpStatus.NOT_FOUND.value())); 
        }catch(Exception e){
            log.error("Error al actualizar usuario", e); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResult<>("Error interno al actualizar usuario", null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> eliminarUsuario(@PathVariable Long id){
        try {
            usuariosService.eliminarUsuario(id);
            return ResponseEntity.ok(
                    new ApiResult<>("Usuario eliminado correctamente",
                            "Usuario con ID " + id + " eliminado",
                            HttpStatus.OK.value()));
        } catch (Exception e) {
            log.error("Error al eliminar usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResult<>("Error interno al eliminar usuario", null,
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}

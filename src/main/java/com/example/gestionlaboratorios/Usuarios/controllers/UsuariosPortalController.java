package com.example.gestionlaboratorios.Usuarios.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.gestionlaboratorios.Usuarios.controllers.UsuariosPortalController;
import com.example.gestionlaboratorios.Usuarios.dto.LoginUsuarioPortalDTO;
import com.example.gestionlaboratorios.Usuarios.dto.PerfilUsuarioPortalDTO;
import com.example.gestionlaboratorios.Usuarios.dto.RecuperarPasswordDTO;
import com.example.gestionlaboratorios.Usuarios.dto.RegistroUsuarioPortalDTO;
import com.example.gestionlaboratorios.Usuarios.dto.UsuarioPortalDTO;
import com.example.gestionlaboratorios.Usuarios.model.UsuariosPortal;
import com.example.gestionlaboratorios.Usuarios.services.UsuariosPortalMapper;
import com.example.gestionlaboratorios.Usuarios.services.UsuariosPortalService;
import com.example.gestionlaboratorios.comun.model.ApiResult;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/portal/usuarios")
public class UsuariosPortalController {

    @Autowired
    private UsuariosPortalService usuariosService;

    @PostMapping("/registro")
    public ResponseEntity<ApiResult<UsuarioPortalDTO>> registrar(@Valid @RequestBody RegistroUsuarioPortalDTO user) {
        
        //try{
            log.info("POST /registro - Nuevo usuario: {}", user.getEmail());

            UsuariosPortal nuevo = new UsuariosPortal(); 
            nuevo.setRut(user.getRut());
            nuevo.setNombres(user.getNombres());
            nuevo.setApellidos(user.getApellidos());
            nuevo.setEmail(user.getEmail());
            /*service se encarga de encriptar la pass */
            nuevo.setPasswordHash(user.getPassword());

            UsuariosPortal usuarioGuardado = usuariosService.registrarCliente(nuevo); 
            UsuarioPortalDTO dto =  UsuariosPortalMapper.toDto(usuarioGuardado);

            ApiResult<UsuarioPortalDTO> respuesta = new ApiResult<>(
                "Usuario registrado correctamente",
                dto,
                HttpStatus.CREATED.value()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResult<UsuarioPortalDTO>> login(
        @Valid @RequestBody LoginUsuarioPortalDTO login) {
        try {
            log.info("POST /login - Login usuario portal rut={}", login.getRut());
            
            UsuarioPortalDTO  dto = usuariosService.login(
                    login.getRut(),
                    login.getPassword()
            );

            // mapea a DTO
           // UsuarioPortalDTO dto = UsuariosPortalMapper.toDto(usuario);

            ApiResult<UsuarioPortalDTO> respuesta = new ApiResult<>(
                    "Login correcto",
                    dto,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.ok(respuesta);

        } catch (IllegalArgumentException e) {
            log.warn("Login inválido para rut {} : {}", login.getRut(), e.getMessage());

            ApiResult<UsuarioPortalDTO> respuesta = new ApiResult<>(
                    "Rut o contraseña incorrecta",
                    null,
                    HttpStatus.UNAUTHORIZED.value()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);

        } catch (Exception e) {
            log.error("Error interno al procesar login", e);

            ApiResult<UsuarioPortalDTO> respuesta = new ApiResult<>(
                    "Error al procesar login: " + e.getMessage(),
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<ApiResult<Map<String, String>>> recuperarPassword(@RequestBody RecuperarPasswordDTO body) {

        try {
            log.info("POST /recuperar-password - {}", body.getRutOrEmail());
            String tempPassword = usuariosService.iniciarRecuperacionPassword(body.getRutOrEmail());

            Map<String, String> data = Map.of("tempPassword", tempPassword);


            return ResponseEntity.ok(
                new ApiResult<>("Contraseña temporal generada correctamente, su nueva contraseña es : " + tempPassword, data, HttpStatus.OK.value())  
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResult<>(e.getMessage(), null, HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>("Error al recuperar contraseña", null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
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

    @GetMapping("/perfil/{id}")
    public ResponseEntity<ApiResult<PerfilUsuarioPortalDTO>> getPerfil(@PathVariable Long id) {
        PerfilUsuarioPortalDTO dto = usuariosService.obtenerPerfil(id);
        ApiResult<PerfilUsuarioPortalDTO> result = new ApiResult<>(
            "Perfil obtenido correctamente",
            dto,
            HttpStatus.OK.value()
        );

        return ResponseEntity.ok(result);
    }

    @PutMapping("/perfil/{id}")
    public ResponseEntity<ApiResult<PerfilUsuarioPortalDTO>> updatePerfil(
            @PathVariable Long id,
            @RequestBody PerfilUsuarioPortalDTO dto) {

        PerfilUsuarioPortalDTO actualizado = usuariosService.actualizarPerfil(id, dto);
        ApiResult<PerfilUsuarioPortalDTO> result = new ApiResult<>(
            "Perfil actualizado correctamente",
            actualizado,
            HttpStatus.OK.value()
        );

        return ResponseEntity.ok(result);
    }
}

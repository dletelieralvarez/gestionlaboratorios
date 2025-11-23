package com.example.gestionlaboratorios.Usuarios.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestionlaboratorios.Usuarios.dto.LoginUsuarioPortalDTO;
import com.example.gestionlaboratorios.Usuarios.dto.UsuarioPortalDTO;
import com.example.gestionlaboratorios.Usuarios.services.UsuariosPortalService;
import com.example.gestionlaboratorios.comun.model.ApiResult;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/portal")
public class LoginController {
    
    @Autowired
    private UsuariosPortalService usuariosService;

    @PostMapping("/login")
    public ResponseEntity<ApiResult<UsuarioPortalDTO>> login(@Valid @RequestBody LoginUsuarioPortalDTO login){

        try{
            log.info("POST / LOGIN - Login usuario portal rut={}", login.getRut()); 

            UsuarioPortalDTO dto  = usuariosService.login(
                login.getRut(), 
                login.getPassword()
            );

            ApiResult<UsuarioPortalDTO> respuesta = new ApiResult<>(
                "Login exitoso",
                dto, 
                HttpStatus.OK.value()
            ); 

            return ResponseEntity.ok(respuesta); 
        }catch(IllegalArgumentException e){
            log.warn("Login inválido para rut {}: {}", login.getRut(), e.getMessage()); 

            ApiResult<UsuarioPortalDTO> respuesta = new ApiResult<>(
                "Rut o contraseña incorrecta", 
                null, 
                HttpStatus.UNAUTHORIZED.value()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta); 
        }catch(Exception e){
            log.error("Error interno en login", e); 

            ApiResult<UsuarioPortalDTO> respuesta = new ApiResult<>(
                "Error al procesar login: " + e.getMessage(), 
                null, 
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta); 
        }
    }
}

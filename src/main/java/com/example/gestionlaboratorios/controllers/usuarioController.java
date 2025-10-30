package com.example.gestionlaboratorios.controllers;

import com.example.gestionlaboratorios.model.usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestionlaboratorios.services.usuarioService;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")

public class usuarioController {
    @Autowired
    private usuarioService usuario_service;

    @GetMapping
    public List<usuario> listaTodosLosUsuarios() {
        return usuario_service.getUsuarios();
    }

    @GetMapping("/{id}")
    public usuario obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<usuario> usuario = usuario_service.getUsuarioPorId(id);
        return usuario.orElse(null);
    }

    @PostMapping
    public ResponseEntity<String> guardarNuevoUsuario(@RequestBody usuario nuevoUsuario) {
        try{
            usuario_service.insertarUsuario(nuevoUsuario);
            return ResponseEntity.ok("Usuario guardado exitosamente.");
        }
        catch(Exception e){
            return ResponseEntity.status(500).body("Error al guardar el usuario: " + e.getMessage());
        }        
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@RequestBody usuario usuario, @PathVariable Long id) {
        try{
            usuario_service.actualizarUsuario(usuario, id);
            return ResponseEntity.ok("Usuario actualizado exitosamente. ID : " + id);
        }
        catch(Exception e){
            return ResponseEntity.status(500).body("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        try{
            usuario_service.eliminarUsuario(id);
            return ResponseEntity.ok("Usuario eliminado exitosamente.");
        }
        catch(Exception e){
            return ResponseEntity.status(500).body("Error al eliminar el usuario: " + e.getMessage());
        }
    }

}
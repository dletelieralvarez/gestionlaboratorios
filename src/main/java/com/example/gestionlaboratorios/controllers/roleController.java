package com.example.gestionlaboratorios.controllers;

import com.example.gestionlaboratorios.model.role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestionlaboratorios.services.roleService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "*")

public class roleController {
    
    @Autowired
    private roleService role_Service;

    @GetMapping
    public List<role> listaTodosLosRoles() {
        return role_Service.getRoles();
    }

    @PostMapping
    public ResponseEntity<String> guardarNuevoRol(@RequestBody role nuevoRol) {
        try{
            role_Service.insertarRole(nuevoRol);
            return ResponseEntity.ok("Rol guardado exitosamente.");
        }
        catch(Exception e){
            return ResponseEntity.status(500).body("Error al guardar el rol: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarRol(@RequestBody role role, @PathVariable Long id) {
        try{
            role_Service.actualizarRole(role, id);
            return ResponseEntity.ok("Rol actualizado exitosamente.");
        }
        catch(Exception e){
            return ResponseEntity.status(500).body("Error al actualizar el rol: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRol(@PathVariable Long id) {
        try{
            role_Service.eliminarRole(id);
            return ResponseEntity.ok("Rol eliminado exitosamente.");
        }
        catch(Exception e){
            return ResponseEntity.status(500).body("Error al eliminar el rol: " + e.getMessage());
        }
    }
    
}

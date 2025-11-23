package com.example.gestionlaboratorios.Usuarios.repository;

import java.util.Optional;

import com.example.gestionlaboratorios.Usuarios.model.UsuariosPortal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosPortalRepository extends JpaRepository<UsuariosPortal, Long> {
    Optional<UsuariosPortal> findByRut(String rut); 
    boolean existsByRut(String rut); 
    boolean existsByEmail(String email); 
    Optional<UsuariosPortal> findByRutOrEmail(String rut, String email); 
}

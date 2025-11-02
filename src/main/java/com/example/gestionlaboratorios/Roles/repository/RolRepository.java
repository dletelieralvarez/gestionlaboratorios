package com.example.gestionlaboratorios.Roles.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestionlaboratorios.Roles.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
    boolean existsByNombreRolIgnoreCase(String nombreRol);
}

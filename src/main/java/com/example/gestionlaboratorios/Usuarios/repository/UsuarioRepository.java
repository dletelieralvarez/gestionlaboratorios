package com.example.gestionlaboratorios.Usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestionlaboratorios.Usuarios.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
    
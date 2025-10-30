package com.example.gestionlaboratorios.services;
import com.example.gestionlaboratorios.model.role;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public interface roleService {
    List<role> getRoles(); 
    Optional<role> getRolPorId(Long id);
    role insertarRole(role role);
    role actualizarRole(role role, long id);
    void eliminarRole(long id);
}

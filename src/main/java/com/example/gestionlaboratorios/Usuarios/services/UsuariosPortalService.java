package com.example.gestionlaboratorios.Usuarios.services;

import java.util.List;
import java.util.Optional;

import com.example.gestionlaboratorios.Usuarios.model.UsuariosPortal;

public interface UsuariosPortalService {

    UsuariosPortal registrarCliente(UsuariosPortal nuevo); 
    Optional<UsuariosPortal> buscarPorRut(String rut); 
    Optional<UsuariosPortal> buscarPorId(Long id);     
    List<UsuariosPortal> listarTodos(); 
    UsuariosPortal actualizarDatos(Long id, UsuariosPortal datos); 
    void eliminarUsuario(Long id); 
    UsuariosPortal login(String rut, String passwordPlano); 
}

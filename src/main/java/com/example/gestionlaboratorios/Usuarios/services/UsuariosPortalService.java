package com.example.gestionlaboratorios.Usuarios.services;

import java.util.List;
import java.util.Optional;

import com.example.gestionlaboratorios.Usuarios.dto.PerfilUsuarioPortalDTO;
import com.example.gestionlaboratorios.Usuarios.dto.UsuarioPortalDTO;
import com.example.gestionlaboratorios.Usuarios.model.UsuariosPortal;

public interface UsuariosPortalService {

    UsuariosPortal registrarCliente(UsuariosPortal nuevo); 
    Optional<UsuariosPortal> buscarPorRut(String rut); 
    Optional<UsuariosPortal> buscarPorId(Long id);     
    List<UsuariosPortal> listarTodos(); 
    UsuariosPortal actualizarDatos(Long id, UsuariosPortal datos); 
    void eliminarUsuario(Long id); 
    //UsuariosPortal login(String rut, String passwordPlano); 
    UsuarioPortalDTO login(String rut, String passwordPlano);
    String iniciarRecuperacionPassword(String rutOrEmail);
    PerfilUsuarioPortalDTO obtenerPerfil(Long id);
    PerfilUsuarioPortalDTO actualizarPerfil(Long id, PerfilUsuarioPortalDTO datos);    
}

package com.example.gestionlaboratorios.Usuarios.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.example.gestionlaboratorios.Usuarios.model.Usuario;

@Service
public interface UsuarioService {
    List<Usuario> getAllUsuarios();
    Optional<Usuario> getUsuarioPorId(Long id);
    Usuario insertarUsuario(Usuario usuario);
    Usuario actualizarUsuario(Usuario usuario, Long id);
    Usuario eliminarUsuario(Long id);
}

package com.example.gestionlaboratorios.services;

import com.example.gestionlaboratorios.model.usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface usuarioService {
    List<usuario> getUsuarios();
    Optional<usuario> getUsuarioPorId(Long id);
    usuario insertarUsuario(usuario usuario);
    usuario actualizarUsuario(usuario usuario, long id);
    void eliminarUsuario(long id);
}

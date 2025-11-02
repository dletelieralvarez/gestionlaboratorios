package com.example.gestionlaboratorios.Usuarios.services;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.gestionlaboratorios.Usuarios.model.Usuario;
import com.example.gestionlaboratorios.Usuarios.repository.UsuarioRepository;

@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> getAllUsuarios() {
        log.debug("Servicio : getAllUsuarios");
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> getUsuarioPorId(Long id) {
        log.debug("Servicio : getUsuarioPorId con id {}", id);
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario insertarUsuario(Usuario nuevoUsuario) {
        log.debug("Servicio : insertarUsuario()");

        if (nuevoUsuario.getId() != null && usuarioRepository.existsById(nuevoUsuario.getId())) {
            throw new IllegalArgumentException("Ya existe un usuario con id: " + nuevoUsuario.getId());
        }
        return usuarioRepository.save(nuevoUsuario);
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuarioActualizado, Long id) {
        log.debug("Actualizando usuario con id: {}", id);
        return usuarioRepository.save(usuarioActualizado);
    }

    @Override
    public Usuario eliminarUsuario(Long id) {
        log.debug("Eliminando usuario con id: {}", id);
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if(usuario.isPresent()) {
            usuarioRepository.deleteById(id);
            return usuario.get();
        } else {
            return null;
        }
    }
    
}

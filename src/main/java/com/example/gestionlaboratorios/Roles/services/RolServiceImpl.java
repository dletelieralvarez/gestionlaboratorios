package com.example.gestionlaboratorios.Roles.services;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.gestionlaboratorios.Roles.model.Rol;
import com.example.gestionlaboratorios.Roles.repository.RolRepository;

@Slf4j
@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<Rol> getAllRoles() {
        log.debug("Servicio : getAllRoles");
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> getRolById(Long id) {
        log.debug("Servicio : getRolById con id {}", id);
        return rolRepository.findById(id);
    }

    @Override
    public Rol insertarRol(Rol nuevoRol) {
        log.debug("Servicio : insertarRol()");

        if (nuevoRol.getId() != null && rolRepository.existsById(nuevoRol.getId())) {
            throw new IllegalArgumentException("Rol ya existe con id: " + nuevoRol.getId());
        }
        return rolRepository.save(nuevoRol);
    }

    @Override
    public Rol actualizarRol(Rol rolActualizado, Long id) {
        log.debug("Updating role with id: {}", id);
        return rolRepository.save(rolActualizado);
    }

    @Override
    public Rol eliminarRol(Long id) {
        log.debug("Eliminando rol con id: {}", id);
        Optional<Rol> rol = rolRepository.findById(id);
        if(rol.isPresent()) {
            rolRepository.deleteById(id);
            return rol.get();
        } else {
            return null;
        }
    }
    
}

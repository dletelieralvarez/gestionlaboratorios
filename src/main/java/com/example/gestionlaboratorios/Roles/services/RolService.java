package com.example.gestionlaboratorios.Roles.services;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.gestionlaboratorios.Roles.model.Rol;

@Service
public interface RolService {

    List<Rol> getAllRoles(); 
    Optional<Rol> getRolById(Long id);
    Rol insertarRol(Rol nuevoRol);
    Rol actualizarRol(Rol rolActualizado, Long id);
    Rol eliminarRol(Long id);    
}

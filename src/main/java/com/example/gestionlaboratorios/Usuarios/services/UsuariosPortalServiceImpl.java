package com.example.gestionlaboratorios.Usuarios.services;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.gestionlaboratorios.Roles.model.Rol;
import com.example.gestionlaboratorios.Roles.repository.RolRepository;
import com.example.gestionlaboratorios.Usuarios.dto.UsuarioPortalDTO;
import com.example.gestionlaboratorios.Usuarios.model.UsuariosPortal;
import com.example.gestionlaboratorios.Usuarios.repository.UsuariosPortalRepository;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsuariosPortalServiceImpl implements UsuariosPortalService {
    
    private final UsuariosPortalRepository usuarioRepo; 
    private final RolRepository rolRepo; 
    private final PasswordEncoder passwordEncoder; 

    private static final String ROL_CLIENTE = "CLIENTE";

    @Override
    public UsuariosPortal registrarCliente(UsuariosPortal nuevo){
        log.debug("Servicio : registrarCliente");
        if(usuarioRepo.existsByRut(nuevo.getRut())){
            throw new IllegalArgumentException("El RUT ya está registrado");
        }
        if(usuarioRepo.existsByEmail(nuevo.getEmail())){
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Rol rolCliente = rolRepo.findByNombreRol(ROL_CLIENTE)
        .orElseThrow(()-> new IllegalStateException("Rol CLIENTE no configurado en la base de datos")); 

        nuevo.setRol(rolCliente);
        nuevo.setPasswordHash(passwordEncoder.encode(nuevo.getPasswordHash()));
        nuevo.setCreado(OffsetDateTime.now());
        nuevo.setActualizado(null);

        return usuarioRepo.save(nuevo);
    }

    @Override
    @Transactional//(readOnly  = true) 
    public Optional<UsuariosPortal> buscarPorId(Long id){
        return usuarioRepo.findById(id); 
    }

    @Override
    @Transactional
    public Optional<UsuariosPortal> buscarPorRut(String rut)
    {
        return usuarioRepo.findByRut(rut); 
    }

    @Override
    @Transactional
    public List<UsuariosPortal> listarTodos(){
        return usuarioRepo.findAll(); 
    }

    @Override
    public UsuariosPortal actualizarDatos(Long id, UsuariosPortal datos){
        UsuariosPortal existente = usuarioRepo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        existente.setNombres(datos.getNombres());
        existente.setApellidos(datos.getApellidos());
        existente.setEmail(datos.getEmail());
        existente.setActualizado(OffsetDateTime.now());
        return usuarioRepo.save(existente); 
    }

    @Override
    public void eliminarUsuario(Long id){
        usuarioRepo.deleteById(id);
    }

    @Override
    @Transactional
    public UsuarioPortalDTO  login(String rut, String passwordPlano) {
        UsuariosPortal usuario = usuarioRepo.findByRut(rut)
           .orElseThrow(() -> new IllegalArgumentException("Rut o contraseña inválidos"));

        if (!passwordEncoder.matches(passwordPlano, usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Rut o contraseña inválidos");
        }

        return UsuariosPortalMapper.toDto(usuario);
    }

}

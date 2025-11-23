package com.example.gestionlaboratorios.Usuarios.services;

import com.example.gestionlaboratorios.Usuarios.dto.UsuarioPortalDTO;
import com.example.gestionlaboratorios.Usuarios.model.UsuariosPortal;

public class UsuariosPortalMapper {
    public static UsuarioPortalDTO toDto(UsuariosPortal entity) {
        UsuarioPortalDTO dto = new UsuarioPortalDTO(); 
        dto.setId(entity.getId());
        dto.setRut(entity.getRut());
        dto.setNombres(entity.getNombres());
        dto.setApellidos(entity.getApellidos());
        dto.setEmail(entity.getEmail());

        if (entity == null) return null;

        Long rolId = null;
        String nombreRol = null;

        if (entity.getRol() != null) {
            // aquí se inicializa el proxy de rol mientras la sesión está abierta
            rolId = entity.getRol().getId();       
            nombreRol = entity.getRol().getNombreRol();
        }

        return UsuarioPortalDTO.builder()
                .id(entity.getId())
                .rut(entity.getRut())
                .nombres(entity.getNombres())
                .apellidos(entity.getApellidos())
                .email(entity.getEmail())
                .rolId(rolId)    
                .nombreRol(nombreRol)
                .creado(entity.getCreado())
                .actualizado(entity.getActualizado())
                .build();
    }
}

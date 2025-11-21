package com.example.gestionlaboratorios.Usuarios.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioPortalDTO {
    private Long id;
    private String rut;
    private String nombres;
    private String apellidos;
    private String email;
    private String nombreRol; //viene de entidad Rol
    private OffsetDateTime creado;
    private OffsetDateTime actualizado;
}

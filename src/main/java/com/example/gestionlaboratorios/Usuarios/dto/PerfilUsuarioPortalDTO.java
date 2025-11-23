package com.example.gestionlaboratorios.Usuarios.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilUsuarioPortalDTO {
    private Long id;         
    private String nombres;
    private String apellidos;
    private String email;
    //private String telefono; 
}

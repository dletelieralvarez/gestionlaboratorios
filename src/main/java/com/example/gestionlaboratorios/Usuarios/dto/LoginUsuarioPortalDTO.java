package com.example.gestionlaboratorios.Usuarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUsuarioPortalDTO {
    @NotBlank
    @Size(max = 10)
    private String rut;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;
}

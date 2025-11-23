package com.example.gestionlaboratorios.Usuarios.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecuperarPasswordDTO{
    @NotBlank(message = "Debes ingresar RUT sin puntos y sin digito verificador o email")
    private String rutOrEmail;

    private String tempPassword; 
}
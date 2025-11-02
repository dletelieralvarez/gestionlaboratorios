package com.example.gestionlaboratorios.Roles.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROLES_USUARIO"/*, schema = "DESARROLLADOR"*/)
public class Rol {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /*DEJARÉ UN MAXIMO DE CARACTERES DE 50 PARA NOMBRE DE ROL, y EN LA BD AGUANTA HASTA 255 EN CASO DE CAMBIAR ESPECIFICACIONES*/
    @NotBlank(message = "El nombre del rol no puede estar vacio")
    @Size(max = 50, message = "El nombre del rol no puede tener mas de 50 caracteres")
    @Column(name = "nombre_rol", nullable = false, length = 50)
    private String nombreRol;

}

/*
 * posibles roles 
 * 1 - ADMIN
 * 2 - CLIENTE
 * 3 - TECNICO
 * 4 - INVITADO
 */
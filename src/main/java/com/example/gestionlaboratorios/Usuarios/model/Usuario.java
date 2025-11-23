package com.example.gestionlaboratorios.Usuarios.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

import com.example.gestionlaboratorios.Roles.model.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USUARIOS", 
        uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuarios_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_usuarios_rut", columnNames = "rut")
    })
public class Usuario {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @NotNull(message = "El Rut no puede ser nulo, es obligatorio")
    @Positive(message = "El Rut debe ser un número positivo")
    @Column(name="rut", unique = true, nullable = false, length = 10)
    private Long rut;

    @NotBlank(message = "El Dígito Verificador no puede estar vacío")
    @Pattern(regexp = "^[0-9Kk]$", message = "El Dígito Verificador debe ser un número del 0 al 9 o la letra K")
    @Column(name="dv", nullable = false, length = 1)
    private String dv;

    @NotBlank(message = "El Nombre no puede estar vacío")
    @Length(max = 200, message = "El Nombre no puede exceder los 200 caracteres")
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @NotBlank(message = "El Email no puede estar vacío")
    @Length(max = 250, message = "El Email no puede exceder los 250 caracteres")
    @Email(message = "El Email debe tener un formato válido")
    @Column(name = "email", unique = true, nullable = false, length = 250)
    private String email;

    // @NotBlank(message = "La Contraseña no puede estar vacía")
    // @Size(min=8, max=15, message = "La Contraseña debe tener entre 8 y 15 caracteres")
    // @Column(name = "contrasena", nullable = false, length = 15)
    // private String contrasena;
    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @ManyToOne
    @NotNull(message = "El Rol es obligatorio")
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
}

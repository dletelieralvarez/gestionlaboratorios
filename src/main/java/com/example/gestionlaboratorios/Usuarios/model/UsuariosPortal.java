package com.example.gestionlaboratorios.Usuarios.model;
import java.time.OffsetDateTime;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.gestionlaboratorios.Roles.model.Rol;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(
    name = "USUARIOS_PORTAL",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_USUARIOS_PORTAL_RUT",   
            columnNames = "RUT"                
        )
    }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter 
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UsuariosPortal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*sin puntos, sin guion y sin dv */
    @NotNull(message = "El Rut no puede ser nulo, es obligatorio")
    @Positive(message = "El Rut debe ser un número positivo")
    @Column(nullable = false, length = 10, unique = true)
    private String rut;

    @NotBlank(message = "Nombres no puede estar vacío")
    @Length(max = 200, message = "Nombres no puede exceder los 200 caracteres")
    @Column(nullable = false, length = 100)
    private String nombres;

    @NotBlank(message = "Apellidos no puede estar vacío")
    @Length(max = 100, message = "Apellidos no puede exceder los 200 caracteres")
    @Column(nullable = false, length = 100)
    private String apellidos;

    @NotBlank(message = "El Email no puede estar vacío")
    @Length(max = 150, message = "El Email no puede exceder los 150 caracteres")
    @Email(message = "El Email debe tener un formato válido")
    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ROL", nullable = false)
    private Rol rol;

    @Column(nullable = false)
    private OffsetDateTime creado = OffsetDateTime.now();

    @Column
    private OffsetDateTime actualizado;

    @PreUpdate
    public void onUpdate() {
        this.actualizado = OffsetDateTime.now();
    }
}

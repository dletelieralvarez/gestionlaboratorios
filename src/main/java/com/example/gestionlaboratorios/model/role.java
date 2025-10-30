package com.example.gestionlaboratorios.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "ROLES")
public class role {
    @Id 
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "nombre_rol", nullable = false)
    private String nombre_rol;
}

/*
 * posibles roles 
 * 1 - ADMIN
 * 2 - CLIENTE
 * 3 - TECNICO
 * 4 - INVITADO
 */
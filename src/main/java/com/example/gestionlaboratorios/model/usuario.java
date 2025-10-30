package com.example.gestionlaboratorios.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "USUARIOS")
public class usuario {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name="rut", unique = true, nullable = false, length = 10)
    private Long rut;

    @Column(name="dv", nullable = false, length = 1)
    private String dv;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "email", unique = true, nullable = false, length = 250)
    private String email;

    @Column(name = "contrasena", nullable = false, length = 15)
    private String contrasena;

    @Column(name = "rol_id", nullable = false)
    private Long rolId;
}

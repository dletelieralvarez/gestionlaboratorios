package com.example.gestionlaboratorios.Resultados.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "resultado")
public class Resultado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USUARIO_ID", nullable = false, updatable = false)
    private Long usuarioId; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAnalisis tipo; 

    private Long idLaboratorio;

    @Lob
    //@Column(columnDefinition = "TEXT")
    private String valores;

    private OffsetDateTime fechaMuestra; 
    private OffsetDateTime fechaResultado; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoResultado estado = EstadoResultado.Pendiente;

    @Column(nullable = false)
    private OffsetDateTime creado = OffsetDateTime.now();

    private OffsetDateTime actualizado;

    @PreUpdate
    public void onUpdate() {
        this.actualizado = OffsetDateTime.now();
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}

    public TipoAnalisis getTipo() {return tipo;}
    public void setTipo(TipoAnalisis tipo) {this.tipo = tipo;}

    public Long getIdLaboratorio() {return idLaboratorio;}
    public void setIdLaboratorio(Long idLaboratorio) {this.idLaboratorio = idLaboratorio;}

    public String getValores() {return valores;}
    public void setValores(String valores) {this.valores = valores;}

    public OffsetDateTime getFechaMuestra() {return fechaMuestra;}
    public void setFechaMuestra(OffsetDateTime fechaMuestra) {this.fechaMuestra = fechaMuestra;}

    public OffsetDateTime getFechaResultado() {return fechaResultado;}
    public void setFechaResultado(OffsetDateTime fechaResultado) {this.fechaResultado = fechaResultado;}

    public EstadoResultado getEstado() {return estado;}
    public void setEstado(EstadoResultado estado) {this.estado = estado;}

    public OffsetDateTime getCreado() {return creado;}
    public void setCreado(OffsetDateTime creado) {this.creado = creado;}
    
    public OffsetDateTime getActualizado() {return actualizado;}
    public void setActualizado(OffsetDateTime actualizado) {this.actualizado = actualizado;}
}

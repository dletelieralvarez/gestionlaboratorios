package com.example.gestionlaboratorios.Resultados.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.example.gestionlaboratorios.Usuarios.model.UsuariosPortal;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter 
@Table(name = "resultado")
public class Resultado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "USUARIO_ID", nullable = false)
    private Long id;
    
    //relacion con usuarios_portal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    @JsonIgnore
    private UsuariosPortal usuarioPortal;

    public Long getUsuarioId() {
        return usuarioPortal != null ? usuarioPortal.getId() : null;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAnalisis tipo; 

    @Column(name="ID_LABORATORIO", nullable = false)
    private Long idLaboratorio;

    @Lob
    //@Column(columnDefinition = "TEXT")
    private String valores;

    @Column(name = "FECHA_MUESTRA", nullable = false)
    private LocalDate fechaMuestra; 

    @Column(name = "FECHA_RESULTADO", nullable = false)
    private LocalDate fechaResultado; 

    @Enumerated(EnumType.STRING)
    @Column(name="ESTADO", nullable = false)
    private EstadoResultado estado = EstadoResultado.Pendiente;

    @Column(name = "CREADO", nullable = false)
    private OffsetDateTime  creado = OffsetDateTime.now();

    @Column(name = "ACTUALIZADO")
    private OffsetDateTime actualizado;


    @PrePersist
    public void prePersist(){
        if(fechaMuestra==null){
            fechaMuestra=LocalDate.now();
        }

        if(fechaResultado == null && fechaMuestra != null){
            fechaResultado = fechaMuestra.plusDays(3);
            estado = EstadoResultado.En_Proceso;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.actualizado = OffsetDateTime.now();
    }

}

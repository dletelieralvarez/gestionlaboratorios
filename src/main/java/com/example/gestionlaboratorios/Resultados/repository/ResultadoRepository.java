package com.example.gestionlaboratorios.Resultados.repository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.gestionlaboratorios.Resultados.model.Resultado;
import com.example.gestionlaboratorios.Resultados.model.TipoAnalisis;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

    List<Resultado> findByUsuarioId(Long usuarioId);
    List<Resultado> findByUsuarioIdAndTipo(Long usuarioId, TipoAnalisis tipo);
    
    @Query("SELECT r FROM Resultado r " +
           "WHERE r.usuarioId = :usuarioId " +
           "AND (:idLaboratorio IS NULL OR r.idLaboratorio = :idLaboratorio) " +
           "AND (:fechaMuestra IS NULL OR r.fechaMuestra = :fechaMuestra)")
    List<Resultado> buscarPorFiltros(
            @Param("usuarioId") Long usuarioId,
            @Param("idLaboratorio") Long idLaboratorio,
            @Param("fechaMuestra") LocalDate fechaMuestra);

}



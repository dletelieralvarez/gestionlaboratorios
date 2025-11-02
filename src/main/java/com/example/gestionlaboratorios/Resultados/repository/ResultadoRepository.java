package com.example.gestionlaboratorios.Resultados.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gestionlaboratorios.Resultados.model.Resultado;
import com.example.gestionlaboratorios.Resultados.model.TipoAnalisis;

public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

    List<Resultado> findByUsuarioId(Long usuarioId);

    List<Resultado> findByUsuarioIdAndTipo(Long usuarioId, TipoAnalisis tipo);
}



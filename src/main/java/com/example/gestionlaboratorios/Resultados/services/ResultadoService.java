package com.example.gestionlaboratorios.Resultados.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.gestionlaboratorios.Resultados.model.Resultado;

public interface ResultadoService {
    List<Resultado> getAllResultados();
    Optional<Resultado> getResultadoPorId(Long id);
    Resultado insertarResultado(Resultado resultado);
    Resultado actualizarResultado(Resultado resultado, Long id);
    Resultado eliminarResultado(Long id);
    Resultado listaResultadoPorUsuario(Long usuarioId, String tipoAnalisis);
    List<Resultado> listaResultadoPorfiltros(Long usuarioId, Long idLaboratorio, LocalDate fechaMuestra);
}

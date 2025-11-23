package com.example.gestionlaboratorios.Resultados.services;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.gestionlaboratorios.Resultados.model.Resultado;
import com.example.gestionlaboratorios.Resultados.model.TipoAnalisis;
import com.example.gestionlaboratorios.Resultados.repository.ResultadoRepository;

@Slf4j
@Service
public class ResultadoServiceImpl implements ResultadoService {

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Override
    public List<Resultado> getAllResultados() {
        log.debug("Servicio : getAllResultados");
        return resultadoRepository.findAll();
    }

    @Override
    public Optional<Resultado> getResultadoPorId(Long id) {
        log.debug("Servicio : getResultadoPorId con id {}", id);
        return resultadoRepository.findById(id);
    }

    @Override
    public Resultado insertarResultado(Resultado nuevoResultado) {
        log.debug("Servicio : insertarResultado()");
        return resultadoRepository.save(nuevoResultado);
    }

    @Override
    public Resultado actualizarResultado(Resultado resultadoActualizado, Long id) {
        log.debug("Actualizando resultado con id: {}", id);
        return resultadoRepository.save(resultadoActualizado);
    }

    @Override
    public Resultado eliminarResultado(Long id) {
        log.debug("Eliminando resultado con id: {}", id);
        Optional<Resultado> resultado = resultadoRepository.findById(id);
        if(resultado.isPresent()) {
            resultadoRepository.deleteById(id);
            return resultado.get();
        } else {
            return null;
        }
    }
    
    @Override 
    public Resultado listaResultadoPorUsuario(Long usuarioId, String tipoAnalisis) {
        if (tipoAnalisis == null || tipoAnalisis.isBlank()) {
            var lista = resultadoRepository.findByUsuarioPortal_Id(usuarioId);
            return lista.isEmpty() ? null : lista.get(0); 
        }
        TipoAnalisis tipo = TipoAnalisis.valueOf(tipoAnalisis.toUpperCase());
        var lista = resultadoRepository.findByUsuarioPortal_IdAndTipo(usuarioId, tipo);
        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public List<Resultado> listaResultadoPorfiltros(Long usuarioId, Long idLaboratorio, LocalDate fechaMuestra)
    {           
        log.debug("Buscando resultados por filtros: usuarioId={}, idLaboratorio={}, fechaMuestra={}", usuarioId, idLaboratorio, fechaMuestra);
        return resultadoRepository.buscarPorFiltros(usuarioId, idLaboratorio, fechaMuestra);

    }
}

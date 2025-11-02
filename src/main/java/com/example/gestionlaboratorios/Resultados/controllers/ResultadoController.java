package com.example.gestionlaboratorios.Resultados.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestionlaboratorios.Resultados.model.Resultado;
import com.example.gestionlaboratorios.Resultados.services.ResultadoService;
import com.example.gestionlaboratorios.Usuarios.model.Usuario;
import com.example.gestionlaboratorios.Usuarios.services.UsuarioService;
import com.example.gestionlaboratorios.comun.model.ApiResult;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.Link;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/resultados")
public class ResultadoController {
    @Autowired
    private ResultadoService resultadoService;

    @Autowired
    private UsuarioService  usuarioService;

    @GetMapping
    public ResponseEntity<ApiResult<List<Resultado>>> retornaTodosLosResultados() {        
        try
        {
            log.info("Get / retornaTodosLosResultados - Se obtiene lista con todos los resultados");
            List<Resultado> resultados = resultadoService.getAllResultados();
            //si lista de resultados esta vacia mostrará el mensaje
            if(resultados.isEmpty())
            {
                 return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResult<>("No se encontraron resultados", null, HttpStatus.NOT_FOUND.value())); 
            }
            
            //si lista tiene datos retorna el mensaje encontrados mas la lista y el estado
            return ResponseEntity.ok(
                new ApiResult<>("Resultados encontrados", resultados, HttpStatus.OK.value())
            );
        }
        catch(Exception e){
            log.error("Error al obtener los resultados: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>("Error al obtener los resultados: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResult<List<Resultado>>> retornaResultadoPorUsuario(@PathVariable("usuarioId") Long usuarioId,
     @RequestParam(required = false) String tipoAnalisis,
     @RequestParam(required = false) Long idLaboratorio, 
     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaMuestra)
     {       
        try {
        log.info("Get / retornaResultadoPorUsuario - usuarioId={}, tipoAnalisis={}, idLaboratorio={}, fechaMuestra={}",
         usuarioId, tipoAnalisis, idLaboratorio, fechaMuestra);

        Usuario usuario = usuarioService.getUsuarioPorId(usuarioId).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResult<>("Usuario no encontrado con ID: " + usuarioId, null, HttpStatus.NOT_FOUND.value()));
        }

        List<Resultado> resultado = resultadoService.listaResultadoPorfiltros(usuarioId, idLaboratorio, fechaMuestra);
        if (resultado == null || resultado.isEmpty()) { 
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResult<>("No se encontraron resultados para el usuario con ID: " + usuarioId, null, HttpStatus.NOT_FOUND.value()));
        }

        return ResponseEntity.ok(new ApiResult<>("Resultados encontrados", resultado, HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiResult<>("Tipo de análisis inválido: " + e.getMessage(), null, HttpStatus.BAD_REQUEST.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResult<>("Error al obtener los resultados: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<Resultado>> retornaResultadoPorId(@Valid @PathVariable Long id){
        try{

            log.info("Get / retornaResultadoPorId - Se obtiene el resultado por ID: ", id);
            Optional<Resultado> resultadoOpt = resultadoService.getResultadoPorId(id);

            if(resultadoOpt.isPresent())
            {
                Resultado resultado = resultadoOpt.get();
                log.info("Resultado encontrado con ID: ", id);
                //aqui genero link HATEOAS
                List<Link> links = List.of(
                    linkTo(methodOn(ResultadoController.class).retornaResultadoPorId(id)).withRel("Retorna Resultado por ID"),
                    linkTo(methodOn(ResultadoController.class).retornaTodosLosResultados()).withRel("Retorna Todos los Resultados"),
                    linkTo(methodOn(ResultadoController.class).actualizarResultado(id, resultado)).withRel("Actualizar Resultado"),
                    linkTo(methodOn(ResultadoController.class).eliminarResultado(id)).withRel("Eliminar Resultado")
                );
                //si encuentra el resultado por id retorna el mensaje encontrado mas el resultado y el estado y links
                return ResponseEntity.ok(
                    new ApiResult<>("Resultado encontrado", resultado, HttpStatus.OK.value(), links));
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResult<>("Resultado no encontrado con ID: " + id, null, HttpStatus.NOT_FOUND.value(), null));
            }

        }
        catch(Exception e){
            log.error("Error al obtener el resultado por ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>("Error al obtener el resultado: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResult<List<Resultado>>> guardarNuevoResultado(@Valid @RequestBody Resultado nuevoResultado) {
        try{
            log.info("Post / guardarNuevoResultado - Se guarda un nuevo resultado: {}", nuevoResultado.toString());
            Resultado resultado = resultadoService.insertarResultado(nuevoResultado);

            ApiResult<List<Resultado>> respuesta = new ApiResult<List<Resultado>>(
               "Resultado insertado con éxito", List.of(resultado), HttpStatus.CREATED.value()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }
        catch(Exception e){
            ApiResult<List<Resultado>> respuesta = new ApiResult<List<Resultado>>(
               "Error al insertar el resultado: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<List<Resultado>>> actualizarResultado(@PathVariable Long id, @Valid @RequestBody Resultado resultado) {
        try{
            log.info("Put / actualizarResultado - Se actualiza resultado con ID: " + id);
            Optional<Resultado> buscaResultado = resultadoService.getResultadoPorId(id);

            if(!buscaResultado.isPresent()){
                log.warn("No se encontró Resultado con el id : " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResult<>("Resultado no encontrado", null, HttpStatus.NOT_FOUND.value(), null));
            }

            //solo permitiré modificar la fecha de resultado del examen y el estado
            Resultado actualiza = buscaResultado.get();
            actualiza.setEstado(resultado.getEstado());
            //guarda la modificacion de estado
            log.info("Resultado actualizado, Id : " + id);
            Resultado resultadoActualizado = resultadoService.actualizarResultado(actualiza, id);

            //links HATEOAS
            List<Link> links = List.of(
                linkTo(methodOn(ResultadoController.class).actualizarResultado(id, resultado)).withRel("Actualizar Resultado"),
                linkTo(methodOn(ResultadoController.class).retornaTodosLosResultados()).withRel("Retorna Todos los Resultados"),
                linkTo(methodOn(ResultadoController.class).retornaResultadoPorId(id)).withRel("Retorna Resultado por ID"),
                linkTo(methodOn(ResultadoController.class).eliminarResultado(id)).withRel("Eliminar Resultado")
            );

            //retorna el mensaje con apiresult
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResult<>("Resultado actualizado con éxito", List.of(resultadoActualizado), HttpStatus.OK.value(), links));
        }
        catch(Exception e){
            log.error("Error al actualizar Resultado con el id" + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>("Error al actualizar el resultado " , null, HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> eliminarResultado(@Valid @PathVariable Long id) {
        try{
            log.info("Delete / eliminarResultado - se elimina el resultado con ID: " + id);
            //retorna los datos de RESULTADO segun el id 
            Optional<Resultado> resultadoExistente = resultadoService.getResultadoPorId(id);
            //si no lo encuentra entra en el if y retorna el mensaje
            if(!resultadoExistente.isPresent()){
                log.warn("No se encontró Resultado con el id : " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResult<>("Resultado no encontrado", null, HttpStatus.NOT_FOUND.value(), null));
            }            
            //si encuentra el resultado lo elimina y retorna el mensaje de exito 
            log.info("Resultado encontrado con id " + id);
            resultadoService.eliminarResultado(id);
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResult<>("Resultado eliminado con éxito", "Resultado con ID " + id + " eliminado.", HttpStatus.OK.value()));

        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>("Error al eliminar el resultado: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }
}

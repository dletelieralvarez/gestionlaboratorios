package com.example.gestionlaboratorios.Resultados.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.lang.NonNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.example.gestionlaboratorios.Resultados.controllers.ResultadoController;
import com.example.gestionlaboratorios.Resultados.model.Resultado;

public class RespuestaModelAssembler implements RepresentationModelAssembler<Resultado, EntityModel<Resultado>> {
    @Override
    public @NonNull EntityModel<Resultado> toModel(@NonNull Resultado entidadResultado) {
        return EntityModel.of(entidadResultado,
                linkTo(methodOn(ResultadoController.class).retornaResultadoPorId(entidadResultado.getId())).withSelfRel(),
                linkTo(methodOn(ResultadoController.class).eliminarResultado(entidadResultado.getId())).withRel("delete"),
                linkTo(methodOn(ResultadoController.class).actualizarResultado(entidadResultado.getId(), entidadResultado)).withRel("update"),
                linkTo(methodOn(ResultadoController.class).retornaTodosLosResultados()).withRel("all"));
    }
}

package com.example.gestionlaboratorios.Roles.hateoas;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*; // Importa funciones para generar enlaces
import com.example.gestionlaboratorios.Roles.controllers.RolController;
import com.example.gestionlaboratorios.Roles.model.Rol;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class RolModelAssembler implements RepresentationModelAssembler<Rol, EntityModel<Rol>> {
    //convierte el objeto rol a un objeto EntityModel<rol> que contiene los enlaces
    @Override
    public @NonNull EntityModel<Rol> toModel(@NonNull Rol entidadRol) {
        return EntityModel.of(entidadRol,
                linkTo(methodOn(RolController.class).retornaRolPorId(entidadRol.getId())).withSelfRel(),
                linkTo(methodOn(RolController.class).eliminarRol(entidadRol.getId())).withRel("delete"),
                linkTo(methodOn(RolController.class).actualizarRol(entidadRol.getId(), entidadRol)).withRel("update"), 
                linkTo(methodOn(RolController.class).retornaTodosLosRoles()).withRel("all"));
    }
}

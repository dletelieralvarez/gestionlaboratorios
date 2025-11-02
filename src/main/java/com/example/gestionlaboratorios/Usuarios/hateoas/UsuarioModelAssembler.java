package com.example.gestionlaboratorios.Usuarios.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.lang.NonNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.gestionlaboratorios.Usuarios.controllers.UsuarioController;
import com.example.gestionlaboratorios.Usuarios.model.Usuario;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {
    //convierte el objeto usuario a un objeto EntityModel<Usuario> que contiene los enlaces
    @Override
    public @NonNull EntityModel<Usuario> toModel(@NonNull Usuario entidadUsuario) {
        return EntityModel.of(entidadUsuario,
                linkTo(methodOn(UsuarioController.class).retornaUsuarioPorId(entidadUsuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).eliminarUsuario(entidadUsuario.getId())).withRel("delete"),
                linkTo(methodOn(UsuarioController.class).actualizarUsuario(entidadUsuario.getId(), entidadUsuario)).withRel("update"),
                linkTo(methodOn(UsuarioController.class).retornaTodosLosUsuarios()).withRel("all"));
    }
}

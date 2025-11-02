package com.example.gestionlaboratorios.comun.model;
import org.springframework.hateoas.Link;
import java.util.List;

public class ApiResult<T> {
   
        private String mensaje;
        private T data;
        private int codigoEstado;
        //se adapta mi clase ApiResult para que retorne los links de HATEOAS
        private List<Link> _links;
    
        public ApiResult(String mensaje, T data, int codigoEstado, List<Link> links) {
            this.mensaje = mensaje;
            this.data = data;
            this.codigoEstado = codigoEstado;
            this._links = links;
        }
        //constructor sin links para usar en los controladores que no retornan links
        public ApiResult(String mensaje, T data, int codigoEstado) {
            this(mensaje, data, codigoEstado, null);
        }

        // Getters and setters
    
        public String getMensaje() {
            return mensaje;
        }
    
        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
    
        public T getData() {
            return data;
        }
    
        public void setData(T data) {
            this.data = data;
        }
    
        public int getCodigoEstado() {
            return codigoEstado;
        }
    
        public void setCodigoEstado(int codigoEstado) {
            this.codigoEstado = codigoEstado;
        }
    
        public List<Link> getLinks() {
            return _links;
        }
        public void setLinks(List<Link> links) {
            this._links = links;
        }
}

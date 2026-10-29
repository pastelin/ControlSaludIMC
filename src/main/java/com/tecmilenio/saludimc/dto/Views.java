package com.tecmilenio.saludimc.dto;

import com.tecmilenio.saludimc.domain.*;
import java.time.LocalDateTime;

/** DTOs de salida: nunca exponen contrasenas ni entidades JPA. */
public final class Views {
    private Views(){}
    public record UserView(Long id,String nombreCompleto,String usuario,Integer edad,String sexo,Double estatura,Double pesoInicial){
        public static UserView of(Usuario x){return new UserView(x.id,x.nombreCompleto,x.usuario,x.edad,x.sexo,x.estatura,x.pesoInicial);}
    }
    public record ImcView(Long id,Double peso,Double estatura,Double valorImc,String estado,LocalDateTime fecha){
        public static ImcView of(Medicion x){return new ImcView(x.id,x.peso,x.estatura,x.valorImc,x.estado,x.fecha);}
    }
    public record DirectoryView(Long id,String nombre){
        public static DirectoryView of(Directorio x){return new DirectoryView(x.id,x.nombre);}
    }
    public record FileView(Long id,String nombre,Long directorioId,LocalDateTime fecha){
        public static FileView of(Archivo x){return new FileView(x.id,x.nombre,x.directorio.id,x.fecha);}
    }
    public record NotifyView(Long id,String mensaje,boolean leida,LocalDateTime fecha){
        public static NotifyView of(Notificacion x){return new NotifyView(x.id,x.mensaje,x.leida,x.fecha);}
    }
}

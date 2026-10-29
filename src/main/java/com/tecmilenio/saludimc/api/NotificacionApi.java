package com.tecmilenio.saludimc.api;

import com.tecmilenio.saludimc.domain.Notificacion;
import com.tecmilenio.saludimc.dto.Views.*;
import com.tecmilenio.saludimc.repo.NotificacionRepository;
import com.tecmilenio.saludimc.service.UsuarioService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value="/api/notify",produces={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
public class NotificacionApi {
    private final NotificacionRepository notificaciones;
    private final UsuarioService usuarios;
    public NotificacionApi(NotificacionRepository notificaciones,UsuarioService usuarios){
        this.notificaciones=notificaciones;this.usuarios=usuarios;
    }
    private Notificacion buscar(Long u,Long id){
        return notificaciones.findByIdAndUsuarioId(id,u).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"Notificación no encontrada"));
    }
    @GetMapping public Object listar(Authentication auth){
        var u=usuarios.actual(auth.getName());
        return ApiSupport.response(notificaciones.findByUsuarioIdOrderByFechaDesc(u.id).stream().map(NotifyView::of).toList(),
            "self","/api/notify","GET","imc","/api/imc","GET");
    }
    @GetMapping("/{id}") public Object uno(Authentication auth,@PathVariable Long id){
        return ApiSupport.response(NotifyView.of(buscar(usuarios.actual(auth.getName()).id,id)),
            "self","/api/notify/"+id,"GET","markRead","/api/notify/"+id+"/read","PATCH",
            "delete","/api/notify/"+id,"DELETE");
    }
    @PatchMapping("/{id}/read") public Object leida(Authentication auth,@PathVariable Long id){
        var n=buscar(usuarios.actual(auth.getName()).id,id);n.leida=true;notificaciones.save(n);
        return ApiSupport.response(NotifyView.of(n),"self","/api/notify/"+id,"GET");
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(Authentication auth,@PathVariable Long id){
        notificaciones.delete(buscar(usuarios.actual(auth.getName()).id,id));
        return ResponseEntity.noContent().build();
    }
}

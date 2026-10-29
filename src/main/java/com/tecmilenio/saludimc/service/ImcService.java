package com.tecmilenio.saludimc.service;

import com.tecmilenio.saludimc.domain.*;
import com.tecmilenio.saludimc.dto.Inputs;
import com.tecmilenio.saludimc.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ImcService {
    private final MedicionRepository mediciones;
    private final NotificacionRepository notificaciones;
    public ImcService(MedicionRepository mediciones,NotificacionRepository notificaciones){
        this.mediciones=mediciones;this.notificaciones=notificaciones;
    }
    public List<Medicion> historial(Usuario u){return mediciones.findByUsuarioIdOrderByFechaDesc(u.id);}
    public Medicion buscar(Usuario u,Long id){return mediciones.findByIdAndUsuarioId(id,u.id)
        .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Medición no encontrada"));}
    @Transactional
    public Medicion guardar(Usuario u, Inputs.MedicionInput input){
        Medicion m=new Medicion();m.usuario=u;m.peso=input.peso();m.estatura=input.estatura();
        m.valorImc=BmiCalculator.calcular(m.peso,m.estatura);m.estado=BmiCalculator.clasificar(m.valorImc);
        mediciones.save(m);
        Notificacion n=new Notificacion();n.usuario=u;n.mensaje="Medición guardada: IMC " + m.valorImc + " (" + m.estado + ").";
        notificaciones.save(n);
        return m;
    }
    @Transactional public void eliminar(Usuario u,Long id){mediciones.delete(buscar(u,id));}
}

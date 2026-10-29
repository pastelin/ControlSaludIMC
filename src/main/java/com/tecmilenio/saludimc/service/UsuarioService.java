package com.tecmilenio.saludimc.service;

import com.tecmilenio.saludimc.domain.*;
import com.tecmilenio.saludimc.dto.*;
import com.tecmilenio.saludimc.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarios;
    private final DirectorioRepository directorios;
    private final MedicionRepository mediciones;
    private final NotificacionRepository notificaciones;
    private final ArchivoRepository archivos;
    private final PasswordEncoder encoder;
    public UsuarioService(UsuarioRepository usuarios,DirectorioRepository directorios,MedicionRepository mediciones,
             NotificacionRepository notificaciones, ArchivoRepository archivos, PasswordEncoder encoder){
        this.usuarios=usuarios;this.directorios=directorios;this.mediciones=mediciones;
        this.notificaciones=notificaciones;this.archivos=archivos;this.encoder=encoder;
    }
    public Usuario actual(String nombre){return usuarios.findByUsuario(nombre)
        .orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Sesión no válida"));}
    @Transactional
    public Usuario registrar(RegistroForm form){
        if (usuarios.existsByUsuario(form.usuario))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"El nombre de usuario ya existe");
        Usuario u=new Usuario();u.nombreCompleto=form.nombreCompleto.trim();u.usuario=form.usuario.trim();
        u.contrasenaHash=encoder.encode(form.contrasena);u.edad=form.edad;u.sexo=form.sexo;
        u.estatura=form.estatura;u.pesoInicial=form.pesoInicial;usuarios.save(u);
        Directorio d=new Directorio();d.usuario=u;d.nombre="General";directorios.save(d);
        Notificacion n=new Notificacion();n.usuario=u;n.mensaje="¡Bienvenido! Registra tu primera medición de IMC.";
        notificaciones.save(n);
        return u;
    }
    @Transactional
    public Usuario actualizar(Usuario u, Inputs.PerfilInput input){
        u.nombreCompleto=input.nombreCompleto().trim();u.edad=input.edad();u.sexo=input.sexo();u.estatura=input.estatura();
        return usuarios.save(u);
    }
    @Transactional
    public void eliminar(Usuario u){
        archivos.deleteByUsuarioId(u.id);
        notificaciones.deleteByUsuarioId(u.id);
        mediciones.deleteByUsuarioId(u.id);
        directorios.deleteByUsuarioId(u.id);
        usuarios.delete(u);
    }
}

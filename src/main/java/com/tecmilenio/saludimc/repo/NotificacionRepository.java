package com.tecmilenio.saludimc.repo;

import com.tecmilenio.saludimc.domain.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface NotificacionRepository extends JpaRepository<Notificacion,Long> {
    List<Notificacion> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
    Optional<Notificacion> findByIdAndUsuarioId(Long id,Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
}

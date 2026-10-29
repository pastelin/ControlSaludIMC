package com.tecmilenio.saludimc.repo;

import com.tecmilenio.saludimc.domain.Medicion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface MedicionRepository extends JpaRepository<Medicion,Long> {
    List<Medicion> findByUsuarioIdOrderByFechaDesc(Long id);
    Optional<Medicion> findByIdAndUsuarioId(Long id,Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
}

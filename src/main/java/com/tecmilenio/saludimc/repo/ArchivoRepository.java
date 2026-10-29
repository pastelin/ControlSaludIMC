package com.tecmilenio.saludimc.repo;

import com.tecmilenio.saludimc.domain.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ArchivoRepository extends JpaRepository<Archivo,Long> {
    List<Archivo> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
    Optional<Archivo> findByIdAndUsuarioId(Long id,Long usuarioId);
    boolean existsByDirectorioId(Long directorioId);
    void deleteByUsuarioId(Long usuarioId);
}

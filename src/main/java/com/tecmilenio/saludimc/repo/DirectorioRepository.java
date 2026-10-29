package com.tecmilenio.saludimc.repo;

import com.tecmilenio.saludimc.domain.Directorio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface DirectorioRepository extends JpaRepository<Directorio,Long> {
    List<Directorio> findByUsuarioIdOrderByIdAsc(Long usuarioId);
    Optional<Directorio> findByIdAndUsuarioId(Long id,Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
}

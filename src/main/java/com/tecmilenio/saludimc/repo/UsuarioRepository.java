package com.tecmilenio.saludimc.repo;

import com.tecmilenio.saludimc.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByUsuario(String usuario);
    boolean existsByUsuario(String usuario);
}

package com.taller.app.modules.seguridad.usuario_sistema.repository;

import com.taller.app.modules.seguridad.usuario_sistema.model.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioSistemaRepository extends JpaRepository<UsuarioSistema, Integer> {

    Optional<UsuarioSistema> findById(int i);
}

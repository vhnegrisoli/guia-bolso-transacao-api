package br.com.guiabolso.transacaoapi.modulos.usuario.repository;

import br.com.guiabolso.transacaoapi.modulos.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
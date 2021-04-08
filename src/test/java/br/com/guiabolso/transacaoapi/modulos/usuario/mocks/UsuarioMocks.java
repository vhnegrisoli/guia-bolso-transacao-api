package br.com.guiabolso.transacaoapi.modulos.usuario.mocks;

import br.com.guiabolso.transacaoapi.modulos.usuario.model.Usuario;

import java.time.LocalDateTime;

public class UsuarioMocks {

    public static Usuario umUsuario() {
        return Usuario
            .builder()
            .id(1)
            .dataCadastro(LocalDateTime.now())
            .build();
    }
}

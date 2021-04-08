package br.com.guiabolso.transacaoapi.modulos.usuario.mocks;

import br.com.guiabolso.transacaoapi.modulos.usuario.dto.UsuarioTransacoesResponse;

import static br.com.guiabolso.transacaoapi.modulos.transacao.mocks.TransacaoMocks.umaTransacao;

public class UsuarioTransacoesResponseMocks {

    public static UsuarioTransacoesResponse umUsuarioTransacoesResponse() {
        return UsuarioTransacoesResponse.converterDe(umaTransacao());
    }
}

package br.com.guiabolso.transacaoapi.modulos.usuario.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static br.com.guiabolso.transacaoapi.modulos.transacao.mocks.TransacaoMocks.umaTransacao;
import static org.assertj.core.api.Assertions.assertThat;

public class UsuarioTransacoesResponseTest {

    @Test
    @DisplayName("Deve converter para DTO de resposta de UsuarioTransacaoResponse quando informar um Model de Transacao")
    public void converterDe_deveConverterParaDtoDeUsuarioTransacoesResponse_quandoInformarModelDeTransacao() {
        var response = UsuarioTransacoesResponse.converterDe(umaTransacao());
        assertThat(response).isNotNull();
        assertThat(response.getDescricao()).isEqualTo("pnmzukmzxapozvjxeflttnkaeidqabkejoruaxxi");
        assertThat(response.getData()).isEqualTo(1577952660000L);
        assertThat(response.getValor()).isEqualTo(1291915);
        assertThat(response.getDuplicated()).isEqualTo(false);
    }
}

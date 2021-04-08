package br.com.guiabolso.transacaoapi.modulos.transacao.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static br.com.guiabolso.transacaoapi.modulos.usuario.mocks.UsuarioMocks.umUsuario;
import static org.assertj.core.api.Assertions.assertThat;

public class TransacaoTest {

    @Test
    @DisplayName("Deve gerar Model de Transação quando informar Model de Usuario e objeto de YearMonth de data referência")
    public void gerarTransacao_deveGerarModelDeTransacao_quandoInformarModelDeUsuarioEObjetoDeYearMonthDeDataReferencia() {
        var transacao = Transacao.gerarTransacao(umUsuario(), YearMonth.of(2020, 1));
        assertThat(transacao).isNotNull();
        assertThat(transacao.getUsuario().getId()).isEqualTo(1);
        assertThat(transacao.getDataReferencia()).isEqualTo(YearMonth.of(2020, 1));
        assertThat(transacao.getDescricao()).isNotNull();
        assertThat(transacao.getValor()).isNotNull();
        assertThat(transacao.getDataTransacao()).isNotNull();
        assertThat(transacao.getDuplicated()).isFalse();
    }
}

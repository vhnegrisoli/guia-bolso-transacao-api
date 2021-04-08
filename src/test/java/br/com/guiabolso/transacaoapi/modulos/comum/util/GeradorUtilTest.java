package br.com.guiabolso.transacaoapi.modulos.comum.util;

import br.com.guiabolso.transacaoapi.modulos.comum.utilitarios.GeradorUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

public class GeradorUtilTest {

    @Test
    @DisplayName("Deve gerar número aleatório em um intervalo quando informar número inicial e final")
    public void gerarNumeroAleatorioEntre_deveGerarNumeroAleatorio_quandoInformarIntervalo() {
        var numeroAleatorio = GeradorUtil.gerarNumeroAleatorioEntre(1, 10);
        assertThat(numeroAleatorio >= 1).isTrue();
        assertThat(numeroAleatorio <= 10).isTrue();
    }

    @Test
    @DisplayName("Deve gerar data aleatória quando informar data de referência  no formato YearMonth")
    public void gerarDataAleatoriaEntreDataDeReferencia_deveGerarDataAleatoria_quandoInformarIntervalo() {
        var dataReferencia = YearMonth.of(2020, 1);
        var dataAleatoria = GeradorUtil.gerarDataAleatoriaEntreDataDeReferencia(dataReferencia);
        assertThat(dataAleatoria).isNotNull();
    }

    @Test
    @DisplayName("Deve gerar String aleatória e legível quando solicitado")
    public void gerarStringAleatoriaLegivel_deveGerarStringAleatoriaLegivel_quandoSolicitar() {
        var stringAleatoriaLegivel = GeradorUtil.gerarStringAleatoriaLegivel();
        assertThat(stringAleatoriaLegivel).isNotNull();
        assertThat(stringAleatoriaLegivel.length() >= 10).isTrue();
        assertThat(stringAleatoriaLegivel.length() <= 60).isTrue();
    }
}

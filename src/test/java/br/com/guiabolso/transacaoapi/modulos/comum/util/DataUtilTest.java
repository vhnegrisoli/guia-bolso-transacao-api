package br.com.guiabolso.transacaoapi.modulos.comum.util;

import br.com.guiabolso.transacaoapi.modulos.comum.utilitarios.DataUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DataUtilTest {

    @Test
    @DisplayName("Deve converter objeto LocalDateTime para Long no formato de milissegundos")
    public void converterParaMilissegundos_deveConverterParaMilissegundos_quandoInformarLocalDateTime() {
        assertThat(DataUtil.converterParaMilissegundos(
            LocalDateTime.of(2020, 1, 1, 0, 0))).isNotNull();
    }
}

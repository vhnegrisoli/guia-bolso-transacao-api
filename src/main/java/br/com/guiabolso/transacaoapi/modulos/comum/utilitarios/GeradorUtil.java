package br.com.guiabolso.transacaoapi.modulos.comum.utilitarios;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.Random;

import static br.com.guiabolso.transacaoapi.modulos.comum.constantes.Constantes.*;

public class GeradorUtil {

    public static Integer gerarNumeroAleatorioEntre(Integer numeroInicial, Integer numeroFinal) {
        return new Random()
            .ints(numeroInicial, numeroFinal + UM)
            .findFirst()
            .orElse(numeroInicial);
    }

    public static String gerarStringAleatoriaLegivel() {
        return RandomStringUtils
            .randomAlphabetic(gerarNumeroAleatorioEntre(VALOR_MINIMO_TAMANHO_DESCRICAO, VALOR_MAXIMO_TAMANHO_DESCRICAO))
            .toLowerCase();
    }

    public static Long gerarDataAleatoriaEntreDataDeReferencia(YearMonth dataReferencia) {
        return DataUtil.converterParaMilissegundos(LocalDateTime.of(
            dataReferencia.getYear(),
            dataReferencia.getMonthValue(),
            gerarNumeroAleatorioEntre(UM, recuperarUltimoDiaDoMes(dataReferencia)),
            gerarNumeroAleatorioEntre(VALOR_MINIMO_HORA, VALOR_MAXIMO_HORA),
            gerarNumeroAleatorioEntre(VALOR_MINIMO_MINUTO, VALOR_MAXIMO_MINUTO))
        );
    }

    private static Integer recuperarUltimoDiaDoMes(YearMonth dataReferencia) {
        var dataLocal = LocalDate.of(dataReferencia.getYear(), dataReferencia.getMonthValue(), UM);
        return dataLocal.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
    }
}

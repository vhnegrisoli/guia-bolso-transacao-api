package br.com.guiabolso.transacaoapi.modulos.comum.utilitarios;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static br.com.guiabolso.transacaoapi.modulos.comum.constantes.Constantes.SAO_PAULO_TIMEZONE;

public class DataUtil {

    public static Long converterParaMilissegundos(LocalDateTime data) {
        return data
            .atZone(ZoneId.of(SAO_PAULO_TIMEZONE))
            .toInstant()
            .toEpochMilli();
    }
}

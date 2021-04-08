package br.com.guiabolso.transacaoapi.modulos.transacao.mocks;

import br.com.guiabolso.transacaoapi.modulos.transacao.model.Transacao;

import java.time.YearMonth;
import java.util.List;

import static br.com.guiabolso.transacaoapi.modulos.usuario.mocks.UsuarioMocks.umUsuario;

public class TransacaoMocks {

    public static Transacao umaTransacao() {
        return Transacao
            .builder()
            .id(1)
            .dataReferencia(YearMonth.of(2020, 1))
            .dataTransacao(1577952660000L)
            .descricao("pnmzukmzxapozvjxeflttnkaeidqabkejoruaxxi")
            .duplicated(false)
            .usuario(umUsuario())
            .valor(1291915)
            .build()
            ;
    }

    public static List<YearMonth> umaLista12Meses() {
        return List.of(
            YearMonth.of(2020, 1),
            YearMonth.of(2020, 2),
            YearMonth.of(2020, 3),
            YearMonth.of(2020, 4),
            YearMonth.of(2020, 5),
            YearMonth.of(2020, 6),
            YearMonth.of(2020, 7),
            YearMonth.of(2020, 8),
            YearMonth.of(2020, 9),
            YearMonth.of(2020, 10),
            YearMonth.of(2020, 11),
            YearMonth.of(2020, 12)
        );
    }
}

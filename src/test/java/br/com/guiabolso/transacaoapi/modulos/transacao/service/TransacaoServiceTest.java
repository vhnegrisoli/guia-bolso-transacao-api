package br.com.guiabolso.transacaoapi.modulos.transacao.service;

import br.com.guiabolso.transacaoapi.modulos.transacao.model.Transacao;
import br.com.guiabolso.transacaoapi.modulos.transacao.repository.TransacaoRepository;
import br.com.guiabolso.transacaoapi.modulos.usuario.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.guiabolso.transacaoapi.modulos.transacao.mocks.TransacaoMocks.umaLista12Meses;
import static br.com.guiabolso.transacaoapi.modulos.transacao.mocks.TransacaoMocks.umaTransacao;
import static br.com.guiabolso.transacaoapi.modulos.usuario.mocks.UsuarioMocks.umUsuario;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService transacaoService;
    @Mock
    private TransacaoRepository transacaoRepository;

    @Test
    @DisplayName("Deve buscar transações quando já existir por usuário e data de referência")
    public void buscarTransacoesPorUsuarioEDataReferencia_deveBuscarTransacoes_quandoJaExistirPorUsuarioEDataReferencia() {
        when(transacaoRepository.existsByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class)))
            .thenReturn(true);
        when(transacaoRepository.findByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class)))
            .thenReturn(List.of(umaTransacao()));

        var response = transacaoService
            .buscarTransacoesPorUsuarioEDataReferencia(umUsuario(), YearMonth.of(2020, 1));

        assertThat(response).isNotNull();
        assertThat(response)
            .extracting("descricao", "data", "valor", "duplicated")
            .containsExactly(
                tuple("pnmzukmzxapozvjxeflttnkaeidqabkejoruaxxi", 1577952660000L, 1291915, false)
            );

        verify(transacaoRepository, times(1))
            .existsByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class));
        verify(transacaoRepository, times(1))
            .findByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class));
        verify(transacaoRepository, times(0)).saveAll(anyList());
        verify(transacaoRepository, times(0)).findByDataReferenciaIn(anyList());
        verify(transacaoRepository, times(0)).save(any(Transacao.class));
        verify(transacaoRepository, times(0))
            .findByDescricaoAndDataReferenciaAndValor(anyString(), any(YearMonth.class), anyInt());
    }

    @Test
    @DisplayName("Deve gerar e buscar transações quando não existir por usuário e data de referência")
    public void buscarTransacoesPorUsuarioEDataReferencia_deveGerarEBuscarTransacoes_quandoNaoExistirPorUsuarioEData() {
        when(transacaoRepository.existsByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class)))
            .thenReturn(false);
        when(transacaoRepository.findByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class)))
            .thenReturn(List.of(umaTransacao()));
        when(transacaoRepository.findByDataReferenciaIn(anyList())).thenReturn(Collections.emptyList());
        when(transacaoRepository.findByDescricaoAndDataReferenciaAndValor(anyString(), any(YearMonth.class), anyInt()))
            .thenReturn(Collections.emptyList());

        var response = transacaoService
            .buscarTransacoesPorUsuarioEDataReferencia(umUsuario(), YearMonth.of(2020, 1));

        assertThat(response).isNotNull();
        assertThat(response)
            .extracting("descricao", "data", "valor", "duplicated")
            .containsExactly(
                tuple("pnmzukmzxapozvjxeflttnkaeidqabkejoruaxxi", 1577952660000L, 1291915, false)
            );

        verify(transacaoRepository, times(1))
            .existsByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class));
        verify(transacaoRepository, times(1)).saveAll(anyList());
        verify(transacaoRepository, times(1))
            .findByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class));
        verify(transacaoRepository, times(1)).findByDataReferenciaIn(anyList());
        verify(transacaoRepository, times(0)).findTop1ByDataReferencia(any(YearMonth.class));
        verify(transacaoRepository, times(0)).save(any(Transacao.class));
        verify(transacaoRepository, times(1))
            .findByDescricaoAndDataReferenciaAndValor(anyString(), any(YearMonth.class), anyInt());
    }

    @Test
    @DisplayName("Deve gerar e buscar transações e gerar duplicidades quando não existir por usuário e data de referência")
    public void buscarTransacoesPorUsuarioEDataReferencia_deveGerarTransacoesEDuplicidade_quandoNaoExistirPorUsuarioEData() {
        when(transacaoRepository.existsByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class)))
            .thenReturn(false);
        when(transacaoRepository.findByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class)))
            .thenReturn(List.of(umaTransacao()));
        when(transacaoRepository.findByDataReferenciaIn(anyList())).thenReturn(Collections.emptyList());
        when(transacaoRepository.findByDescricaoAndDataReferenciaAndValor(anyString(), any(YearMonth.class), anyInt()))
            .thenReturn(List.of(umaTransacao(), umaTransacao()));

        var response = transacaoService
            .buscarTransacoesPorUsuarioEDataReferencia(umUsuario(), YearMonth.of(2020, 1));

        assertThat(response).isNotNull();
        assertThat(response)
            .extracting("descricao", "data", "valor", "duplicated")
            .containsExactly(
                tuple("pnmzukmzxapozvjxeflttnkaeidqabkejoruaxxi", 1577952660000L, 1291915, true)
            );

        verify(transacaoRepository, times(1))
            .existsByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class));
        verify(transacaoRepository, times(2)).saveAll(anyList());
        verify(transacaoRepository, times(1))
            .findByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class));
        verify(transacaoRepository, times(1)).findByDataReferenciaIn(anyList());
        verify(transacaoRepository, times(0)).findTop1ByDataReferencia(any(YearMonth.class));
        verify(transacaoRepository, times(0)).save(any(Transacao.class));
        verify(transacaoRepository, times(1))
            .findByDescricaoAndDataReferenciaAndValor(anyString(), any(YearMonth.class), anyInt());
    }

    @Test
    @DisplayName("Deve gerar duplicidades ao completar 12 meses quando não existir por usuário e data de referência")
    public void buscarTransacoesPorUsuarioEDataReferencia_deveTransacoesEDuplicidade12Meses_quandoNaoExistirPorUsuarioEData() {
        when(transacaoRepository.existsByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class)))
            .thenReturn(false);
        when(transacaoRepository.findByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class)))
            .thenReturn(List.of(umaTransacao()));
        when(transacaoRepository.findByDataReferenciaIn(anyList())).thenReturn(umaLista12Meses());
        when(transacaoRepository.findTop1ByDataReferencia(any(YearMonth.class))).thenReturn(Optional.of(umaTransacao()));
        when(transacaoRepository.findByDescricaoAndDataReferenciaAndValor(anyString(), any(YearMonth.class), anyInt()))
            .thenReturn(List.of(umaTransacao(), umaTransacao()));

        var response = transacaoService
            .buscarTransacoesPorUsuarioEDataReferencia(umUsuario(), YearMonth.of(2020, 1));

        assertThat(response).isNotNull();
        assertThat(response)
            .extracting("descricao", "data", "valor", "duplicated")
            .containsExactly(
                tuple("pnmzukmzxapozvjxeflttnkaeidqabkejoruaxxi", 1577952660000L, 1291915, true)
            );

        verify(transacaoRepository, times(1))
            .existsByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class));
        verify(transacaoRepository, times(2)).saveAll(anyList());
        verify(transacaoRepository, times(1))
            .findByUsuarioAndDataReferencia(any(Usuario.class), any(YearMonth.class));
        verify(transacaoRepository, atLeastOnce()).findByDataReferenciaIn(anyList());
        verify(transacaoRepository, atLeastOnce()).findTop1ByDataReferencia(any(YearMonth.class));
        verify(transacaoRepository, atLeastOnce()).save(any(Transacao.class));
        verify(transacaoRepository, times(1))
            .findByDescricaoAndDataReferenciaAndValor(anyString(), any(YearMonth.class), anyInt());
    }
}

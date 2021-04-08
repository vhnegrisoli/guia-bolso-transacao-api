package br.com.guiabolso.transacaoapi.modulos.usuario.service;

import br.com.guiabolso.transacaoapi.modulos.comum.exceptions.ValidacaoException;
import br.com.guiabolso.transacaoapi.modulos.transacao.service.TransacaoService;
import br.com.guiabolso.transacaoapi.modulos.usuario.model.Usuario;
import br.com.guiabolso.transacaoapi.modulos.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static br.com.guiabolso.transacaoapi.modulos.usuario.mocks.UsuarioMocks.umUsuario;
import static br.com.guiabolso.transacaoapi.modulos.usuario.mocks.UsuarioTransacoesResponseMocks.umUsuarioTransacoesResponse;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private TransacaoService transacaoService;

    @Test
    @DisplayName("Deve lançar exception quando informar ID de usuário menor que 1.000")
    public void buscarTransacoesDoUsuarioPorMesEAno_deveLancarException_quandoInformarIdDeUsuarioMenorQue1000() {
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> usuarioService.buscarTransacoesDoUsuarioPorMesEAno(1, 2020, 5))
            .withMessage("O valor do ID do usuário deve estar entre 1.000 e 100.000.000.");
    }

    @Test
    @DisplayName("Deve lançar exception quando informar ID de usuário maior que 100.000.000")
    public void buscarTransacoesDoUsuarioPorMesEAno_deveLancarException_quandoInformarIdDeUsuarioMaiorQue100000000() {
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> usuarioService.buscarTransacoesDoUsuarioPorMesEAno(200000000, 2020, 5))
            .withMessage("O valor do ID do usuário deve estar entre 1.000 e 100.000.000.");
    }

    @Test
    @DisplayName("Deve lançar exception quando ano for nulo")
    public void buscarTransacoesDoUsuarioPorMesEAno_deveLancarException_quandoInformarAnoForNulo() {
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> usuarioService.buscarTransacoesDoUsuarioPorMesEAno(2500, null, 5))
            .withMessage("O ano deve ser informado.");
    }

    @Test
    @DisplayName("Deve lançar exception quando ano for menor que 1970")
    public void buscarTransacoesDoUsuarioPorMesEAno_deveLancarException_quandoInformarAnoMenorQue1970() {
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> usuarioService.buscarTransacoesDoUsuarioPorMesEAno(2500, 1900, 5))
            .withMessage("O valor do ano deve estar entre 1970 e 3000.");
    }

    @Test
    @DisplayName("Deve lançar exception quando ano for maior que 3000")
    public void buscarTransacoesDoUsuarioPorMesEAno_deveLancarException_quandoInformarAnoMaiorQue3000() {
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> usuarioService.buscarTransacoesDoUsuarioPorMesEAno(2500, 10001, 5))
            .withMessage("O valor do ano deve estar entre 1970 e 3000.");
    }

    @Test
    @DisplayName("Deve lançar exception quando mês for nulo")
    public void buscarTransacoesDoUsuarioPorMesEAno_deveLancarException_quandoInformarMesNulo() {
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> usuarioService.buscarTransacoesDoUsuarioPorMesEAno(2500, 2020, null))
            .withMessage("O mês deve ser informado.");
    }

    @Test
    @DisplayName("Deve lançar exception quando mês for menor que 1")
    public void buscarTransacoesDoUsuarioPorMesEAno_deveLancarException_quandoInformarMesMenorQue1() {
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> usuarioService.buscarTransacoesDoUsuarioPorMesEAno(2500, 2020, 0))
            .withMessage("O valor do mês deve estar entre 1 e 12.");
    }

    @Test
    @DisplayName("Deve lançar exception quando mês for maior que 12")
    public void buscarTransacoesDoUsuarioPorMesEAno_deveLancarException_quandoInformarMesMaiorQue12() {
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> usuarioService.buscarTransacoesDoUsuarioPorMesEAno(2500, 2020, 13))
            .withMessage("O valor do mês deve estar entre 1 e 12.");
    }

    @Test
    @DisplayName("Deve buscar transações do usuário e não salvar o usuário quando já existir o ID")
    public void buscarTransacoesDoUsuarioPorMesEAno_deveBuscarUsuarioSemSalvar_quandoIdJaExistir() {
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(umUsuario()));
        when(transacaoService.buscarTransacoesPorUsuarioEDataReferencia(any(), any()))
            .thenReturn(List.of(umUsuarioTransacoesResponse()));

        var response = usuarioService.buscarTransacoesDoUsuarioPorMesEAno(1000, 2020, 5);

        assertThat(response).isNotNull();
        assertThat(response)
            .extracting("descricao", "data", "valor", "duplicated")
            .containsExactly(
                tuple("pnmzukmzxapozvjxeflttnkaeidqabkejoruaxxi", 1577952660000L, 1291915, false)
            );

        verify(usuarioRepository, times(1)).findById(anyInt());
        verify(usuarioRepository, times(0)).save(any(Usuario.class));
        verify(transacaoService, times(1))
            .buscarTransacoesPorUsuarioEDataReferencia(any(Usuario.class), any(YearMonth.class));
    }

    @Test
    @DisplayName("Deve buscar transações do usuário e salvar novo usuário quando não existir o ID")
    public void buscarTransacoesDoUsuarioPorMesEAno_deveSalvarUsuarioEBuscar_quandoIdNaoExistir() {
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any())).thenReturn(umUsuario());
        when(transacaoService.buscarTransacoesPorUsuarioEDataReferencia(any(), any()))
            .thenReturn(List.of(umUsuarioTransacoesResponse()));

        var response = usuarioService.buscarTransacoesDoUsuarioPorMesEAno(1000, 2020, 5);

        assertThat(response).isNotNull();
        assertThat(response)
            .extracting("descricao", "data", "valor", "duplicated")
            .containsExactly(
                tuple("pnmzukmzxapozvjxeflttnkaeidqabkejoruaxxi", 1577952660000L, 1291915, false)
            );

        verify(usuarioRepository, times(1)).findById(anyInt());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(transacaoService, times(1))
            .buscarTransacoesPorUsuarioEDataReferencia(any(Usuario.class), any(YearMonth.class));
    }
}
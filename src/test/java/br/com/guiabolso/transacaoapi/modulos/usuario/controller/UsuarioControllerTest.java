package br.com.guiabolso.transacaoapi.modulos.usuario.controller;

import br.com.guiabolso.transacaoapi.modulos.transacao.model.Transacao;
import br.com.guiabolso.transacaoapi.modulos.transacao.repository.TransacaoRepository;
import br.com.guiabolso.transacaoapi.modulos.usuario.repository.UsuarioRepository;
import br.com.guiabolso.transacaoapi.modulos.usuario.service.UsuarioService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TransacaoRepository transacaoRepository;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void removerDadosIniciais() {
        transacaoRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve buscar transações do usuário por mês e ano quando dados estiverem corretos")
    public void buscarTransacoesDoUsuarioPorMesEAno_deveRetornar200_quandoInformarDadosCorretos() {
        mvc
            .perform(get("/1001/transacoes/2020/1"))
            .andExpect(status().isOk());

        assertThat(usuarioRepository.findById(1001).get().getId()).isEqualTo(1001);
        assertThat(transacaoRepository.findAll().isEmpty()).isFalse();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve buscar transações do usuário por mês e ano em 12 meses quando dados estiverem corretos com duplicidade")
    public void buscarTransacoesDoUsuarioPorMesEAno_deveRetornar200_quandoDer12MesesPossuindoDuplicadas() {
        IntStream
            .rangeClosed(1, 12)
            .forEach(i -> {
                    try {
                        mvc.perform(get(
                            "/1000".concat(String.valueOf(i)).concat("/transacoes/2020/".concat(String.valueOf(i)))))
                            .andExpect(status().isOk());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            );

        var transacoes = transacaoRepository.findAll();
        assertThat(usuarioRepository.findAll().size()).isEqualTo(12);
        assertThat(transacoes.size() >= 12).isTrue();
        var transacoesDuplicadas = transacoes
            .stream()
            .filter(Transacao::getDuplicated)
            .count();
        assertThat(transacoesDuplicadas > 1).isTrue();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve retornar 400 quando informar usuário com ID menor que 1.000 no endpoint")
    public void buscarTransacoesDoUsuarioPorMesEAno_Retornar400_quandoInformarIdMenorQue1000() {
        mvc
            .perform(get("/1/transacoes/2020/1"))
            .andExpect(status().isBadRequest());

        assertThat(usuarioRepository.findAll().isEmpty()).isTrue();
        assertThat(transacaoRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve retornar 400 quando informar usuário com ID maior que 100.000.000 no endpoint")
    public void buscarTransacoesDoUsuarioPorMesEAno_Retornar400_quandoInformarIdMaiorQue100000000() {
        mvc
            .perform(get("/9999999999/transacoes/2020/1"))
            .andExpect(status().isBadRequest());

        assertThat(usuarioRepository.findAll().isEmpty()).isTrue();
        assertThat(transacaoRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve retornar 400 quando informar usuário com ID nulo no endpoint")
    public void buscarTransacoesDoUsuarioPorMesEAno_Retornar400_quandoInformarIdNulo() {
        mvc
            .perform(get("/null/transacoes/2020/1"))
            .andExpect(status().isBadRequest());

        assertThat(usuarioRepository.findAll().isEmpty()).isTrue();
        assertThat(transacaoRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve retornar 404 quando não informar ID de usuário no endpoint")
    public void buscarTransacoesDoUsuarioPorMesEAno_Retornar404_quandoNaoInformarIdDoUsuario() {
        mvc
            .perform(get("//transacoes/2020/1"))
            .andExpect(status().isNotFound());

        assertThat(usuarioRepository.findAll().isEmpty()).isTrue();
        assertThat(transacaoRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve retornar 400 quando informar ano nulo no endpoint")
    public void buscarTransacoesDoUsuarioPorMesEAno_Retornar400_quandoInformarAnoNulo() {
        mvc
            .perform(get("/1001/transacoes/null/1"))
            .andExpect(status().isBadRequest());

        assertThat(usuarioRepository.findAll().isEmpty()).isTrue();
        assertThat(transacaoRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve retornar 404 quando informar ano vazio no endpoint")
    public void buscarTransacoesDoUsuarioPorMesEAno_Retornar400_quandoNaoInformarAnoNoEndpoint() {
        mvc
            .perform(get("/1001/transacoes//1"))
            .andExpect(status().isNotFound());

        assertThat(usuarioRepository.findAll().isEmpty()).isTrue();
        assertThat(transacaoRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve retornar 400 quando informar ano menor que 1970 no endpoint")
    public void buscarTransacoesDoUsuarioPorMesEAno_Retornar400_quandoInformarAnoMenorQue1970() {
        mvc
            .perform(get("/1001/transacoes/1959/1"))
            .andExpect(status().isBadRequest());

        assertThat(usuarioRepository.findAll().isEmpty()).isTrue();
        assertThat(transacaoRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve retornar 400 quando informar ano maior que 3000 no endpoint")
    public void buscarTransacoesDoUsuarioPorMesEAno_Retornar400_quandoInformarAnoMaiorQue3000() {
        mvc
            .perform(get("/1001/transacoes/999999/1"))
            .andExpect(status().isBadRequest());

        assertThat(usuarioRepository.findAll().isEmpty()).isTrue();
        assertThat(transacaoRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve retornar 400 quando informar mês nulo no endpoint")
    public void buscarTransacoesDoUsuarioPorMesEAno_Retornar400_quandoInformarMesNulo() {
        mvc
            .perform(get("/1001/transacoes/2020/null"))
            .andExpect(status().isBadRequest());

        assertThat(usuarioRepository.findAll().isEmpty()).isTrue();
        assertThat(transacaoRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve retornar 404 quando informar mês vazio no endpoint")
    public void buscarTransacoesDoUsuarioPorMesEAno_Retornar400_quandoNaoInformarMesNoEndpoint() {
        mvc
            .perform(get("/1001/transacoes/2020/"))
            .andExpect(status().isNotFound());

        assertThat(usuarioRepository.findAll().isEmpty()).isTrue();
        assertThat(transacaoRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve retornar 400 quando informar mês menor que 1 no endpoint")
    public void buscarTransacoesDoUsuarioPorMesEAno_Retornar400_quandoInformarMesMenorQue1() {
        mvc
            .perform(get("/1001/transacoes/2020/0"))
            .andExpect(status().isBadRequest());

        assertThat(usuarioRepository.findAll().isEmpty()).isTrue();
        assertThat(transacaoRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @SneakyThrows
    @DisplayName("Deve retornar 400 quando informar mes maior que 12 no endpoint")
    public void buscarTransacoesDoUsuarioPorMesEAno_Retornar400_quandoInformarMesMaiorQue12() {
        mvc
            .perform(get("/1001/transacoes/2020/13"))
            .andExpect(status().isBadRequest());

        assertThat(usuarioRepository.findAll().isEmpty()).isTrue();
        assertThat(transacaoRepository.findAll().isEmpty()).isTrue();
    }
}

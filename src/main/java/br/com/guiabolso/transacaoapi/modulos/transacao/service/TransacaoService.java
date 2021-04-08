package br.com.guiabolso.transacaoapi.modulos.transacao.service;

import br.com.guiabolso.transacaoapi.modulos.transacao.model.Transacao;
import br.com.guiabolso.transacaoapi.modulos.transacao.repository.TransacaoRepository;
import br.com.guiabolso.transacaoapi.modulos.usuario.dto.UsuarioTransacoesResponse;
import br.com.guiabolso.transacaoapi.modulos.usuario.model.Usuario;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static br.com.guiabolso.transacaoapi.modulos.comum.constantes.Constantes.*;
import static br.com.guiabolso.transacaoapi.modulos.comum.utilitarios.GeradorUtil.gerarNumeroAleatorioEntre;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    public List<UsuarioTransacoesResponse> buscarTransacoesPorUsuarioEDataReferencia(Usuario usuario,
                                                                                     YearMonth dataReferencia) {
        return tratarTransacoesPorUsuarioEDataReferencia(usuario, dataReferencia)
            .stream()
            .map(UsuarioTransacoesResponse::converterDe)
            .collect(Collectors.toList());
    }

    private List<Transacao> tratarTransacoesPorUsuarioEDataReferencia(Usuario usuario, YearMonth dataReferencia) {
        if (!transacaoRepository.existsByUsuarioAndDataReferencia(usuario, dataReferencia)) {
            return gerarTransacoesParaUsuarioComDataDeReferencia(usuario, dataReferencia);
        }
        return transacaoRepository.findByUsuarioAndDataReferencia(usuario, dataReferencia);
    }

    @Transactional
    private List<Transacao> gerarTransacoesParaUsuarioComDataDeReferencia(Usuario usuario, YearMonth dataReferencia) {
        var novasTransacoes = IntStream
            .rangeClosed(VALOR_MINIMO_NUMERO_TRANSACOES,
                gerarNumeroAleatorioEntre(VALOR_MINIMO_NUMERO_TRANSACOES, VALOR_MAXIMO_NUMERO_TRANSACOES))
            .mapToObj(i -> Transacao.gerarTransacao(usuario, dataReferencia))
            .collect(Collectors.toList());
        transacaoRepository.saveAll(novasTransacoes);
        var transacoes = transacaoRepository.findByUsuarioAndDataReferencia(usuario, dataReferencia);
        validarTransacoesExistentesParaAnoTodo(dataReferencia.getYear());
        transacoes.forEach(this::tratarDuplicidade);
        return transacoes;
    }

    private void validarTransacoesExistentesParaAnoTodo(Integer ano) {
        var mesesDoAno = IntStream
            .rangeClosed(VALOR_MINIMO_MES, VALOR_MAXIMO_MES)
            .mapToObj(mes -> YearMonth.of(ano, mes))
            .collect(Collectors.toList());
        var datasExistentesParaAno = transacaoRepository.findByDataReferenciaIn(mesesDoAno);
        if (datasExistentesParaAno.size() == VALOR_MAXIMO_MES) {
            var quantidadeMeses = gerarNumeroAleatorioEntre(VALOR_MINIMO_DUPLICIDADES_MENSAIS, VALOR_MAXIMO_MES);
            IntStream
                .rangeClosed(VALOR_MINIMO_MES, quantidadeMeses)
                .forEach(mes -> gerarDuplicidadeParaMesAno(ano, mes));
        }
    }

    @Transactional
    private void gerarDuplicidadeParaMesAno(Integer ano, Integer mes) {
        var dataReferencia = YearMonth.of(ano, mes);
        transacaoRepository.findTop1ByDataReferencia(dataReferencia)
            .ifPresent(transacaoExistente -> {
                var transacaoComDuplicidade = new Transacao();
                BeanUtils.copyProperties(transacaoExistente, transacaoComDuplicidade);
                transacaoComDuplicidade.setarDuplicacaoParaNovaTransacao();
                transacaoRepository.save(transacaoComDuplicidade);
            });
    }

    private void tratarDuplicidade(Transacao transacao) {
        var transacoesExistentes = transacaoRepository
            .findByDescricaoAndDataReferenciaAndValor(
                transacao.getDescricao(), transacao.getDataReferencia(), transacao.getValor());
        if (!isEmpty(transacoesExistentes) && transacoesExistentes.size() > VALOR_MINIMO_NUMERO_TRANSACOES) {
            transacao.setDuplicated(true);
            transacoesExistentes.get(PRIMEIRA_TRANSACAO).setDuplicated(false);
            transacoesExistentes
                .stream()
                .skip(PRIMEIRA_TRANSACAO)
                .forEach(transacaoExistente -> transacaoExistente.setDuplicated(true));
            transacaoRepository.saveAll(transacoesExistentes);
        }
    }
}
package br.com.guiabolso.transacaoapi.modulos.usuario.service;

import br.com.guiabolso.transacaoapi.modulos.comum.exceptions.ValidacaoException;
import br.com.guiabolso.transacaoapi.modulos.transacao.service.TransacaoService;
import br.com.guiabolso.transacaoapi.modulos.usuario.dto.UsuarioTransacoesResponse;
import br.com.guiabolso.transacaoapi.modulos.usuario.model.Usuario;
import br.com.guiabolso.transacaoapi.modulos.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.YearMonth;
import java.util.List;

import static br.com.guiabolso.transacaoapi.modulos.comum.constantes.Constantes.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TransacaoService transacaoService;

    public List<UsuarioTransacoesResponse> buscarTransacoesDoUsuarioPorMesEAno(Integer usuarioId,
                                                                               Integer ano,
                                                                               Integer mes) {
        validarExtensaoUsuarioId(usuarioId);
        validarDataReferencia(ano, mes);
        var dataReferencia = YearMonth.of(ano, mes);
        var usuario = buscarUsuarioOuGerarQuandoForNovoId(usuarioId);
        return transacaoService.buscarTransacoesPorUsuarioEDataReferencia(usuario, dataReferencia);
    }

    private void validarExtensaoUsuarioId(Integer usuarioId) {
        if (usuarioId < VALOR_MINIMO_USUARIO_ID || usuarioId > VALOR_MAXIMO_USUARIO_ID) {
            throw new ValidacaoException("O valor do ID do usuário deve estar entre 1.000 e 100.000.000.");
        }
    }

    private void validarDataReferencia(Integer ano, Integer mes) {
        validarAno(ano);
        validarMes(mes);
    }

    private void validarAno(Integer ano) {
        if (isEmpty(ano)) {
            throw new ValidacaoException("O ano deve ser informado.");
        }
        if (ano < VALOR_MINIMO_ANO || ano > VALOR_MAXIMO_ANO) {
            throw new ValidacaoException("O valor do ano deve estar entre 1970 e 3000.");
        }
    }

    private void validarMes(Integer mes) {
        if (isEmpty(mes)) {
            throw new ValidacaoException("O mês deve ser informado.");
        }
        if (mes < VALOR_MINIMO_MES || mes > VALOR_MAXIMO_MES) {
            throw new ValidacaoException("O valor do mês deve estar entre 1 e 12.");
        }
    }

    @Transactional
    private Usuario buscarUsuarioOuGerarQuandoForNovoId(Integer usuarioId) {
        return usuarioRepository
            .findById(usuarioId)
            .orElseGet(() -> usuarioRepository.save(Usuario.converterDe(usuarioId)));
    }
}
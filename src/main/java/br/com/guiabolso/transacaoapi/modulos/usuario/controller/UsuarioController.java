package br.com.guiabolso.transacaoapi.modulos.usuario.controller;

import br.com.guiabolso.transacaoapi.modulos.usuario.dto.UsuarioTransacoesResponse;
import br.com.guiabolso.transacaoapi.modulos.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("{usuarioId}/transacoes/{ano}/{mes}")
    public List<UsuarioTransacoesResponse> buscarTransacoesDoUsuarioPorMesEAno(@PathVariable Integer usuarioId,
                                                                               @PathVariable Integer ano,
                                                                               @PathVariable Integer mes) {
        return usuarioService.buscarTransacoesDoUsuarioPorMesEAno(usuarioId, ano, mes);
    }
}
package br.com.guiabolso.transacaoapi.modulos.usuario.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UsuarioTest {

    @Test
    @DisplayName("Deve converter para Model de Usuario quando informar inteiro como usuarioId")
    public void converterDe_deveConverterParaModelDeUsuario_quandoInformarInteiroUsuarioId() {
        var usuario = Usuario.converterDe(1);
        assertThat(usuario).isNotNull();
        assertThat(usuario.getId()).isEqualTo(1);
    }
}

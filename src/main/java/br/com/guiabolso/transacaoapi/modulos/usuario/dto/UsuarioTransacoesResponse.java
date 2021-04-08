package br.com.guiabolso.transacaoapi.modulos.usuario.dto;

import br.com.guiabolso.transacaoapi.modulos.transacao.model.Transacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioTransacoesResponse {

    private String descricao;
    private Long data;
    private Integer valor;
    private Boolean duplicated;

    public static UsuarioTransacoesResponse converterDe(Transacao transacao) {
        return UsuarioTransacoesResponse
            .builder()
            .descricao(transacao.getDescricao())
            .data(transacao.getDataTransacao())
            .valor(transacao.getValor())
            .duplicated(transacao.getDuplicated())
            .build();
    }
}

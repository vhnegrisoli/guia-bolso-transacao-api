package br.com.guiabolso.transacaoapi.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDetalhes {
    private int status;
    private String mensagem;
}

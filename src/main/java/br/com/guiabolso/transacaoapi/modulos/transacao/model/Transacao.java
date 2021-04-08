package br.com.guiabolso.transacaoapi.modulos.transacao.model;

import br.com.guiabolso.transacaoapi.modulos.comum.exceptions.ValidacaoException;
import br.com.guiabolso.transacaoapi.modulos.usuario.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.YearMonth;

import static br.com.guiabolso.transacaoapi.modulos.comum.constantes.Constantes.VALOR_ABSOLUTO_TRANSACAO;
import static br.com.guiabolso.transacaoapi.modulos.comum.constantes.Constantes.VALOR_MINIMO_TRANSACAO_COM_CENTAVOS;
import static br.com.guiabolso.transacaoapi.modulos.comum.utilitarios.GeradorUtil.*;

@Data
@Slf4j
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRANSACAO")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "FK_USUARIO", nullable = false)
    public Usuario usuario;

    @Column(name = "DESCRICAO", length = 120, nullable = false)
    private String descricao;

    @Column(name = "DATA_TRANSACAO", nullable = false)
    private Long dataTransacao;

    @Column(name = "DATA_REFERENCIA", nullable = false)
    private YearMonth dataReferencia;

    @Column(name = "VALOR", nullable = false)
    private Integer valor;

    @Column(name = "DUPLICATED", nullable = false)
    private Boolean duplicated;

    public static Transacao gerarTransacao(Usuario usuario, YearMonth dataReferencia) {
        return Transacao
            .builder()
            .dataTransacao(gerarDataAleatoriaEntreDataDeReferencia(dataReferencia))
            .descricao(gerarStringAleatoriaLegivel())
            .valor(gerarValorTransacao())
            .dataReferencia(dataReferencia)
            .usuario(usuario)
            .duplicated(false)
            .build();
    }

    private static Integer gerarValorTransacao() {
        var valor = gerarNumeroAleatorioEntre(
            Math.negateExact(VALOR_ABSOLUTO_TRANSACAO), VALOR_ABSOLUTO_TRANSACAO
        ).toString();
        if (valor.length() < VALOR_MINIMO_TRANSACAO_COM_CENTAVOS) {
            StringUtils.rightPad(valor, VALOR_MINIMO_TRANSACAO_COM_CENTAVOS);
        }
        try {
            return Integer.parseInt(valor);
        } catch (Exception ex) {
            log.error("Erro ao tentar converter valor da transação.", ex);
            throw new ValidacaoException("Houve um erro ao tentar gerar um valor para a transação.");
        }
    }

    public void setarDuplicacaoParaNovaTransacao() {
        id = null;
        duplicated = true;
    }
}

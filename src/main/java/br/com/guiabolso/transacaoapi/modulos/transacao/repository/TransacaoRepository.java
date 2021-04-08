package br.com.guiabolso.transacaoapi.modulos.transacao.repository;

import br.com.guiabolso.transacaoapi.modulos.transacao.model.Transacao;
import br.com.guiabolso.transacaoapi.modulos.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface TransacaoRepository extends JpaRepository<Transacao, Integer> {

    Boolean existsByUsuarioAndDataReferencia(Usuario usuario, YearMonth dataReferencia);

    List<Transacao> findByUsuarioAndDataReferencia(Usuario usuario, YearMonth dataReferencia);

    List<Transacao> findByDescricaoAndDataReferenciaAndValor(String descricao, YearMonth dataReferencia, Integer valor);

    @Query(
        "SELECT DISTINCT t.dataReferencia "
            + "FROM Transacao t "
            + "WHERE t.dataReferencia in (:datasReferencia)"
    )
    List<YearMonth> findByDataReferenciaIn(List<YearMonth> datasReferencia);

    Optional<Transacao> findTop1ByDataReferencia(YearMonth dataReferencia);
}

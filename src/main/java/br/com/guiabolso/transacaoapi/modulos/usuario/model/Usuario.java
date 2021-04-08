package br.com.guiabolso.transacaoapi.modulos.usuario.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USUARIO")
public class Usuario {

    @Id
    private Integer id;

    @Column(name = "DATA_CADASTRO")
    private LocalDateTime dataCadastro;

    @PrePersist
    public void gerarDataCadastro() {
        dataCadastro = LocalDateTime.now();
    }

    public static Usuario converterDe(Integer usuarioId) {
        return Usuario
            .builder()
            .id(usuarioId)
            .build();
    }
}

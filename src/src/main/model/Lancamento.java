package org.conefisco.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ctb_lancamento")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipoLancamento;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataLancamento;
    @NotNull(message = "O campo Conta é obrigatório")
    private String planoConta;
    private Double valor;
    private String modoPagamento;
    private String tipoComprovante;
    private String numDoc;
    private String numCheque;
    private String obs;
    private String supCaixa;
    private Integer anoExercicio;
    private String fileUrl;

    private LocalDateTime created;
    private LocalDateTime updated;

    @PrePersist
    public void prePersist() {
        created = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updated = LocalDateTime.now();
    }



}
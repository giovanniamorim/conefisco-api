package org.conefisco.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;

@Entity(name = "ctb_plano_conta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanoContas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String nome;
    private String profundidade;
    private String tipoLancamento;

    @ManyToOne
    public  PlanoContas contaPai;
}
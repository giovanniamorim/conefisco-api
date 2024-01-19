package org.conefisco.model;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;


@Getter
@Setter
public class LancamentoAgrupado {

    private Long id;
    private String tipoLancamento;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataLancamento;
    private String planoConta;
    private Double valor;
    private String modoPagamento;
    private String tipoComprovante;
    private String numDoc;
    private String numCheque;
    private String obs;
    private String supCaixa;
    private Integer anoExercicio;
    private String mesAno;
    private Double valorTotal;

    public String getMesAno() {
        return mesAno;
    }
    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }
    public Double getValorTotal() {
        return valorTotal;
    }
    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

}
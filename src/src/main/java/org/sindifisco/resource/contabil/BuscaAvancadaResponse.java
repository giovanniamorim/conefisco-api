package org.conefisco.resource.contabil;

import lombok.Getter;
import lombok.Setter;
import org.conefisco.model.Lancamento;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BuscaAvancadaResponse {
    private Page<Lancamento> lancamentos;
    private Map<String, List<Lancamento>> lancamentosPorMes;
    private Double totalValor;

    public BuscaAvancadaResponse(Page<Lancamento> lancamentos, Double totalValor) {
        this.lancamentos = lancamentos;
        this.totalValor = totalValor;
    }


}


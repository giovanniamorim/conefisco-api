package org.conefisco.repository.filter;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class LancamentoFilter {

	private Long id;
	private String tipoLancamento;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataLancamentoDe;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataLancamentoAte;
	private String planoConta;
	private String modoPagamento;
	private String tipoComprovante;
	private String numDoc;
	private String numCheque;
	private String supCaixa;
	private Integer anoExercicio;
	private Double valorMin;
	private Double valorMax;


}

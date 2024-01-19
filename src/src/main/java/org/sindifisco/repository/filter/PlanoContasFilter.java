package org.conefisco.repository.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PlanoContasFilter {

	private String codigo;
	private String nome;
	private String profundidade;
	private String tipoLancamento;

}

package org.conefisco.repository.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RegimentoFilter {
	private Date dataAprovacao;
	private String descricao;
}

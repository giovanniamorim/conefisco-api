package org.conefisco.repository.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AssembleiaFilter {
	private Date data;
	private String tipo;
	private String assunto;
}

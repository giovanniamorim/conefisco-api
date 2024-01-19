package org.conefisco.repository.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
@Getter
@Setter
public class InventarioFilter {
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dataAquisicao;
	private String departamento;
	private String numero;
	private String descricao;
	private String estadoConservacao;
}

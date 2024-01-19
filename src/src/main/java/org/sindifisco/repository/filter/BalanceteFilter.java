package org.conefisco.repository.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class BalanceteFilter {
	private Long ano;
	private String mes;
}

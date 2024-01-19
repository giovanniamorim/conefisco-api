package org.conefisco.repository.contabil.planoContas;

import org.conefisco.model.PlanoContas;
import org.conefisco.repository.filter.PlanoContasFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlanoContasRepositoryQuery {

	public Page<PlanoContas> filtrar(PlanoContasFilter planoContasFilter, Pageable pageable);
}

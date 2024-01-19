package org.conefisco.repository.contabil.planejamento;

import org.conefisco.model.Planejamento;
import org.conefisco.repository.filter.PlanejamentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlanejamentoRepositoryQuery {

	public Page<Planejamento> filtrar(PlanejamentoFilter planejamentoFilter, Pageable pageable);
}

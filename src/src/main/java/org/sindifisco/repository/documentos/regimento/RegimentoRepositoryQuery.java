package org.conefisco.repository.documentos.regimento;

import org.conefisco.model.Regimento;
import org.conefisco.repository.filter.RegimentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RegimentoRepositoryQuery {

	public Page<Regimento> filtrar(RegimentoFilter balanceteFilter, Pageable pageable);
}

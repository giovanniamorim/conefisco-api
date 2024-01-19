package org.conefisco.repository.contabil.balanco;

import org.conefisco.model.Balanco;
import org.conefisco.repository.filter.BalancoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BalancoRepositoryQuery {

	public Page<Balanco> filtrar(BalancoFilter balancoFilter, Pageable pageable);
}

package org.conefisco.repository.contabil.balancete;

import org.conefisco.model.Balancete;
import org.conefisco.repository.filter.BalanceteFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BalanceteRepositoryQuery {

	public Page<Balancete> filtrar(BalanceteFilter balanceteFilter, Pageable pageable);
}

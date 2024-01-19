package org.conefisco.repository.contabil.execucao;

import org.conefisco.model.Execucao;
import org.conefisco.repository.filter.ExecucaoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExecucaoRepositoryQuery {

	public Page<Execucao> filtrar(ExecucaoFilter execucaoFilter, Pageable pageable);
}

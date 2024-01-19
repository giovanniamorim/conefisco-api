package org.conefisco.repository.contabil.assembleia;

import org.conefisco.model.Assembleia;
import org.conefisco.repository.filter.AssembleiaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssembleiaRepositoryQuery {

	public Page<Assembleia> filtrar(AssembleiaFilter assembleiaFilter, Pageable pageable);
}

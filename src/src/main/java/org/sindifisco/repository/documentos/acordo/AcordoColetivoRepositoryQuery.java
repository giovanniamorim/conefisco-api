package org.conefisco.repository.documentos.acordo;

import org.conefisco.model.AcordoColetivo;
import org.conefisco.repository.filter.AcordoColetivoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AcordoColetivoRepositoryQuery {

	public Page<AcordoColetivo> filtrar(AcordoColetivoFilter acordoFilter, Pageable pageable);
}

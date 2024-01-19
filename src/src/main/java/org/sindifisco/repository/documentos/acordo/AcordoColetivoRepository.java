package org.conefisco.repository.documentos.acordo;

import org.conefisco.model.AcordoColetivo;
import org.conefisco.model.Regimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcordoColetivoRepository extends JpaRepository<AcordoColetivo, Long>, AcordoColetivoRepositoryQuery {


}

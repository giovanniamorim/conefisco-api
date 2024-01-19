package org.conefisco.repository.documentos.regimento;

import org.conefisco.model.Regimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegimentoRepository extends JpaRepository<Regimento, Long>, RegimentoRepositoryQuery {


}

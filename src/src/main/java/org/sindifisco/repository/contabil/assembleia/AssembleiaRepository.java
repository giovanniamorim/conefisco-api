package org.conefisco.repository.contabil.assembleia;

import org.conefisco.model.Assembleia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssembleiaRepository extends JpaRepository<Assembleia, Long>, AssembleiaRepositoryQuery {


}

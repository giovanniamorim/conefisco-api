package org.conefisco.repository.contabil.balanco;

import org.conefisco.model.Balanco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalancoRepository extends JpaRepository<Balanco, Long>, BalancoRepositoryQuery {


}

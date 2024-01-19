package org.conefisco.repository.contabil.balancete;

import org.conefisco.model.Balancete;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceteRepository extends JpaRepository<Balancete, Long>, BalanceteRepositoryQuery {


}

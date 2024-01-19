package org.conefisco.repository.contabil.planejamento;

import org.conefisco.model.Planejamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanejamentoRepository extends JpaRepository<Planejamento, Long>, PlanejamentoRepositoryQuery {


}

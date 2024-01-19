package org.conefisco.repository.contabil.execucao;

import org.conefisco.model.Execucao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecucaoRepository extends JpaRepository<Execucao, Long>, ExecucaoRepositoryQuery {


}

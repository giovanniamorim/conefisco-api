package org.conefisco.repository.lancamento;

import org.conefisco.model.Lancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

    Page<Lancamento> findByTipoLancamento(String tipoLancamento, Pageable pageable);

}

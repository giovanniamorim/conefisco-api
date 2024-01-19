package org.conefisco.repository.lancamento;

import org.conefisco.model.Lancamento;
import org.conefisco.repository.filter.LancamentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface LancamentoRepositoryQuery {

    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
    public List<Lancamento> filtrarRelatorio(LancamentoFilter lancamentoFilter);

    @Query("SELECT COALESCE(SUM(l.valor), 0) FROM Lancamento l " +
            "WHERE (:#{#filter.tipoLancamento} IS NULL OR l.tipoLancamento = :#{#filter.tipoLancamento}) " +
            "AND (:#{#filter.dataLancamentoDe} IS NULL OR l.dataLancamento >= :#{#filter.dataLancamentoDe}) " +
            "AND (:#{#filter.dataLancamentoAte} IS NULL OR l.dataLancamento <= :#{#filter.dataLancamentoAte}) " +
            "AND (:#{#filter.planoConta} IS NULL OR l.planoConta = :#{#filter.planoConta}) " +
            "AND (:#{#filter.modoPagamento} IS NULL OR l.modoPagamento = :#{#filter.modoPagamento}) " +
            "AND (:#{#filter.tipoComprovante} IS NULL OR l.tipoComprovante = :#{#filter.tipoComprovante}) " +
            "AND (:#{#filter.numDoc} IS NULL OR l.numDoc LIKE %:#{#filter.numDoc}%) " +
            "AND (:#{#filter.numCheque} IS NULL OR l.numCheque LIKE %:#{#filter.numCheque}%) " +
            "AND (:#{#filter.supCaixa} IS NULL OR l.supCaixa = :#{#filter.supCaixa}) " +
            "AND (:#{#filter.anoExercicio} IS NULL OR l.anoExercicio = :#{#filter.anoExercicio}) " +
            "AND (:#{#filter.valorMin} IS NULL OR l.valor >= :#{#filter.valorMin}) " +
            "AND (:#{#filter.valorMax} IS NULL OR l.valor <= :#{#filter.valorMax})")
    Double sumValorByFilter(@Param("filter") LancamentoFilter filter);


}

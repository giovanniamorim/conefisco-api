package org.conefisco.repository.lancamento;

import org.conefisco.model.Lancamento;
import org.conefisco.repository.filter.LancamentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    private static Logger LOGGER = Logger.getLogger("InfoLogging");

    @Override
    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {

        LOGGER.info("Entrou no filtrarDespesas: " + lancamentoFilter);
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Lancamento> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        LOGGER.info("Resultado de filtrarDespesas: " + query.getResultList());

        Double totalValor = sumValorByFilter(lancamentoFilter);

        return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));

    }

    @Override
    public List<Lancamento> filtrarRelatorio(LancamentoFilter lancamentoFilter) {
        LOGGER.info("Entrou no filtrarDespesas: " + lancamentoFilter);
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Lancamento> query = manager.createQuery(criteria);

        LOGGER.info("Resultado de filtrarDespesas: " + query.getResultList());


        return query.getResultList();
    }

    @Override
    public Double sumValorByFilter(LancamentoFilter lancamentoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Double> criteria = builder.createQuery(Double.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.sum(root.get("valor")));

        TypedQuery<Double> query = manager.createQuery(criteria);
        return query.getSingleResult();
    }


    private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
                                        Root<Lancamento> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(lancamentoFilter.getId())) {
            LOGGER.info("Entrou no if ID: " + lancamentoFilter.getId());
            predicates.add(builder.equal(
                    builder.lower(root.get("id")), lancamentoFilter.getId() ));
        }

        if (!StringUtils.isEmpty(lancamentoFilter.getTipoLancamento())) {
            LOGGER.info("Entrou no if TipoLancamento: " + lancamentoFilter.getTipoLancamento());
            predicates.add(builder.equal(
                    builder.lower(root.get("tipoLancamento")), lancamentoFilter.getTipoLancamento()));
        }

        if (lancamentoFilter.getDataLancamentoDe() != null) {
            LOGGER.info("Entrou no if getDataLancamentoDe: " + lancamentoFilter.getDataLancamentoDe());
            predicates.add(
                    builder.greaterThanOrEqualTo(
                            root.get("dataLancamento"), lancamentoFilter.getDataLancamentoDe()));
        }

        if (lancamentoFilter.getDataLancamentoAte() != null) {
            LOGGER.info("Entrou no if getDataLancamentoAte: " + lancamentoFilter.getDataLancamentoAte());
            predicates.add(
                    builder.lessThanOrEqualTo(
                            root.get("dataLancamento"), lancamentoFilter.getDataLancamentoAte()));
        }

        if (!StringUtils.isEmpty(lancamentoFilter.getPlanoConta())) {
            LOGGER.info("Entrou no if getPlanoConta: " + lancamentoFilter.getPlanoConta());
            predicates.add(
                    builder.equal(
                            root.get("planoConta"), lancamentoFilter.getPlanoConta()));
        }

        if (!StringUtils.isEmpty(lancamentoFilter.getModoPagamento())) {
            LOGGER.info("Entrou no if getModoPagamento: " + lancamentoFilter.getModoPagamento());
            predicates.add(
                    builder.equal(
                            root.get("modoPagamento"), lancamentoFilter.getModoPagamento()));
        }

        if (!StringUtils.isEmpty(lancamentoFilter.getTipoComprovante())) {
            LOGGER.info("Entrou no if tipoComprovante: " + lancamentoFilter.getTipoComprovante());
            predicates.add(
                    builder.equal(
                            root.get("tipoComprovante"), lancamentoFilter.getTipoComprovante()));
        }

        if (!StringUtils.isEmpty(lancamentoFilter.getNumDoc())) {
            LOGGER.info("Entrou no if numDoc: " + lancamentoFilter.getNumDoc());
            predicates.add(builder.like(
                    builder.lower(
                            root.get("numDoc")), "%" + lancamentoFilter.getNumDoc().toLowerCase() + "%"));
        }

        if (!StringUtils.isEmpty(lancamentoFilter.getNumCheque())) {
            LOGGER.info("Entrou no if numCheque: " + lancamentoFilter.getNumCheque());
            predicates.add(builder.like(
                    builder.lower(
                            root.get("numCheque")), "%" + lancamentoFilter.getNumCheque().toLowerCase() + "%"));
        }

        if (!StringUtils.isEmpty(lancamentoFilter.getSupCaixa())) {
            LOGGER.info("Entrou no if supCaixa: " + lancamentoFilter.getSupCaixa());
            predicates.add(
                    builder.equal(
                            root.get("supCaixa"), lancamentoFilter.getSupCaixa()));
        }

        if (lancamentoFilter.getAnoExercicio() != null) {
            LOGGER.info("Entrou no if anoExercicio: " + lancamentoFilter.getAnoExercicio());
            predicates.add(
                    builder.equal(
                            root.get("anoExercicio"), lancamentoFilter.getAnoExercicio()));
        }

        if (lancamentoFilter.getValorMin() != null) {
            LOGGER.info("Entrou no if getValorMin: " + lancamentoFilter.getValorMin());
            predicates.add(
                    builder.greaterThanOrEqualTo(
                            root.get("valor"), lancamentoFilter.getValorMin()));
        }

        if (lancamentoFilter.getValorMax() != null) {
            LOGGER.info("Entrou no if getDataLancamentoAte: " + lancamentoFilter.getValorMax());
            predicates.add(
                    builder.lessThanOrEqualTo(
                            root.get("valor"), lancamentoFilter.getValorMax()));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(
            TypedQuery<?> query, Pageable pageable
        ) {
        LOGGER.info("Entrou no adicionarRestricoesDePaginacao: " + query);
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long total(LancamentoFilter lancamentoFilter) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        LOGGER.info("Entrou no Predicate final: " + predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}

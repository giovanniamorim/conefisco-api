package org.conefisco.repository.contabil.planejamento;

import org.conefisco.model.Planejamento;
import org.conefisco.repository.filter.PlanejamentoFilter;
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

public class PlanejamentoRepositoryImpl implements PlanejamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;


	@Override
	public Page<Planejamento> filtrar(PlanejamentoFilter planejamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Planejamento> criteria = builder.createQuery(Planejamento.class);
		
		Root<Planejamento> root = criteria.from(Planejamento.class);
		
		//cria as restrições
		Predicate[] predicates = criarRestricoes(planejamentoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Planejamento> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(planejamentoFilter));
	}


	@SuppressWarnings("deprecation")
	private Predicate[] criarRestricoes(PlanejamentoFilter planejamentoFilter, CriteriaBuilder builder, Root<Planejamento> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(planejamentoFilter.getAno())) {
			predicates.add(builder.equal(builder.lower(root.get("ano")), planejamentoFilter.getAno()));
		}
		
		if (!StringUtils.isEmpty(planejamentoFilter.getDescricao())) {
			predicates.add(builder.equal(builder.lower(root.get("mes")), planejamentoFilter.getDescricao()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}

	private Long total(PlanejamentoFilter planejamentoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Planejamento> roor = criteria.from(Planejamento.class);
		
		Predicate[] predicates = criarRestricoes(planejamentoFilter, builder, roor);
		criteria.where(predicates);
		
		criteria.select(builder.count(roor));
		
		return manager.createQuery(criteria).getSingleResult();
	}


}

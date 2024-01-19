package org.conefisco.repository.contabil.balanco;

import org.conefisco.model.Balanco;
import org.conefisco.repository.filter.BalancoFilter;
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

public class BalancoRepositoryImpl implements BalancoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;


	@Override
	public Page<Balanco> filtrar(BalancoFilter BalancoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Balanco> criteria = builder.createQuery(Balanco.class);
		
		Root<Balanco> root = criteria.from(Balanco.class);
		
		//cria as restrições
		Predicate[] predicates = criarRestricoes(BalancoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Balanco> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(BalancoFilter));
	}


	@SuppressWarnings("deprecation")
	private Predicate[] criarRestricoes(BalancoFilter BalancoFilter, CriteriaBuilder builder, Root<Balanco> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(BalancoFilter.getAno())) {
			predicates.add(builder.equal(builder.lower(root.get("ano")), BalancoFilter.getAno()));
		}
		
		if (!StringUtils.isEmpty(BalancoFilter.getMes())) {
			predicates.add(builder.equal(builder.lower(root.get("mes")), BalancoFilter.getMes()));
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

	private Long total(BalancoFilter BalancoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Balanco> roor = criteria.from(Balanco.class);
		
		Predicate[] predicates = criarRestricoes(BalancoFilter, builder, roor);
		criteria.where(predicates);
		
		criteria.select(builder.count(roor));
		
		return manager.createQuery(criteria).getSingleResult();
	}


}

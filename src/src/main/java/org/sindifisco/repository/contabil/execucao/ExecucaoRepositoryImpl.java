package org.conefisco.repository.contabil.execucao;

import org.conefisco.model.Execucao;
import org.conefisco.repository.filter.ExecucaoFilter;
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

public class ExecucaoRepositoryImpl implements ExecucaoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;


	@Override
	public Page<Execucao> filtrar(ExecucaoFilter execucaoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Execucao> criteria = builder.createQuery(Execucao.class);
		
		Root<Execucao> root = criteria.from(Execucao.class);
		
		//cria as restrições
		Predicate[] predicates = criarRestricoes(execucaoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Execucao> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(execucaoFilter));
	}


	@SuppressWarnings("deprecation")
	private Predicate[] criarRestricoes(ExecucaoFilter execucaoFilter, CriteriaBuilder builder, Root<Execucao> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(execucaoFilter.getAno())) {
			predicates.add(builder.equal(builder.lower(root.get("ano")), execucaoFilter.getAno()));
		}
		
		if (!StringUtils.isEmpty(execucaoFilter.getDescricao())) {
			predicates.add(builder.equal(builder.lower(root.get("mes")), execucaoFilter.getDescricao()));
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

	private Long total(ExecucaoFilter execucaoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Execucao> roor = criteria.from(Execucao.class);
		
		Predicate[] predicates = criarRestricoes(execucaoFilter, builder, roor);
		criteria.where(predicates);
		
		criteria.select(builder.count(roor));
		
		return manager.createQuery(criteria).getSingleResult();
	}


}

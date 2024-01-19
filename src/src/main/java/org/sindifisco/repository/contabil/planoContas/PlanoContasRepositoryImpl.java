package org.conefisco.repository.contabil.planoContas;

import org.conefisco.model.PlanoContas;
import org.conefisco.repository.filter.PlanoContasFilter;
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

public class PlanoContasRepositoryImpl implements PlanoContasRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;


	@Override
	public Page<PlanoContas> filtrar(PlanoContasFilter planoContasFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<PlanoContas> criteria = builder.createQuery(PlanoContas.class);
		
		Root<PlanoContas> root = criteria.from(PlanoContas.class);
		
		//cria as restrições
		Predicate[] predicates = criarRestricoes(planoContasFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<PlanoContas> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(planoContasFilter));
	}


	@SuppressWarnings("deprecation")
	private Predicate[] criarRestricoes(PlanoContasFilter planoContasFilter, CriteriaBuilder builder, Root<PlanoContas> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(planoContasFilter.getCodigo())) {
			predicates.add(builder.equal(builder.lower(root.get("codigo")), planoContasFilter.getCodigo()));
		}
		
		if (!StringUtils.isEmpty(planoContasFilter.getNome())) {
			predicates.add(builder.equal(builder.lower(root.get("nome")), planoContasFilter.getNome()));
		}

		if (!StringUtils.isEmpty(planoContasFilter.getProfundidade())) {
			predicates.add(builder.equal(builder.lower(root.get("profundidade")), planoContasFilter.getProfundidade()));
		}

		if (!StringUtils.isEmpty(planoContasFilter.getTipoLancamento())) {
			predicates.add(builder.equal(builder.lower(root.get("tipoLancamento")), planoContasFilter.getTipoLancamento()));
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

	private Long total(PlanoContasFilter planoContasFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<PlanoContas> roor = criteria.from(PlanoContas.class);
		
		Predicate[] predicates = criarRestricoes(planoContasFilter, builder, roor);
		criteria.where(predicates);
		
		criteria.select(builder.count(roor));
		
		return manager.createQuery(criteria).getSingleResult();
	}


}

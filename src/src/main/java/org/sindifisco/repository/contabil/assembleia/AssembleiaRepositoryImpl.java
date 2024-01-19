package org.conefisco.repository.contabil.assembleia;

import org.conefisco.model.Assembleia;
import org.conefisco.repository.filter.AssembleiaFilter;
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

public class AssembleiaRepositoryImpl implements AssembleiaRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;


	@Override
	public Page<Assembleia> filtrar(AssembleiaFilter AssembleiaFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Assembleia> criteria = builder.createQuery(Assembleia.class);
		
		Root<Assembleia> root = criteria.from(Assembleia.class);
		
		//cria as restrições
		Predicate[] predicates = criarRestricoes(AssembleiaFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Assembleia> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(AssembleiaFilter));
	}


	@SuppressWarnings("deprecation")
	private Predicate[] criarRestricoes(AssembleiaFilter assembleiaFilter, CriteriaBuilder builder, Root<Assembleia> root) {
		
		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(assembleiaFilter.getData())) {
			predicates.add(builder.equal(builder.lower(root.get("data")), assembleiaFilter.getData()));
		}
		if (!StringUtils.isEmpty(assembleiaFilter.getTipo())) {
			predicates.add(builder.equal(builder.lower(root.get("tipo")), assembleiaFilter.getTipo()));
		}
		if (!StringUtils.isEmpty(assembleiaFilter.getAssunto())) {
			predicates.add(builder.equal(builder.lower(root.get("assunto")), assembleiaFilter.getAssunto()));
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

	private Long total(AssembleiaFilter AssembleiaFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Assembleia> roor = criteria.from(Assembleia.class);
		
		Predicate[] predicates = criarRestricoes(AssembleiaFilter, builder, roor);
		criteria.where(predicates);
		
		criteria.select(builder.count(roor));
		
		return manager.createQuery(criteria).getSingleResult();
	}


}

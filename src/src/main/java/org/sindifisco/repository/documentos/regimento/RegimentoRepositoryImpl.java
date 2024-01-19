package org.conefisco.repository.documentos.regimento;

import org.conefisco.model.Regimento;
import org.conefisco.repository.filter.RegimentoFilter;
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

public class RegimentoRepositoryImpl implements RegimentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;


	@Override
	public Page<Regimento> filtrar(RegimentoFilter regimentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Regimento> criteria = builder.createQuery(Regimento.class);
		
		Root<Regimento> root = criteria.from(Regimento.class);
		
		//cria as restrições
		Predicate[] predicates = criarRestricoes(regimentoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Regimento> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(regimentoFilter));
	}


	@SuppressWarnings("deprecation")
	private Predicate[] criarRestricoes(RegimentoFilter regimentoFilter, CriteriaBuilder builder, Root<Regimento> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(regimentoFilter.getDataAprovacao())) {
			predicates.add(builder.equal(builder.lower(root.get("dataAprovacao")), regimentoFilter.getDataAprovacao()));
		}
		
		if (!StringUtils.isEmpty(regimentoFilter.getDescricao())) {
			predicates.add(builder.equal(builder.lower(root.get("descricao")), regimentoFilter.getDescricao()));
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

	private Long total(RegimentoFilter RegimentoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Regimento> roor = criteria.from(Regimento.class);
		
		Predicate[] predicates = criarRestricoes(RegimentoFilter, builder, roor);
		criteria.where(predicates);
		
		criteria.select(builder.count(roor));
		
		return manager.createQuery(criteria).getSingleResult();
	}


}

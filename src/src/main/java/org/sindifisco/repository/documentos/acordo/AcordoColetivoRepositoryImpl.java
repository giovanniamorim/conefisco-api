package org.conefisco.repository.documentos.acordo;

import org.conefisco.model.AcordoColetivo;
import org.conefisco.repository.filter.AcordoColetivoFilter;
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

public class AcordoColetivoRepositoryImpl implements AcordoColetivoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;


	@Override
	public Page<AcordoColetivo> filtrar(AcordoColetivoFilter acordoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<AcordoColetivo> criteria = builder.createQuery(AcordoColetivo.class);
		
		Root<AcordoColetivo> root = criteria.from(AcordoColetivo.class);
		
		//cria as restrições
		Predicate[] predicates = criarRestricoes(acordoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<AcordoColetivo> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(acordoFilter));
	}


	@SuppressWarnings("deprecation")
	private Predicate[] criarRestricoes(AcordoColetivoFilter acordoFilter, CriteriaBuilder builder, Root<AcordoColetivo> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(acordoFilter.getDataAprovacao())) {
			predicates.add(builder.equal(builder.lower(root.get("dataAprovacao")), acordoFilter.getDataAprovacao()));
		}
		
		if (!StringUtils.isEmpty(acordoFilter.getDescricao())) {
			predicates.add(builder.equal(builder.lower(root.get("descricao")), acordoFilter.getDescricao()));
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

	private Long total(AcordoColetivoFilter acordoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<AcordoColetivo> roor = criteria.from(AcordoColetivo.class);
		
		Predicate[] predicates = criarRestricoes(acordoFilter, builder, roor);
		criteria.where(predicates);
		
		criteria.select(builder.count(roor));
		
		return manager.createQuery(criteria).getSingleResult();
	}


}

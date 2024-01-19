package org.conefisco.repository.contabil.inventario;

import org.conefisco.model.Inventario;
import org.conefisco.repository.filter.InventarioFilter;
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

public class InventarioRepositoryImpl implements InventarioRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;


	@Override
	public Page<Inventario> filtrar(InventarioFilter inventarioFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Inventario> criteria = builder.createQuery(Inventario.class);
		
		Root<Inventario> root = criteria.from(Inventario.class);
		
		//cria as restrições
		Predicate[] predicates = criarRestricoes(inventarioFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Inventario> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(inventarioFilter));
	}


	@SuppressWarnings("deprecation")
	private Predicate[] criarRestricoes(InventarioFilter inventarioFilter, CriteriaBuilder builder, Root<Inventario> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(inventarioFilter.getDataAquisicao())) {
			predicates.add(builder.equal(builder.lower(root.get("dataAquisicao")), inventarioFilter.getDataAquisicao()));
		}
		
		if (!StringUtils.isEmpty(inventarioFilter.getDepartamento())) {
			predicates.add(builder.equal(builder.lower(root.get("departamento")), inventarioFilter.getDepartamento()));
		}

		if (!StringUtils.isEmpty(inventarioFilter.getNumero())) {
			predicates.add(builder.equal(builder.lower(root.get("numero")), inventarioFilter.getNumero()));
		}

		if (!StringUtils.isEmpty(inventarioFilter.getEstadoConservacao())) {
			predicates.add(builder.equal(builder.lower(root.get("estadoConservacao")), inventarioFilter.getEstadoConservacao()));
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

	private Long total(InventarioFilter inventarioFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Inventario> roor = criteria.from(Inventario.class);
		
		Predicate[] predicates = criarRestricoes(inventarioFilter, builder, roor);
		criteria.where(predicates);
		
		criteria.select(builder.count(roor));
		
		return manager.createQuery(criteria).getSingleResult();
	}


}

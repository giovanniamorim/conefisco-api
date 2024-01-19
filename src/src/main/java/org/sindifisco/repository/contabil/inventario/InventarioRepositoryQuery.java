package org.conefisco.repository.contabil.inventario;

import org.conefisco.model.Inventario;
import org.conefisco.repository.filter.InventarioFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventarioRepositoryQuery {

	public Page<Inventario> filtrar(InventarioFilter inventarioFilter, Pageable pageable);
}

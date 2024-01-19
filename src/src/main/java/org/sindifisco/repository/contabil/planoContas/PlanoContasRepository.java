package org.conefisco.repository.contabil.planoContas;

import org.conefisco.model.PlanoContas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanoContasRepository extends JpaRepository<PlanoContas, Long> {

    PlanoContas findByCodigo(String codigo);
    Boolean existsByCodigo(String codigo);
    Boolean existsByNome(String nome);


}

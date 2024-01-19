package org.conefisco.repository.juridico.processo;
import org.conefisco.model.Processo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessoRepository extends JpaRepository<Processo, Long> {
    Processo findByNumero(String numero);
}

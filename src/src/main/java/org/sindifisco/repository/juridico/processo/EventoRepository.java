package org.conefisco.repository.juridico.processo;
import org.conefisco.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    Evento findByNome(String nome);

}

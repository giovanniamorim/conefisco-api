package org.conefisco.resource.juridico;


import org.conefisco.model.Processo;
import org.conefisco.repository.juridico.processo.ProcessoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static java.lang.Void.TYPE;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/juridico/processos")
@Slf4j
public class ProcessoResource {
    @Autowired
    ProcessoRepository processoRepository;

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('ROLE_CREATE') and #oauth2.hasScope('write')")
    public Processo addProcesso(@RequestBody @Valid Processo processo)  {
        return processoRepository.save(processo);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Processo> listAllBalances(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        return processoRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Processo findProcessoById(@PathVariable Long id) {
        return processoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Processo não encontrado"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_DELETE') and #oauth2.hasScope('write')")
    public void deleteProcesso(@PathVariable Long id) {
        processoRepository.findById(id).map(user -> {
            processoRepository.delete(user);
            return TYPE;
        }).orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND, "Processo não encontrado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE') and #oauth2.hasScope('write')")
    public ResponseEntity<Processo> editProcesso(@PathVariable Long id, @Valid @RequestBody Processo newProcesso){
        return processoRepository.findById(id)
                .map(processo -> {
                    processo.setNumero(newProcesso.getNumero());
                    processo.setExequente(newProcesso.getExequente());
                    processo.setExecutado(newProcesso.getExecutado());
                    processo.setJuizo(newProcesso.getJuizo());
                    processo.setJuiz(newProcesso.getJuiz());
                    processo.setAssunto(newProcesso.getAssunto());
                    processo.setEventos(newProcesso.getEventos());
                    Processo processoUpdated = processoRepository.save(processo);
                    return ResponseEntity.ok().body(processoUpdated);
                }).orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Processo não encontrado"));
    }

}
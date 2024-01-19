package org.conefisco.resource.contabil;

import org.conefisco.model.Planejamento;
import org.conefisco.model.PlanoContas;
import org.conefisco.repository.contabil.planoContas.PlanoContasRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static java.lang.Void.TYPE;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/planocontas")
@Slf4j
public class PlanoContasResource {
    @Autowired
    PlanoContasRepository planoContasRepository;

    @PostMapping()
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('ROLE_CREATE') and #oauth2.hasScope('write')")
    public PlanoContas addConta(@RequestBody @Valid PlanoContas conta) {
        if ((planoContasRepository.existsByCodigo(conta.getCodigo())) ||
                (planoContasRepository.existsByCodigo(conta.getNome()))) {
            throw new ResponseStatusException(BAD_REQUEST, "Já existe uma Conta com o nome ou código sugerido");
        }
        return planoContasRepository.save(conta);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<PlanoContas> findAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable){
        return planoContasRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public PlanoContas findById(@PathVariable Long id) {
        return planoContasRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Conta não encontrado"));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_DELETE') and #oauth2.hasScope('write')")
    public void deleteConta(@PathVariable Long id) {
        planoContasRepository.findById(id).map(conta -> {
            planoContasRepository.delete(conta);
            return TYPE;
        }).orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND, "Conta não encontrado"));
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_UPDATE') and #oauth2.hasScope('write')")
    public void updateConta(@PathVariable Long id, @RequestBody PlanoContas updatedConta) {
        planoContasRepository.findById(id).map(conta -> {
            conta.setCodigo(updatedConta.getCodigo());
            conta.setNome(updatedConta.getNome());
            conta.setProfundidade(updatedConta.getProfundidade());
            conta.setTipoLancamento(updatedConta.getTipoLancamento());
            conta.setContaPai(updatedConta.getContaPai());
            return planoContasRepository.save(updatedConta);
        }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Conta não encontrada"));
    }

}


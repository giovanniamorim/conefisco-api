package org.conefisco.resource.contabil;


import org.conefisco.model.Balancete;
import org.conefisco.model.Planejamento;
import org.conefisco.repository.contabil.planejamento.PlanejamentoRepository;
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
@RequestMapping("/api/planejamentos")
@Slf4j
public class PlanejamentoResource {
    @Autowired
    PlanejamentoRepository planejamentoRepository;

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('ROLE_CREATE') and #oauth2.hasScope('write')")
    public Planejamento addPlanejamento(@RequestBody @Valid Planejamento planejamento)  {
        return planejamentoRepository.save(planejamento);
    }

    @GetMapping("/todos")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Planejamento> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable){
        return planejamentoRepository.findAll(pageable);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Planejamento> listAll(
            @PageableDefault(page = 0, size = 5, sort = "ano", direction = Sort.Direction.DESC) Pageable pageable) {
        return planejamentoRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Planejamento findPlanejamentoById(@PathVariable Long id) {
        return planejamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Planejamento não encontrado"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_DELETE') and #oauth2.hasScope('write')")
    public void deletePlanejamento(@PathVariable Long id) {
        planejamentoRepository.findById(id).map(user -> {
            planejamentoRepository.delete(user);
            return TYPE;
        }).orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND, "Planejamento não encontrado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE') and #oauth2.hasScope('write')")
    public ResponseEntity<Planejamento> editPlanejamento(@PathVariable Long id, @Valid @RequestBody Planejamento newPlanejamento){
               return planejamentoRepository.findById(id)
                .map(planejamento -> {
                    planejamento.setAno(newPlanejamento.getAno());
                    planejamento.setDescricao(newPlanejamento.getDescricao());
                    planejamento.setFileUrl((newPlanejamento.getFileUrl()));
                    Planejamento planejamentoUpdated = planejamentoRepository.save(planejamento);
                    return ResponseEntity.ok().body(planejamentoUpdated);
                }).orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Planejamento não encontrado"));
    }

}

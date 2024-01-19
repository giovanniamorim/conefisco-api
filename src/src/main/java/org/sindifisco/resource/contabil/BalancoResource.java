package org.conefisco.resource.contabil;


import org.conefisco.model.Assembleia;
import org.conefisco.model.Balanco;
import org.conefisco.repository.contabil.balanco.BalancoRepository;
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
@RequestMapping("/api/balancos")
@Slf4j
public class BalancoResource {
    @Autowired
    BalancoRepository balancoRepository;

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('ROLE_CREATE') and #oauth2.hasScope('write')")
    public Balanco addBalanco(@RequestBody @Valid Balanco balanco)  {
        return balancoRepository.save(balanco);
    }

    @GetMapping("/todos")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Balanco> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable){
        return balancoRepository.findAll(pageable);
    }


    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Balanco> listAll(
            @PageableDefault(page = 0, size = 5, sort = "ano", direction = Sort.Direction.DESC) Pageable pageable) {
        return balancoRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Balanco findBalancoById(@PathVariable Long id) {
        return balancoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Balanco não encontrado"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_DELETE') and #oauth2.hasScope('write')")
    public void deleteBalanco(@PathVariable Long id) {
        balancoRepository.findById(id).map(balanco -> {
            balancoRepository.delete(balanco);
            return TYPE;
        }).orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND, "Balanco não encontrado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE') and #oauth2.hasScope('write')")
    public ResponseEntity<Balanco> editBalanco(@PathVariable Long id, @Valid @RequestBody Balanco newBalanco){
               return balancoRepository.findById(id)
                .map(balanco -> {
                    balanco.setAno(newBalanco.getAno());
                    balanco.setMes(newBalanco.getMes());
                    balanco.setDescricao((newBalanco.getDescricao()));
                    balanco.setFileUrl((newBalanco.getFileUrl()));
                    Balanco balancoUpdated = balancoRepository.save(balanco);
                    return ResponseEntity.ok().body(balancoUpdated);
                }).orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Balanco não encontrado"));
    }

}

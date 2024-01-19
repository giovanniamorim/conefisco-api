package org.conefisco.resource.contabil;


import org.conefisco.model.Assembleia;
import org.conefisco.model.Balancete;
import org.conefisco.repository.contabil.assembleia.AssembleiaRepository;
import org.conefisco.repository.filter.AssembleiaFilter;
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
@RequestMapping("/api/assembleias")
@Slf4j
public class AssembleiaResource {
    @Autowired
    AssembleiaRepository assembleiaRepository;


    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_CREATE') and #oauth2.hasScope('write')")
    public Assembleia addInventari(@RequestBody @Valid Assembleia assembleia)  {
        return assembleiaRepository.save(assembleia);
    }

    @GetMapping("/todas")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Assembleia> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable){
        return assembleiaRepository.findAll(pageable);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Assembleia> pesquisar(
            AssembleiaFilter assembleiaFilter,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return assembleiaRepository.filtrar(assembleiaFilter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Assembleia findAssembleiaById(@PathVariable Long id) {
        return assembleiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Assembleia não encontrada"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_DELETE') and #oauth2.hasScope('write')")
    public void deleteAssembleia(@PathVariable Long id) {
        assembleiaRepository.findById(id).map(assembleia -> {
            assembleiaRepository.delete(assembleia);
            return TYPE;
        }).orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND, "Assembleia não encontrada"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE') and #oauth2.hasScope('write')")
    public ResponseEntity<Assembleia> editAssembleia(@PathVariable Long id, @Valid @RequestBody Assembleia newAssembleia){
        return assembleiaRepository.findById(id)
                .map(assembleia -> {
                    assembleia.setData(newAssembleia.getData());
                    assembleia.setTipo((newAssembleia.getTipo()));
                    assembleia.setAssunto(newAssembleia.getAssunto());
                    assembleia.setComentario(newAssembleia.getComentario());
                    assembleia.setFileUrl((newAssembleia.getFileUrl()));
                    Assembleia assembleiaUpdated = assembleiaRepository.save(assembleia);
                    return ResponseEntity.ok().body(assembleiaUpdated);
                }).orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Assembleia não encontrada"));
    }


}

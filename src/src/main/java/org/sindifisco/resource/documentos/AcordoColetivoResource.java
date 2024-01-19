package org.conefisco.resource.documentos;


import lombok.extern.slf4j.Slf4j;
import org.conefisco.model.AcordoColetivo;
import org.conefisco.repository.documentos.acordo.AcordoColetivoRepository;
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
@RequestMapping("/api/acordos")
@Slf4j
public class AcordoColetivoResource {
    @Autowired
    AcordoColetivoRepository acordoRepository;


    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('ROLE_CREATE') and #oauth2.hasScope('write')")
    public AcordoColetivo addAcordoColetivo(@RequestBody @Valid AcordoColetivo acordo)  {
        return acordoRepository.save(acordo);
    }

    @GetMapping("/todos")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<AcordoColetivo> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return acordoRepository.findAll(pageable);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<AcordoColetivo> pesquisar(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC)
                                         Pageable pageable) {
        return acordoRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public AcordoColetivo findAcordoColetivoById(@PathVariable Long id) {
        return acordoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Acordo Coletivo não encontrado"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_DELETE') and #oauth2.hasScope('write')")
    public void deleteAcordoColetivo(@PathVariable Long id) {
        acordoRepository.findById(id).map(user -> {
            acordoRepository.delete(user);
            return TYPE;
        }).orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND, "Acordo Coletivo não encontrado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE') and #oauth2.hasScope('write')")
    public ResponseEntity<AcordoColetivo> editAcordoColetivo(@PathVariable Long id, @Valid @RequestBody AcordoColetivo newAcordoColetivo){
               return acordoRepository.findById(id)
                .map(acordo -> {
                    acordo.setDataAprovacao(newAcordoColetivo.getDataAprovacao());
                    acordo.setDescricao(newAcordoColetivo.getDescricao());
                    acordo.setFileUrl((newAcordoColetivo.getFileUrl()));
                    AcordoColetivo acordoUpdated = acordoRepository.save(acordo);
                    return ResponseEntity.ok().body(acordoUpdated);
                }).orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Acordo Coletivo não encontrado"));
    }


}

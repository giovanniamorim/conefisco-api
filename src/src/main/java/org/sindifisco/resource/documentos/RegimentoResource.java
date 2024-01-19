package org.conefisco.resource.documentos;


import lombok.extern.slf4j.Slf4j;
import org.conefisco.model.Regimento;
import org.conefisco.repository.documentos.regimento.RegimentoRepository;
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
@RequestMapping("/api/regimentos")
@Slf4j
public class RegimentoResource {
    @Autowired
    RegimentoRepository regimentoRepository;


    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('ROLE_CREATE') and #oauth2.hasScope('write')")
    public Regimento addRegimento(@RequestBody @Valid Regimento regimento)  {
        return regimentoRepository.save(regimento);
    }

    @GetMapping("/todos")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Regimento> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return regimentoRepository.findAll(pageable);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Regimento> pesquisar(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC)
                                         Pageable pageable) {
        return regimentoRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Regimento findRegimentoById(@PathVariable Long id) {
        return regimentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Regimento não encontrado"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_DELETE') and #oauth2.hasScope('write')")
    public void deleteRegimento(@PathVariable Long id) {
        regimentoRepository.findById(id).map(user -> {
            regimentoRepository.delete(user);
            return TYPE;
        }).orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND, "Regimento não encontrado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE') and #oauth2.hasScope('write')")
    public ResponseEntity<Regimento> editRegimento(@PathVariable Long id, @Valid @RequestBody Regimento newRegimento){
               return regimentoRepository.findById(id)
                .map(regimento -> {
                    regimento.setDataAprovacao(newRegimento.getDataAprovacao());
                    regimento.setDescricao(newRegimento.getDescricao());
                    regimento.setFileUrl((newRegimento.getFileUrl()));
                    Regimento regimentoUpdated = regimentoRepository.save(regimento);
                    return ResponseEntity.ok().body(regimentoUpdated);
                }).orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Regimento não encontrado"));
    }


}

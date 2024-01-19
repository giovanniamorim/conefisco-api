package org.conefisco.resource.contrato;

import lombok.extern.slf4j.Slf4j;
import org.conefisco.model.Contrato;
import org.conefisco.repository.contabil.contrato.ContratoRepository;
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
@RequestMapping("/api/contratos")
@Slf4j
public class ContratoResource {
    @Autowired
    ContratoRepository contratoRepository;


    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_CREATE') and #oauth2.hasScope('write')")
    public Contrato addContrato(@RequestBody @Valid Contrato contrato)  {
        return contratoRepository.save(contrato);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Contrato> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable){
        return contratoRepository.findAll(pageable);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Contrato findContratoById(@PathVariable Long id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Contrato não encontrado"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_DELETE') and #oauth2.hasScope('write')")
    public void deleteContrato(@PathVariable Long id) {
        contratoRepository.findById(id).map(contrato -> {
            contratoRepository.delete(contrato);
            return TYPE;
        }).orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND, "Contrato não encontrado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE') and #oauth2.hasScope('write')")
    public ResponseEntity<Contrato> editContrato(@PathVariable Long id, @Valid @RequestBody Contrato newContrato){
        return contratoRepository.findById(id)
                .map(contrato -> {
                    contrato.setPrestador(newContrato.getPrestador());
                    contrato.setDescServico((newContrato.getDescServico()));
                    contrato.setDataFinal(newContrato.getDataInicial());
                    contrato.setDataFinal(newContrato.getDataFinal());
                    contrato.setObs(newContrato.getObs());
                    contrato.setValor(newContrato.getValor());
                    contrato.setFileUrl((newContrato.getFileUrl()));
                    Contrato contratoUpdated = contratoRepository.save(contrato);
                    return ResponseEntity.ok().body(contratoUpdated);
                }).orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Contrato não encontrado"));
    }


}

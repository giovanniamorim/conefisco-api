package org.conefisco.resource.contabil;

import org.conefisco.model.Balancete;
import org.conefisco.model.Inventario;
import org.conefisco.repository.contabil.inventario.InventarioRepository;
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
@RequestMapping("/api/inventario")
@Slf4j
public class InventarioResource {
    @Autowired
    InventarioRepository inventarioRepository;


    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('ROLE_CREATE') and #oauth2.hasScope('write')")
    public Inventario addInventari(@RequestBody @Valid Inventario inventario)  {
        return inventarioRepository.save(inventario);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Inventario> listAll(
            @PageableDefault(page = 0, size = 5, sort = "dataAquisicao", direction = Sort.Direction.DESC) Pageable pageable) {
        return inventarioRepository.findAll(pageable);
    }

    @GetMapping("/todos")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<Inventario> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable){
        return inventarioRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Inventario findInventarioById(@PathVariable Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Inventário não encontrado"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_DELETE') and #oauth2.hasScope('write')")
    public void deleteInventario(@PathVariable Long id) {
        inventarioRepository.findById(id).map(inventario -> {
            inventarioRepository.delete(inventario);
            return TYPE;
        }).orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND, "Inventário não encontrado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE') and #oauth2.hasScope('write')")
    public ResponseEntity<Inventario> editInventario(@PathVariable Long id, @Valid @RequestBody Inventario newInventario){
        return inventarioRepository.findById(id)
                .map(inventario -> {
                    inventario.setDataAquisicao(newInventario.getDataAquisicao());
                    inventario.setDepartamento((newInventario.getDepartamento()));
                    inventario.setNumero(newInventario.getNumero());
                    inventario.setQuant(newInventario.getQuant());
                    inventario.setDescricao(newInventario.getDescricao());
                    inventario.setFileUrl((newInventario.getFileUrl()));
                    inventario.setEstadoConservacao(newInventario.getEstadoConservacao());
                    Inventario inventarioUpdated = inventarioRepository.save(inventario);
                    return ResponseEntity.ok().body(inventarioUpdated);
                }).orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Inventário não encontrado"));
    }

}

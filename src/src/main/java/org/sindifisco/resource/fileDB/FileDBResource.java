package org.conefisco.resource.fileDB;

import lombok.RequiredArgsConstructor;
import org.conefisco.exception.ErrorResponse;
import org.conefisco.message.ResponseFile;
import org.conefisco.message.ResponseMessage;
import org.conefisco.model.FileDB;
import org.conefisco.repository.fileDB.FileDBRepository;
import org.conefisco.service.FileDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Void.TYPE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileDBResource {

    @Autowired
    private FileDBService filesService;

    @Autowired
    private FileDBRepository fileDBRepository;

    @PostMapping("/files/upload")
    @PreAuthorize("hasAuthority('ROLE_CREATE') and #oauth2.hasScope('write')")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message;
        try {
            // Validação e busca do arquivo existente como antes...

            // Verificar se já existe um arquivo com o mesmo nome
            FileDB existingFile = fileDBRepository.findByName(file.getOriginalFilename());
            if (existingFile != null) {
                // Excluir o arquivo existente
                filesService.delete(existingFile);
            }

            // Realizar o armazenamento do novo arquivo
            FileDB newFile = filesService.store(file);

            message = "Arquivo enviado com sucesso: " + newFile.getName();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (IOException e) {
            // Tratamento da exceção de IOException específica para o armazenamento de arquivos
            message = "Erro ao fazer o upload do arquivo: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        } catch (Exception e) {
            // Tratamento de exceções gerais que não são específicas para o armazenamento de arquivos
            message = "Ocorreu um erro durante o upload do arquivo: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }

    }

    @GetMapping("/files/list")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public Page<FileDB> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable){
        return fileDBRepository.findAll(pageable);
    }



    @GetMapping("/files")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = filesService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/file/find?name=")
                    .path((dbFile.getName()))
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/file/find")
    public ResponseEntity<byte[]> getFile(@RequestParam String name)  {

        try {
            FileDB fileDB = filesService.findByName(name);
            if (fileDB == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                    .body(fileDB.getData());
        } catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/file/deleteByName")
    public ResponseEntity<?> getFileByName(@RequestParam String name) {
        FileDB fileDB = filesService.findByName(name);
        try {
            fileDBRepository.delete(fileDB);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(
                    "Não foi possivel enviar arquivo: " + fileDB.getName() + "!"
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body("O Arquivo " + fileDB.getName() + " foi excluido com sucesso!");
    }


    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        FileDB fileDB = filesService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }

    @PutMapping("/file/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE') and #oauth2.hasScope('write')")
    public ResponseEntity<FileDB> updateFile(
            @PathVariable Long id, @Valid @RequestBody FileDB newFileDB, @RequestParam("file") MultipartFile file){
        return fileDBRepository.findById(id)
                .map(fileDB -> {
                    fileDB.setName(newFileDB.getName());
                    fileDB.setType(newFileDB.getType());
                    fileDB.setData(newFileDB.getData());

                    FileDB fileDBUpdated = fileDBRepository.save(fileDB);
                    return ResponseEntity.ok().body(fileDBUpdated);
                }).orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Arquivo não encontrado"));
    }

    @DeleteMapping("/file/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteFileDB(@PathVariable Long id) {
        fileDBRepository.findById(id).map(fileDB -> {
            fileDBRepository.delete(fileDB);
            return TYPE;
        }).orElseThrow(() -> new ResponseStatusException(
                NOT_FOUND, "Arquivo não encontrado"));
    }

    // Tratamento específico para a exceção de arquivo não encontrado.
    @ExceptionHandler(java.io.FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFoundException(java.io.FileNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage("Arquivo não encontrado.");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Tratamento específico para outras exceções (opcional).
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage("Ocorreu um erro interno.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


}

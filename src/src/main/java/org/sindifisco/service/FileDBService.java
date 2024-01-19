package org.conefisco.service;

import org.conefisco.model.FileDB;
import org.conefisco.repository.fileDB.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileDBService {

    @Autowired
    private FileDBRepository fileRepository;

    public FileDB store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());

        return fileRepository.save(fileDB);
    }

    public FileDB getFile(Long id) {
        return fileRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles() {
        return fileRepository.findAll().stream();
    }

    public FileDB findByName(String name) {
        return fileRepository.findByName(name);
    }

    public void delete(FileDB file){
        fileRepository.delete(file);
    }

}

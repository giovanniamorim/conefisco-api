package org.conefisco.repository.fileDB;

import org.conefisco.model.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, Long> {
    Boolean existsByName(String name);
    FileDB findByName(String name);

    void delete(FileDB fileDB);

}

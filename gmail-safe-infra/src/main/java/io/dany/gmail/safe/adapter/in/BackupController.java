package io.dany.gmail.safe.adapter.in;

import io.dany.gmail.safe.adapter.in.mapper.CreateBackupResponseMapper;
import io.dany.gmail.safe.adapter.in.mapper.GetBackupsResponseMapper;
import io.dany.gmail.safe.adapter.in.mapper.GetCompressedBackupQueryMapper;
import io.dany.gmail.safe.adapter.in.util.HttpServletResponseUtil;
import io.dany.gmail.safe.kernel.exception.UseCaseException;
import io.dany.gmail.safe.usecase.CreateBackup;
import io.dany.gmail.safe.usecase.GetBackups;
import io.dany.gmail.safe.usecase.GetCompressedBackup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class BackupController {

    private final CreateBackup createBackup;
    private final GetBackups getBackups;
    private final GetCompressedBackup getCompressedBackup;

    public BackupController(CreateBackup createBackup, GetBackups getBackups, GetCompressedBackup getCompressedBackup) {
        this.createBackup = createBackup;
        this.getBackups = getBackups;
        this.getCompressedBackup = getCompressedBackup;
    }

    @PostMapping("/backups")
    ResponseEntity<Object> createBackup() {
        return createBackup.execute()
                .map(CreateBackupResponseMapper::from)
                .map(ResponseEntity::<Object>ok)
                .getOrElseGet(t -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(t.getMessage()));
    }

    @GetMapping("/backups")
    ResponseEntity<Object> getBackups() {
        return getBackups.execute()
                .map(GetBackupsResponseMapper::from)
                .map(ResponseEntity::<Object>ok)
                .getOrElseGet(t -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(t.getMessage()));
    }

    @GetMapping("/exports/{backupId}")
    void exportBackup(@PathVariable String backupId, HttpServletResponse response) {
        HttpServletResponseUtil.downloadZip(response);
        GetCompressedBackupQueryMapper.from(backupId, response)
                .toEither()
                .mapLeft(UseCaseException::new)
                .flatMap(getCompressedBackup::execute)
                .peekLeft(e -> HttpServletResponseUtil.showException(response, e));
    }

    @GetMapping("/exports/{backupId}/{label}")
    void exportBackup(@PathVariable String backupId, @PathVariable String label, HttpServletResponse response) {
        HttpServletResponseUtil.downloadZip(response);
        GetCompressedBackupQueryMapper.from(backupId, label, response)
                .toEither()
                .mapLeft(UseCaseException::new)
                .flatMap(getCompressedBackup::execute)
                .peekLeft(e -> HttpServletResponseUtil.showException(response, e));
    }
}
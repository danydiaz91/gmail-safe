package io.dany.gmail.safe.adapter.in;

import io.dany.gmail.safe.adapter.in.mapper.CreateBackupResponseMapper;
import io.dany.gmail.safe.adapter.in.mapper.GetBackupsResponseMapper;
import io.dany.gmail.safe.usecase.CreateBackup;
import io.dany.gmail.safe.usecase.GetBackups;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackupController {

    private final CreateBackup createBackup;
    private final GetBackups getBackups;

    public BackupController(CreateBackup createBackup, GetBackups getBackups) {
        this.createBackup = createBackup;
        this.getBackups = getBackups;
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
}

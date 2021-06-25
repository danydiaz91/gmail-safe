package io.dany.gmail.safe.kernel.factory;

import io.dany.gmail.safe.kernel.model.Backup;
import io.dany.gmail.safe.kernel.model.ImmutableBackup;
import io.dany.gmail.safe.kernel.vo.Status;

import java.time.LocalDate;
import java.util.UUID;

public final class BackupFactory {

    private BackupFactory() {
    }

    /**
     * It helps to create a new Backup with the status IN_PROGRESS
     * and ID auto generated
     *
     * @return a new Backup
     */
    public static Backup newBackupInProgress() {
        return ImmutableBackup.builder()
                .id(UUID.randomUUID().toString())
                .date(LocalDate.now())
                .status(Status.IN_PROGRESS)
                .build();
    }

    /**
     * It helps to update the status of an existing Backup
     *
     * @param backup current backup
     * @param status new status
     * @return new backup
     */
    public static Backup copyWith(Backup backup, Status status) {
        return ImmutableBackup.builder()
                .from(backup)
                .status(status)
                .build();
    }
}

package io.dany.gmail.safe.mock;

import io.dany.gmail.safe.kernel.model.Backup;
import io.dany.gmail.safe.usecase.port.BackupRepository;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.doReturn;

public final class BackupRepositoryMock {

    private BackupRepositoryMock() {
    }

    public static void whenSave(BackupRepository backupRepositoryMock, ArgumentCaptor<Backup> backupCaptor, Try<Backup> toBeReturned) {
        doReturn(toBeReturned)
                .when(backupRepositoryMock)
                .save(backupCaptor.capture());
    }

    public static void whenFindAll(BackupRepository backupRepositoryMock, Try<List<Backup>> toBeReturned) {
        doReturn(toBeReturned)
                .when(backupRepositoryMock)
                .findAll();
    }
}

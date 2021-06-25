package io.dany.gmail.safe.fixture;

import io.dany.gmail.safe.kernel.model.Backup;
import io.dany.gmail.safe.kernel.model.ImmutableBackup;
import io.dany.gmail.safe.kernel.vo.Status;

import java.time.LocalDate;
import java.util.UUID;

public final class BackupFixture {

    private BackupFixture() {
    }

    public static ImmutableBackup.Builder builder() {
        return ImmutableBackup.builder()
                .id(UUID.randomUUID().toString())
                .status(Status.IN_PROGRESS)
                .date(LocalDate.of(2021, 1, 1));
    }

    public static Backup newDefault() {
        return builder()
                .build();
    }
}

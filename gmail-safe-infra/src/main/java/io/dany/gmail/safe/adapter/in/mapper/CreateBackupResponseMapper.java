package io.dany.gmail.safe.adapter.in.mapper;

import io.dany.gmail.safe.adapter.in.dto.CreateBackupResponse;
import io.dany.gmail.safe.adapter.in.dto.ImmutableCreateBackupResponse;
import io.dany.gmail.safe.kernel.model.Backup;

public final class CreateBackupResponseMapper {

    private CreateBackupResponseMapper() {
    }

    public static CreateBackupResponse from(Backup backup) {
        return ImmutableCreateBackupResponse.builder()
                .backupId(backup.getId())
                .build();
    }
}

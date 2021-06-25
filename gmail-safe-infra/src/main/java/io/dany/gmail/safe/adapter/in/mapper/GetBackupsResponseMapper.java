package io.dany.gmail.safe.adapter.in.mapper;

import io.dany.gmail.safe.adapter.in.dto.GetBackupsResponse;
import io.dany.gmail.safe.adapter.in.dto.ImmutableGetBackupsResponse;
import io.dany.gmail.safe.kernel.model.Backup;
import io.vavr.collection.List;

public final class GetBackupsResponseMapper {

    private GetBackupsResponseMapper() {
    }

    public static List<GetBackupsResponse> from(List<Backup> backups) {
        return backups.map(GetBackupsResponseMapper::from);
    }

    private static GetBackupsResponse from(Backup backup) {
        return ImmutableGetBackupsResponse.builder()
                .backupId(backup.getId())
                .date(backup.getDate())
                .status(backup.getStatus().getValue())
                .build();
    }
}

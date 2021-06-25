package io.dany.gmail.safe.adapter.in.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.time.LocalDate;

@JsonSerialize(as = ImmutableGetBackupsResponse.class)
@Value.Immutable
public interface GetBackupsResponse {
    String getBackupId();

    LocalDate getDate();

    String getStatus();
}

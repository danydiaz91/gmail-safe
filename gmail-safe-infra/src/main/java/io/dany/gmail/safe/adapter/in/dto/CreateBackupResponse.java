package io.dany.gmail.safe.adapter.in.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableCreateBackupResponse.class)
@Value.Immutable
public interface CreateBackupResponse {
    String getBackupId();
}

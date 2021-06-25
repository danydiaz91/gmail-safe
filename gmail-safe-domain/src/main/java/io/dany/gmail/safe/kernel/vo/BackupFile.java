package io.dany.gmail.safe.kernel.vo;

import org.immutables.value.Value;

@Value.Immutable
public interface BackupFile {
    byte[] getData();

    String getExtension();
}

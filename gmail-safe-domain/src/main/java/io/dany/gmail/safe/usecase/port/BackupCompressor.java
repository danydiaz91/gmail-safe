package io.dany.gmail.safe.usecase.port;

import io.dany.gmail.safe.kernel.model.Backup;

@FunctionalInterface
public interface BackupCompressor {

    String compress(Backup backup);
}

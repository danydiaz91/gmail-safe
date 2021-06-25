package io.dany.gmail.safe.usecase.port;

import io.dany.gmail.safe.kernel.model.Backup;
import io.vavr.control.Try;

public interface BackupRepository {

    Try<Backup> save(Backup backup);
}

package io.dany.gmail.safe.usecase.port;

import io.dany.gmail.safe.kernel.model.Backup;
import io.vavr.collection.List;
import io.vavr.control.Try;

public interface BackupRepository {

    Try<Backup> save(Backup backup);

    Try<Backup> findById(String id);

    Try<List<Backup>> findAll();
}

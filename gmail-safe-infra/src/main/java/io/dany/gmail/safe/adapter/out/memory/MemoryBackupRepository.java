package io.dany.gmail.safe.adapter.out.memory;

import io.dany.gmail.safe.kernel.model.Backup;
import io.dany.gmail.safe.usecase.port.BackupRepository;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryBackupRepository implements BackupRepository {

    private final Map<String, Backup> database;

    public MemoryBackupRepository() {
        database = new ConcurrentHashMap<>();
    }

    @Override
    public Try<Backup> save(Backup backup) {
        database.put(backup.getId(), backup);
        return Try.success(backup);
    }

    @Override
    public Try<Backup> findById(String id) {
        return Try.of(() -> database.get(id));
    }

    @Override
    public Try<List<Backup>> findAll() {
        return Try.success(List.ofAll(database.values()));
    }
}

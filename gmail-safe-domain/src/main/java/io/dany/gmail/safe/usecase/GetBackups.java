package io.dany.gmail.safe.usecase;

import io.dany.gmail.safe.kernel.exception.UseCaseException;
import io.dany.gmail.safe.kernel.model.Backup;
import io.dany.gmail.safe.usecase.port.BackupRepository;
import io.vavr.collection.List;
import io.vavr.control.Either;


/**
 * Get Backups Use Case
 *
 * Get all the backups stored in the repository
 */
public class GetBackups {

    private final BackupRepository backupRepository;

    /**
     * Instantiates a new GetBackups.
     *
     * @param backupRepository the backup repository
     */
    public GetBackups(BackupRepository backupRepository) {
        this.backupRepository = backupRepository;
    }

    /**
     * It Executes the process to get the all backups stored in the repository
     *
     * @return the either
     */
    public Either<UseCaseException, List<Backup>> execute() {
        return backupRepository.findAll()
                .toEither()
                .mapLeft(UseCaseException::new);
    }
}

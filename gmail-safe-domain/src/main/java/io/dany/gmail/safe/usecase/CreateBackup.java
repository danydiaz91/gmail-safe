package io.dany.gmail.safe.usecase;

import io.dany.gmail.safe.kernel.exception.UseCaseException;
import io.dany.gmail.safe.kernel.model.Backup;
import io.dany.gmail.safe.kernel.model.ImmutableBackup;
import io.dany.gmail.safe.kernel.model.Message;
import io.dany.gmail.safe.kernel.vo.Status;
import io.dany.gmail.safe.usecase.port.BackupRepository;
import io.dany.gmail.safe.usecase.port.MessageRepository;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Either;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Create Backup Use Case
 *
 * Create a backup with the status IN_PROGRESS and it will be returned immediately, meanwhile in the
 * background it will be reading the messages from the repository, if the repository returns
 * the messages correctly, they will be stored on the Backup and the status
 * will be updated to OK, otherwise will be updated to FAILED.
 */
public class CreateBackup {

    private final MessageRepository messageRepository;
    private final BackupRepository backupRepository;

    /**
     * Instantiates a new CreateBackup.
     *
     * @param messageRepository the message repository
     * @param backupRepository  the backup repository
     */
    public CreateBackup(MessageRepository messageRepository,
                        BackupRepository backupRepository) {
        this.messageRepository = messageRepository;
        this.backupRepository = backupRepository;
    }

    /**
     * Execute the process to create a backup
     *
     * @return Either.right when it returns a Backup, otherwise Either.left (Exception)
     */
    public Either<UseCaseException, Backup> execute() {
        Backup backup = newBackup();

        messageRepository.findAll()
                .onSuccess(messages -> backupCompleted(backup, messages))
                .onFailure(t -> backupFailed(backup));

        return backupRepository.save(backup)
                .toEither()
                .mapLeft(UseCaseException::new);
    }

    /**
     * It helps to create a new Backup with the status IN_PROGRESS
     * and ID auto generated
     *
     * @return a new Backup
     */
    private Backup newBackup() {
        return ImmutableBackup.builder()
                .id(UUID.randomUUID().toString())
                .date(LocalDate.now())
                .status(Status.IN_PROGRESS)
                .build();
    }

    /**
     * It helps to update the status of an existing Backup, then the
     * Backup will be stored in the repository.
     *
     * @param backup current Backup
     * @param status new status
     */
    private void updateBackupStatus(Backup backup, Status status) {
        Backup newBackup = ImmutableBackup.builder()
                .from(backup)
                .status(status)
                .build();

        backupRepository.save(newBackup)
            .onFailure(Throwable::printStackTrace);
    }

    /**
     * It helps to update the status of an existing backup to OK, and
     * put the messages in it, then the Backup will be stored in the repository.
     *
     * @param backup current Backup
     * @param messages the messages to be put on the Backup
     */
    private void backupCompleted(Backup backup, List<Message> messages) {
        backup = messages.foldLeft(backup, this::putMessage);
        updateBackupStatus(backup, Status.OK);
    }

    /**
     * It helps to update the status of an existing backup to FAILED, then the
     * Backup will be stored in the repository.
     *
     * @param backup current Backup
     */
    private void backupFailed(Backup backup) {
        updateBackupStatus(backup, Status.FAILED);
    }

    /**
     * It helps to put a message in the Backup and update the labels present in the Backup
     *
     * @param backup current Backup
     * @param message The message to be backed up
     * @return A new backup with the new data
     */
    private Backup putMessage(Backup backup, Message message) {
        Map<String, Set<String>> labelsPresent = message.getLabels()
                .foldLeft(backup.getLabelsPresent(), (acc, label) -> putLabel(acc, label, message.getId()));

        return ImmutableBackup.builder()
                .from(backup)
                .putMessages(message.getId(), message)
                .labelsPresent(labelsPresent)
                .build();
    }

    /**
     * It helps to update a list of labels with a new label and the ID of the message where it exists
     *
     * @param labelsPresent List of labels
     * @param label The new label
     * @param messageId The ID of the message that has the label
     *
     * @return The list of labels updated
     */
    private Map<String, Set<String>> putLabel(Map<String, Set<String>> labelsPresent, String label, String messageId) {
        Set<String> messages = labelsPresent.computeIfPresent(label, (ignored, currentMessages) -> currentMessages.add(messageId))
                ._1()
                .getOrElse(() -> HashSet.of(messageId));

        return labelsPresent.put(label, messages);
    }
}

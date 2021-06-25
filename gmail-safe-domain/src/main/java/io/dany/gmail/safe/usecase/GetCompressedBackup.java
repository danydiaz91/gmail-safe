package io.dany.gmail.safe.usecase;

import io.dany.gmail.safe.kernel.command.GetCompressedBackupQuery;
import io.dany.gmail.safe.kernel.exception.BackupNotFoundException;
import io.dany.gmail.safe.kernel.exception.BackupStatusNotValid;
import io.dany.gmail.safe.kernel.exception.MessagesNotFound;
import io.dany.gmail.safe.kernel.exception.UseCaseException;
import io.dany.gmail.safe.kernel.model.Backup;
import io.dany.gmail.safe.kernel.model.Message;
import io.dany.gmail.safe.kernel.vo.Status;
import io.dany.gmail.safe.usecase.port.BackupCompressor;
import io.dany.gmail.safe.usecase.port.BackupCompressorResolver;
import io.dany.gmail.safe.usecase.port.BackupRepository;
import io.dany.gmail.safe.usecase.port.MessageTransformer;
import io.dany.gmail.safe.usecase.port.MessageTransformerResolver;
import io.vavr.Predicates;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Either;

import java.io.OutputStream;
import java.util.function.Function;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.$None;
import static io.vavr.Patterns.$Some;

public class GetCompressedBackup {

    private final BackupRepository backupRepository;
    private final MessageTransformerResolver messageTransformerResolver;
    private final BackupCompressorResolver backupCompressorResolver;

    public GetCompressedBackup(BackupRepository backupRepository,
                               MessageTransformerResolver messageTransformerResolver,
                               BackupCompressorResolver backupCompressorResolver) {
        this.backupRepository = backupRepository;
        this.messageTransformerResolver = messageTransformerResolver;
        this.backupCompressorResolver = backupCompressorResolver;
    }

    public Either<UseCaseException, OutputStream> execute(GetCompressedBackupQuery query) {
        return backupRepository.findById(query.getBackupId())
                .toEither()
                .mapLeft(UseCaseException::new)
                .flatMap(this::validateBackupNotNull)
                .flatMap(backup -> filterMessages(backup, query))
                .flatMap(messages -> compressBackup(messages, query))
                .peekLeft(UseCaseException::printStackTrace);
    }

    private Either<UseCaseException, Backup> validateBackupNotNull(Backup backup) {
        return Match(backup).of(
                Case($(Predicates.isNotNull()), this::validateBackupStatus),
                Case($(), Either.left(new BackupNotFoundException()))
        );
    }

    private Either<UseCaseException, Backup> validateBackupStatus(Backup backup) {
        return Match(backup.getStatus()).of(
                Case($(Status.OK), Either.right(backup)),
                Case($(), status -> Either.left(new BackupStatusNotValid(status)))
        );
    }

    private Either<UseCaseException, List<Message>> filterMessages(Backup backup, GetCompressedBackupQuery query) {
        return Match(query.getLabel()).of(
                Case($Some($()), label -> filterMessages(backup, label)),
                Case($None(), Either.right(backup.getMessagesAsList()))
        );
    }

    private Either<UseCaseException, List<Message>> filterMessages(Backup backup, String label) {
        Set<String> messageIds = backup.getLabelsPresent()
                .get(label)
                .toSet()
                .flatMap(Function.identity());

        List<Message> messages = backup.getMessages()
                .filterKeys(messageIds::contains)
                .values()
                .toList();

        return Match(messages).of(
                Case($(List::nonEmpty), Either::right),
                Case($(), Either.left(new MessagesNotFound(label)))
        );
    }

    private Either<UseCaseException, OutputStream> compressBackup(List<Message> messages, GetCompressedBackupQuery query) {
        MessageTransformer messageTransformer = messageTransformerResolver.resolve(query.getTransformerStrategy());
        BackupCompressor backupCompressor = backupCompressorResolver.resolve(query.getCompressorStrategy());

        return messageTransformer.transform(messages)
                .flatMap(backupCompressor::compress)
                .toEither()
                .mapLeft(UseCaseException::new);
    }
}

package io.dany.gmail.safe.fixture;

import io.dany.gmail.safe.kernel.model.Backup;
import io.dany.gmail.safe.kernel.model.ImmutableBackup;
import io.dany.gmail.safe.kernel.model.Message;
import io.dany.gmail.safe.kernel.vo.Status;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

import java.time.LocalDate;
import java.util.UUID;

import static io.vavr.API.Tuple;

public final class BackupFixture {

    private BackupFixture() {
    }

    public static ImmutableBackup.Builder builder() {
        return ImmutableBackup.builder()
                .id(UUID.randomUUID().toString())
                .status(Status.IN_PROGRESS)
                .date(LocalDate.of(2021, 1, 1));
    }

    public static Backup newDefault() {
        return builder()
                .build();
    }

    public static Backup withMessages(List<Message> messages) {
        return builder()
                .messages(toMessagesMap(messages))
                .status(Status.OK)
                .build();
    }

    public static Backup withMessagesAndLabels(List<Message> messages, Map<String, Set<String>> labelsPresent) {
        return builder()
                .messages(toMessagesMap(messages))
                .status(Status.OK)
                .labelsPresent(labelsPresent)
                .build();
    }

    public static Backup withStatus(Status status) {
        return builder()
                .status(status)
                .build();
    }

    private static Map<String, Message> toMessagesMap(List<Message> messages) {
        return messages
                .map(message -> Tuple(message.getId(), message))
                .transform(HashMap::ofEntries);
    }
}

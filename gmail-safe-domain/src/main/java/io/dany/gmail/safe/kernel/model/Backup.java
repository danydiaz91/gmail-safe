package io.dany.gmail.safe.kernel.model;

import io.dany.gmail.safe.kernel.vo.Status;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import org.immutables.value.Value;
import org.immutables.vavr.encodings.VavrEncodingEnabled;

import java.time.LocalDate;

@Value.Immutable
@VavrEncodingEnabled
public interface Backup {
    String getId();

    LocalDate getDate();

    Status getStatus();

    Map<String, Message> getMessages();

    Map<String, Set<String>> getLabelsPresent();

    default List<Message> getMessagesAsList() {
        return getMessages().values()
                .toList();
    }
}

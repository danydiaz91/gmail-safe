package io.dany.gmail.safe.fixture;

import io.dany.gmail.safe.common.Constants;
import io.dany.gmail.safe.kernel.model.ImmutableMessage;
import io.dany.gmail.safe.kernel.model.Message;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import java.util.UUID;

public final class MessageFixture {

    private MessageFixture() {
    }

    public static ImmutableMessage.Builder builder() {
        return ImmutableMessage.builder()
                .id(UUID.randomUUID().toString())
                .labels(HashSet.of(Constants.LABEL_INBOX, Constants.LABEL_TODO))
                .body(Constants.EMPTY_STRING)
                .to(Constants.EMPTY_STRING)
                .from(Constants.EMPTY_STRING)
                .cC(Constants.EMPTY_STRING)
                .subject(Constants.EMPTY_STRING);
    }

    public static Message newDefault() {
        return builder()
                .build();
    }

    public static Message withLabels(Set<String> labels) {
        return builder()
                .labels(labels)
                .build();
    }
}

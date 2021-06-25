package io.dany.gmail.safe.adapter.out.gmail.mapper;

import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import io.dany.gmail.safe.kernel.model.ImmutableMessage;
import io.dany.gmail.safe.kernel.model.Message;
import io.vavr.control.Option;

import java.util.function.Function;

public final class MessageMapper {

    private static final String HEADER_SUBJECT = "Subject";
    private static final String HEADER_FROM = "From";
    private static final String HEADER_TO = "To";
    private static final String HEADER_CC = "CC";
    private static final String EMPTY_STRING = "";

    private MessageMapper() {
    }

    public static Message toMessage(com.google.api.services.gmail.model.Message message) {
        return ImmutableMessage.builder()
                .id(message.getId())
                .subject(getSubject(message))
                .body(getBody(message))
                .from(getFrom(message))
                .to(getTo(message))
                .cC(getCC(message))
                .addAllLabels(message.getLabelIds())
                .build();
    }

    private static String getHeader(com.google.api.services.gmail.model.Message message, String headerName) {
        return Option.of(message)
                .map(com.google.api.services.gmail.model.Message::getPayload)
                .map(MessagePart::getHeaders)
                .toList()
                .flatMap(Function.identity())
                .find(header -> headerName.equals(header.getName()))
                .map(MessagePartHeader::getValue)
                .getOrElse(() -> EMPTY_STRING);
    }

    private static String getSubject(com.google.api.services.gmail.model.Message message) {
        return getHeader(message, HEADER_SUBJECT);
    }

    private static String getFrom(com.google.api.services.gmail.model.Message message) {
        return getHeader(message, HEADER_FROM);
    }

    private static String getTo(com.google.api.services.gmail.model.Message message) {
        return getHeader(message, HEADER_TO);
    }

    private static String getCC(com.google.api.services.gmail.model.Message message) {
        return getHeader(message, HEADER_CC);
    }

    private static String getBody(com.google.api.services.gmail.model.Message message) {
        return Option.of(message)
                .flatMap(oMessage -> Option.of(oMessage.getPayload()))
                .flatMap(messagePart -> Option.of(messagePart.getParts()))
                .toList()
                .flatMap(Function.identity())
                .headOption()
                .flatMap(messagePart ->  Option.of(messagePart.getBody()))
                .flatMap(messagePartBody -> Option.of(messagePartBody.getData()))
                .getOrElse(() -> EMPTY_STRING);
    }
}

package io.dany.gmail.safe.mock;

import io.dany.gmail.safe.kernel.model.Message;
import io.dany.gmail.safe.usecase.port.MessageTransformer;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;

import static org.mockito.Mockito.doReturn;

public final class MessageTransformerMock {

    private MessageTransformerMock() {
    }

    public static void whenTransform(MessageTransformer messageTransformerMock, ArgumentCaptor<List<Message>> messagesCaptor) {
        doReturn(Try.success(new ByteArrayOutputStream()))
                .when(messageTransformerMock)
                .transform(messagesCaptor.capture());
    }
}

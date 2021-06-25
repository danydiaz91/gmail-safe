package io.dany.gmail.safe.mock;

import io.dany.gmail.safe.kernel.model.Message;
import io.dany.gmail.safe.usecase.port.MessageRepository;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;

import static org.mockito.Mockito.doReturn;

public final class MessageRepositoryMock {

    private MessageRepositoryMock() {
    }

    public static void whenFindAll(MessageRepository messageRepositoryMock, Future<List<Message>> toBeReturned) {
        doReturn(toBeReturned)
                .when(messageRepositoryMock)
                .findAll();
    }
}

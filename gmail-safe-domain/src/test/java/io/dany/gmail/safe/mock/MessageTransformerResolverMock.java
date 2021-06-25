package io.dany.gmail.safe.mock;

import io.dany.gmail.safe.kernel.vo.TransformerStrategy;
import io.dany.gmail.safe.usecase.port.MessageTransformer;
import io.dany.gmail.safe.usecase.port.MessageTransformerResolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public final class MessageTransformerResolverMock {

    private MessageTransformerResolverMock() {
    }

    public static void whenResolve(MessageTransformerResolver messageTransformerResolverMock, MessageTransformer messageTransformer) {
        doReturn(messageTransformer)
                .when(messageTransformerResolverMock)
                .resolve(any(TransformerStrategy.class));
    }
}

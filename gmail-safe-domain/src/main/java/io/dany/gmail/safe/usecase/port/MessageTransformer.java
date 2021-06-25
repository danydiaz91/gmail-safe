package io.dany.gmail.safe.usecase.port;

import io.dany.gmail.safe.kernel.model.Message;

@FunctionalInterface
public interface MessageTransformer {

    <T> T transform(Message message);
}

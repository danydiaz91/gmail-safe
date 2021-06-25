package io.dany.gmail.safe.usecase.port;

import io.dany.gmail.safe.kernel.model.Message;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.io.OutputStream;

@FunctionalInterface
public interface MessageTransformer {

    Try<OutputStream> transform(List<Message> messages);
}

package io.dany.gmail.safe.usecase.port;

import io.dany.gmail.safe.kernel.model.Message;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;

public interface MessageRepository {

    Future<List<Message>> findAll();
}

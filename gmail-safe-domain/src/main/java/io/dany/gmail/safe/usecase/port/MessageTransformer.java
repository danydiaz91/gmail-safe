package io.dany.gmail.safe.usecase.port;

import io.dany.gmail.safe.kernel.model.Message;
import io.dany.gmail.safe.kernel.vo.BackupFile;
import io.vavr.collection.List;
import io.vavr.control.Try;

@FunctionalInterface
public interface MessageTransformer {

    Try<BackupFile> transform(List<Message> messages);
}

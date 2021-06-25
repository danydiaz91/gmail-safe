package io.dany.gmail.safe.adapter.out.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dany.gmail.safe.kernel.model.Message;
import io.dany.gmail.safe.kernel.vo.BackupFile;
import io.dany.gmail.safe.kernel.vo.ImmutableBackupFile;
import io.dany.gmail.safe.usecase.port.MessageTransformer;
import io.vavr.collection.List;
import io.vavr.control.Try;
import io.vavr.jackson.datatype.VavrModule;

public class JacksonMessageTransformer implements MessageTransformer {

    private static final String EXTENSION = "json";

    @Override
    public Try<BackupFile> transform(List<Message> messages) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new VavrModule());

        return Try.of(() -> objectMapper.writeValueAsBytes(messages))
                .map(this::toBackupFile);
    }

    public BackupFile toBackupFile(byte[] data) {
        return ImmutableBackupFile.builder()
                .data(data)
                .extension(EXTENSION)
                .build();
    }
}

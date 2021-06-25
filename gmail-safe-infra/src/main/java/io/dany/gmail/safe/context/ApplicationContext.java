package io.dany.gmail.safe.context;

import io.dany.gmail.safe.usecase.CreateBackup;
import io.dany.gmail.safe.usecase.GetBackups;
import io.dany.gmail.safe.usecase.GetCompressedBackup;
import io.dany.gmail.safe.usecase.port.BackupCompressorResolver;
import io.dany.gmail.safe.usecase.port.BackupRepository;
import io.dany.gmail.safe.usecase.port.MessageRepository;
import io.dany.gmail.safe.usecase.port.MessageTransformerResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationContext {

    @Bean
    public CreateBackup createBackup(MessageRepository messageRepository, BackupRepository backupRepository) {
        return new CreateBackup(messageRepository, backupRepository);
    }

    @Bean
    public GetBackups getBackups(BackupRepository backupRepository) {
        return new GetBackups(backupRepository);
    }

    @Bean
    public GetCompressedBackup getCompressedBackup(BackupRepository backupRepository,
                                                      MessageTransformerResolver messageTransformerResolver,
                                                      BackupCompressorResolver backupCompressorResolver) {
        return new GetCompressedBackup(backupRepository, messageTransformerResolver, backupCompressorResolver);
    }
}

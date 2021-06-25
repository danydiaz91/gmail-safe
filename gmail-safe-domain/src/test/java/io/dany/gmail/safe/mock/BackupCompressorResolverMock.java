package io.dany.gmail.safe.mock;

import io.dany.gmail.safe.kernel.vo.CompressorStrategy;
import io.dany.gmail.safe.usecase.port.BackupCompressor;
import io.dany.gmail.safe.usecase.port.BackupCompressorResolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public final class BackupCompressorResolverMock {

    private BackupCompressorResolverMock() {
    }

    public static void whenCompress(BackupCompressorResolver backupCompressorResolverMock, BackupCompressor toBeReturned) {
        doReturn(toBeReturned)
                .when(backupCompressorResolverMock)
                .resolve(any(CompressorStrategy.class));
    }
}

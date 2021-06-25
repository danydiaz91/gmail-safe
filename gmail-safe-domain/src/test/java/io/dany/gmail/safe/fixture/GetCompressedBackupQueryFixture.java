package io.dany.gmail.safe.fixture;

import io.dany.gmail.safe.kernel.command.GetCompressedBackupQuery;
import io.dany.gmail.safe.kernel.command.ImmutableGetCompressedBackupQuery;
import io.dany.gmail.safe.kernel.vo.CompressorStrategy;
import io.dany.gmail.safe.kernel.vo.TransformerStrategy;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public final class GetCompressedBackupQueryFixture {

    private GetCompressedBackupQueryFixture() {
    }

    public static ImmutableGetCompressedBackupQuery.Builder builder() {
        return ImmutableGetCompressedBackupQuery.builder()
                .transformerStrategy(TransformerStrategy.DEFAULT)
                .compressorStrategy(CompressorStrategy.DEFAULT)
                .outputStream(new ByteArrayOutputStream())
                .backupId(UUID.randomUUID().toString());
    }

    public static GetCompressedBackupQuery newDefault() {
        return builder()
                .build();
    }

    public static GetCompressedBackupQuery withLabel(String label) {
        return builder()
                .label(label)
                .build();
    }
}

package io.dany.gmail.safe.adapter.in.mapper;

import io.dany.gmail.safe.kernel.command.GetCompressedBackupQuery;
import io.dany.gmail.safe.kernel.command.ImmutableGetCompressedBackupQuery;
import io.dany.gmail.safe.kernel.vo.CompressorStrategy;
import io.dany.gmail.safe.kernel.vo.TransformerStrategy;
import io.vavr.control.Try;

import javax.servlet.http.HttpServletResponse;

public final class GetCompressedBackupQueryMapper {

    private GetCompressedBackupQueryMapper() {
    }

    public static Try<GetCompressedBackupQuery> from(String backupId, HttpServletResponse response) {
        return Try.of(() -> ImmutableGetCompressedBackupQuery.builder()
                .backupId(backupId)
                .transformerStrategy(TransformerStrategy.JSON)
                .compressorStrategy(CompressorStrategy.ZIP)
                .outputStream(response.getOutputStream())
                .build());
    }

    public static Try<GetCompressedBackupQuery> from(String backupId, String label, HttpServletResponse response) {
        return Try.of(() -> ImmutableGetCompressedBackupQuery.builder()
                .backupId(backupId)
                .label(label)
                .transformerStrategy(TransformerStrategy.JSON)
                .compressorStrategy(CompressorStrategy.ZIP)
                .outputStream(response.getOutputStream())
                .build());
    }
}

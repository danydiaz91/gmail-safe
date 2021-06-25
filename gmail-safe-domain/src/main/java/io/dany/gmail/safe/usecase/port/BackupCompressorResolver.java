package io.dany.gmail.safe.usecase.port;

import io.dany.gmail.safe.kernel.vo.CompressorStrategy;

@FunctionalInterface
public interface BackupCompressorResolver {

    BackupCompressor resolve(CompressorStrategy compressorStrategy);
}

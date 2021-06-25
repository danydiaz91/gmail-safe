package io.dany.gmail.safe.adapter;

import io.dany.gmail.safe.adapter.out.zip.ZipBackupCompressor;
import io.dany.gmail.safe.kernel.vo.CompressorStrategy;
import io.dany.gmail.safe.usecase.port.BackupCompressor;
import io.dany.gmail.safe.usecase.port.BackupCompressorResolver;
import org.springframework.stereotype.Component;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

@Component
public class BackupCompressorResolverImpl implements BackupCompressorResolver {

    @Override
    public BackupCompressor resolve(CompressorStrategy compressorStrategy) {
        return Match(compressorStrategy).of(
                Case($(CompressorStrategy.ZIP), ZipBackupCompressor::new),
                Case($(), ZipBackupCompressor::new)
        );
    }
}

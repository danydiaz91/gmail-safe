package io.dany.gmail.safe.usecase.port;

import io.dany.gmail.safe.kernel.vo.BackupFile;
import io.vavr.control.Try;

import java.io.OutputStream;

@FunctionalInterface
public interface BackupCompressor {

    Try<Void> compress(OutputStream outputStream, BackupFile backupFile);
}

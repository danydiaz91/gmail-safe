package io.dany.gmail.safe.usecase.port;

import io.vavr.control.Try;

import java.io.OutputStream;

@FunctionalInterface
public interface BackupCompressor {

    Try<OutputStream> compress(OutputStream data);
}

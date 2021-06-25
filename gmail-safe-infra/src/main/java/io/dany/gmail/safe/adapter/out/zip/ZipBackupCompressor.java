package io.dany.gmail.safe.adapter.out.zip;

import io.dany.gmail.safe.kernel.vo.BackupFile;
import io.dany.gmail.safe.usecase.port.BackupCompressor;
import io.vavr.control.Try;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipBackupCompressor implements BackupCompressor {

    private static final String FILE_NAME = "backup.";

    @Override
    public Try<Void> compress(OutputStream outputStream, BackupFile backupFile) {
        return Try.run(() -> writeFile(outputStream, backupFile));
    }

    public void writeFile(OutputStream outputStream, BackupFile backupFile) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream, StandardCharsets.UTF_8);
        zipOutputStream.putNextEntry(new ZipEntry(FILE_NAME + backupFile.getExtension()));
        zipOutputStream.write(backupFile.getData());
        zipOutputStream.close();
    }
}

package io.dany.gmail.safe.kernel.exception;

public class BackupNotFoundException extends UseCaseException {

    public BackupNotFoundException() {
        super("Backup not found");
    }
}

package io.dany.gmail.safe.kernel.exception;

import io.dany.gmail.safe.kernel.vo.Status;

public class BackupStatusNotValid extends UseCaseException {

    public BackupStatusNotValid(Status status) {
        super(String.format("It is not possible to compress a backup with status '%s'", status.getValue()));
    }
}

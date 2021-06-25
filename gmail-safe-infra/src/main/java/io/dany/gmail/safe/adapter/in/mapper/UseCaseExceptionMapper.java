package io.dany.gmail.safe.adapter.in.mapper;

import io.dany.gmail.safe.kernel.exception.BackupNotFoundException;
import io.dany.gmail.safe.kernel.exception.BackupStatusNotValid;
import io.dany.gmail.safe.kernel.exception.MessagesNotFound;
import io.dany.gmail.safe.kernel.exception.UseCaseException;
import org.springframework.http.HttpStatus;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

public final class UseCaseExceptionMapper {

    private UseCaseExceptionMapper() {
    }

    public static int to(UseCaseException useCaseException) {
        return Match(useCaseException).of(
                Case($(instanceOf(BackupNotFoundException.class)), HttpStatus.NOT_FOUND.value()),
                Case($(instanceOf(BackupStatusNotValid.class)), HttpStatus.BAD_REQUEST.value()),
                Case($(instanceOf(MessagesNotFound.class)), HttpStatus.NOT_FOUND.value()),
                Case($(), HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }
}

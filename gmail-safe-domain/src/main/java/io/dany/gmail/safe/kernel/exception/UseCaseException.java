package io.dany.gmail.safe.kernel.exception;

public class UseCaseException extends RuntimeException {

    public UseCaseException(Throwable cause) {
        super(cause.getMessage());
    }

    public UseCaseException(String message) {
        super(message);
    }
}

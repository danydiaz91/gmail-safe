package io.dany.gmail.safe.kernel.exception;

public class MessagesNotFound extends UseCaseException {

    public MessagesNotFound(String label) {
        super("Messages not found with label " + label);
    }
}

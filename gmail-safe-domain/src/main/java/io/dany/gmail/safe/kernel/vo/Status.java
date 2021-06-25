package io.dany.gmail.safe.kernel.vo;

public enum Status {
    IN_PROGRESS("In progress"),
    OK("OK"),
    FAILED("Failed");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package io.dany.gmail.safe.kernel.vo;

public enum TransformerStrategy {
    JSON("json"),
    DEFAULT("default");

    private final String value;

    TransformerStrategy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

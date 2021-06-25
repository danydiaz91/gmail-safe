package io.dany.gmail.safe.kernel.vo;

public enum CompressorStrategy {
    ZIP("zip");

    private final String value;

    CompressorStrategy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package io.dany.gmail.safe.kernel.model;

import io.vavr.collection.Set;
import org.immutables.value.Value;
import org.immutables.vavr.encodings.VavrEncodingEnabled;

@Value.Immutable
@Value.Style(from = "fromCopy")
@VavrEncodingEnabled
public interface Message {
    String getId();

    String getSubject();

    String getBody();

    String getTo();

    String getFrom();

    String getCC();

    Set<String> getLabels();
}

package io.dany.gmail.safe.kernel.command;

import io.dany.gmail.safe.kernel.vo.CompressorStrategy;
import io.dany.gmail.safe.kernel.vo.TransformerStrategy;
import io.vavr.control.Option;
import org.immutables.value.Value;
import org.immutables.vavr.encodings.VavrEncodingEnabled;

import java.io.OutputStream;

@Value.Immutable
@VavrEncodingEnabled
public interface GetCompressedBackupQuery extends Query {
    String getBackupId();

    Option<String> getLabel();

    TransformerStrategy getTransformerStrategy();

    CompressorStrategy getCompressorStrategy();

    OutputStream getOutputStream();
}

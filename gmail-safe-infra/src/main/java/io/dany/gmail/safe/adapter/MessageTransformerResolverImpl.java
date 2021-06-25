package io.dany.gmail.safe.adapter;

import io.dany.gmail.safe.adapter.out.jackson.JacksonMessageTransformer;
import io.dany.gmail.safe.kernel.vo.TransformerStrategy;
import io.dany.gmail.safe.usecase.port.MessageTransformer;
import io.dany.gmail.safe.usecase.port.MessageTransformerResolver;
import org.springframework.stereotype.Component;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

@Component
public class MessageTransformerResolverImpl implements MessageTransformerResolver {

    @Override
    public MessageTransformer resolve(TransformerStrategy transformerStrategy) {
        return Match(transformerStrategy).of(
                Case($(TransformerStrategy.JSON), JacksonMessageTransformer::new),
                Case($(), JacksonMessageTransformer::new)
        );
    }
}

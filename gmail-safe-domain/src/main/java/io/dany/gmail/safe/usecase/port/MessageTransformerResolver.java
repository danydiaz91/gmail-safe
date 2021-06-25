package io.dany.gmail.safe.usecase.port;

import io.dany.gmail.safe.kernel.vo.TransformerStrategy;

@FunctionalInterface
public interface MessageTransformerResolver {

    MessageTransformer resolve(TransformerStrategy transformerStrategy);
}

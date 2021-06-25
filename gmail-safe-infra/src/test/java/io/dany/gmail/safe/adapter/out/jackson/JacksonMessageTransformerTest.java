package io.dany.gmail.safe.adapter.out.jackson;

import io.dany.gmail.safe.fixture.MessageFixture;
import io.dany.gmail.safe.kernel.model.Message;
import io.dany.gmail.safe.kernel.vo.BackupFile;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class JacksonMessageTransformerTest {

    private JacksonMessageTransformer jacksonMessageTransformer;

    @BeforeEach
    void setUp() {
        jacksonMessageTransformer = new JacksonMessageTransformer();
    }

    @Test
    void shouldReturnOutputStream() {
        Message message = MessageFixture.withId("111");

        Try<BackupFile> result = jacksonMessageTransformer.transform(List.of(message));

        assertThat(result.isSuccess(), is(Boolean.TRUE));
        assertThat(result.get(), notNullValue());
        assertThat(new String(result.get().getData()), equalTo("[{\"id\":\"111\",\"subject\":\"\",\"body\":\"\",\"to\":\"\",\"from\":\"\",\"labels\":[\"TODO\",\"INBOX\"],\"cc\":\"\"}]"));
    }
}
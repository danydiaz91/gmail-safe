package io.dany.gmail.safe.adapter.out.gmail;

import com.google.api.services.gmail.Gmail;
import io.dany.gmail.safe.adapter.out.gmail.mapper.MessageMapper;
import io.dany.gmail.safe.kernel.model.Message;
import io.dany.gmail.safe.usecase.port.MessageRepository;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import org.springframework.stereotype.Repository;

@Repository
public class GmailMessageRepository implements MessageRepository {

    private static final String USER_ID = "me";

    private final Gmail gmailClient;

    public GmailMessageRepository(Gmail gmailClient) {
        this.gmailClient = gmailClient;
    }

    @Override
    public Future<List<Message>> findAll() {
        System.out.println("Reading Messages");
        return Future.of(() -> gmailClient.users().messages().get(USER_ID, "17a37255066fa210").execute())
                //.map(ListMessagesResponse::getMessages)
                .map(MessageMapper::toMessage)
                .map(List::of)
                .onFailure(Throwable::printStackTrace);
    }
}

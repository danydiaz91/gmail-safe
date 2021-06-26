package io.dany.gmail.safe.adapter.out.gmail;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import io.dany.gmail.safe.adapter.out.gmail.mapper.MessageMapper;
import io.dany.gmail.safe.kernel.model.Message;
import io.dany.gmail.safe.usecase.port.MessageRepository;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class GmailMessageRepository implements MessageRepository {

    private static final Logger log = LoggerFactory.getLogger(GmailMessageRepository.class);

    private static final String USER_ID = "me";

    private final Gmail gmailClient;

    public GmailMessageRepository(Gmail gmailClient) {
        this.gmailClient = gmailClient;
    }

    @Override
    public Future<List<Message>> findAll() {
        return findAllGmailMessages()
                .map(this::findFullMessages)
                .onFailure(Throwable::printStackTrace)
                .onSuccess(oMessages -> log.info("Messages Found: {}", oMessages.size()));
    }

    private Future<List<com.google.api.services.gmail.model.Message>> findAllGmailMessages() {
        return Future.of(() -> {
            String query = "-in:chats";
            List<com.google.api.services.gmail.model.Message> messages = List.empty();
            ListMessagesResponse response = gmailClient.users().messages().list(USER_ID).setQ(query).execute();

            while (response.getMessages() != null) {
                messages = messages.appendAll(response.getMessages());
                if (response.getNextPageToken() != null) {
                    String pageToken = response.getNextPageToken();
                    response = gmailClient.users().messages().list(USER_ID)
                            .setQ(query)
                            .setPageToken(pageToken).execute();
                } else {
                    break;
                }
            }

            return messages;
        });
    }

    private List<Message> findFullMessages(List<com.google.api.services.gmail.model.Message> messages) {
        return messages.toJavaParallelStream()
                .map(message -> findById(message.getId()))
                .filter(Objects::nonNull)
                .collect(List.collector());
    }

    private Message findById(String messageId) {
        return Try.of(() -> gmailClient.users().messages().get(USER_ID, messageId).execute())
                .map(MessageMapper::toMessage)
                .onFailure(Throwable::printStackTrace)
                .getOrElse(() -> null);
    }
}

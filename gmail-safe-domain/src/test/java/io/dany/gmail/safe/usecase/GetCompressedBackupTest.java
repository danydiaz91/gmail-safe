package io.dany.gmail.safe.usecase;

import io.dany.gmail.safe.common.Constants;
import io.dany.gmail.safe.fixture.BackupFixture;
import io.dany.gmail.safe.fixture.GetCompressedBackupQueryFixture;
import io.dany.gmail.safe.fixture.MessageFixture;
import io.dany.gmail.safe.kernel.command.GetCompressedBackupQuery;
import io.dany.gmail.safe.kernel.exception.BackupNotFoundException;
import io.dany.gmail.safe.kernel.exception.BackupStatusNotValid;
import io.dany.gmail.safe.kernel.exception.MessagesNotFound;
import io.dany.gmail.safe.kernel.exception.UseCaseException;
import io.dany.gmail.safe.kernel.model.Backup;
import io.dany.gmail.safe.kernel.model.Message;
import io.dany.gmail.safe.kernel.vo.Status;
import io.dany.gmail.safe.mock.BackupCompressorResolverMock;
import io.dany.gmail.safe.mock.BackupRepositoryMock;
import io.dany.gmail.safe.mock.MessageTransformerMock;
import io.dany.gmail.safe.mock.MessageTransformerResolverMock;
import io.dany.gmail.safe.usecase.port.BackupCompressorResolver;
import io.dany.gmail.safe.usecase.port.BackupRepository;
import io.dany.gmail.safe.usecase.port.MessageTransformer;
import io.dany.gmail.safe.usecase.port.MessageTransformerResolver;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static io.vavr.API.Set;
import static io.vavr.API.Tuple;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

class GetCompressedBackupTest {

    private GetCompressedBackup getCompressedBackup;

    @Mock
    private BackupRepository backupRepositoryMock;

    @Mock
    private MessageTransformerResolver messageTransformerResolverMock;

    @Mock
    private BackupCompressorResolver backupCompressorResolverMock;

    @Mock
    private MessageTransformer messageTransformerMock;

    @Captor
    private ArgumentCaptor<List<Message>> messagesCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        getCompressedBackup = new GetCompressedBackup(backupRepositoryMock, messageTransformerResolverMock, backupCompressorResolverMock);
    }

    @Test
    void shouldReturnBackupNotFoundException() {
        GetCompressedBackupQuery query = GetCompressedBackupQueryFixture.newDefault();

        BackupRepositoryMock.whenFindById(backupRepositoryMock, Try.success(null));

        Either<UseCaseException, Void> result = getCompressedBackup.execute(query);

        assertThat(result.isLeft(), is(Boolean.TRUE));
        assertThat(result.getLeft(), instanceOf(BackupNotFoundException.class));
    }

    @Test
    void shouldReturnBackupStatusNotValidException() {
        Backup backup = BackupFixture.newDefault();
        GetCompressedBackupQuery query = GetCompressedBackupQueryFixture.newDefault();

        BackupRepositoryMock.whenFindById(backupRepositoryMock, Try.success(backup));

        Either<UseCaseException, Void> result = getCompressedBackup.execute(query);

        assertThat(result.isLeft(), is(Boolean.TRUE));
        assertThat(result.getLeft(), instanceOf(BackupStatusNotValid.class));
    }

    @Test
    void shouldReturnMessagesNotFoundException() {
        Backup backup = BackupFixture.withStatus(Status.OK);
        GetCompressedBackupQuery query = GetCompressedBackupQueryFixture.withLabel(Constants.LABEL_INBOX);

        BackupRepositoryMock.whenFindById(backupRepositoryMock, Try.success(backup));

        Either<UseCaseException, Void> result = getCompressedBackup.execute(query);

        assertThat(result.isLeft(), is(Boolean.TRUE));
        assertThat(result.getLeft(), instanceOf(MessagesNotFound.class));
    }

    @Test
    void shouldReturnBackupFilterById() {
        Message message1 = MessageFixture.newDefault();
        Message message2 = MessageFixture.newDefault();

        List<Message> messages = List.of(message1, message2);

        Backup backup = BackupFixture.withMessages(messages);
        GetCompressedBackupQuery query = GetCompressedBackupQueryFixture.newDefault();

        BackupRepositoryMock.whenFindById(backupRepositoryMock, Try.success(backup));
        MessageTransformerMock.whenTransform(messageTransformerMock, messagesCaptor);
        MessageTransformerResolverMock.whenResolve(messageTransformerResolverMock, messageTransformerMock);
        BackupCompressorResolverMock.whenCompress(backupCompressorResolverMock, (out, data) -> Try.success(null));

        Either<UseCaseException, Void> result = getCompressedBackup.execute(query);

        assertThat(result.isRight(), is(Boolean.TRUE));
        assertThat(messagesCaptor.getValue(), containsInAnyOrder(message1, message2));
    }

    @Test
    void shouldReturnBackupFilterByIdAndLabel() {
        Message message1 = MessageFixture.withLabels(HashSet.of(Constants.LABEL_TODO));
        Message message2 = MessageFixture.withLabels(HashSet.of(Constants.LABEL_INBOX));
        Message message3 = MessageFixture.withLabels(HashSet.of(Constants.LABEL_TODO));
        Message message4 = MessageFixture.withLabels(HashSet.of(Constants.LABEL_INBOX));

        List<Message> messages = List.of(message1, message2, message3, message4);

        Map<String, Set<String>> labelsPresent = HashMap.ofEntries(
                Tuple(Constants.LABEL_TODO, Set(message1.getId(), message3.getId())),
                Tuple(Constants.LABEL_INBOX, Set(message2.getId(), message4.getId()))
        );

        Backup backup = BackupFixture.withMessagesAndLabels(messages, labelsPresent);
        GetCompressedBackupQuery query = GetCompressedBackupQueryFixture.withLabel(Constants.LABEL_INBOX);

        BackupRepositoryMock.whenFindById(backupRepositoryMock, Try.success(backup));
        MessageTransformerMock.whenTransform(messageTransformerMock, messagesCaptor);
        MessageTransformerResolverMock.whenResolve(messageTransformerResolverMock, messageTransformerMock);
        BackupCompressorResolverMock.whenCompress(backupCompressorResolverMock, (out, data) -> Try.success(null));

        Either<UseCaseException, Void> result = getCompressedBackup.execute(query);

        assertThat(result.isRight(), is(Boolean.TRUE));
        assertThat(result.get(), not(backup));
        assertThat(messagesCaptor.getValue().size(), equalTo(2));
        assertThat(messagesCaptor.getValue(), containsInAnyOrder(message2, message4));
    }
}
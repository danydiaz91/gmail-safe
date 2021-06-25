package io.dany.gmail.safe.usecase;

import io.dany.gmail.safe.common.Constants;
import io.dany.gmail.safe.fixture.BackupFixture;
import io.dany.gmail.safe.fixture.MessageFixture;
import io.dany.gmail.safe.kernel.exception.UseCaseException;
import io.dany.gmail.safe.kernel.model.Backup;
import io.dany.gmail.safe.kernel.model.Message;
import io.dany.gmail.safe.kernel.vo.Status;
import io.dany.gmail.safe.mock.BackupRepositoryMock;
import io.dany.gmail.safe.mock.MessageRepositoryMock;
import io.dany.gmail.safe.usecase.port.BackupRepository;
import io.dany.gmail.safe.usecase.port.MessageRepository;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class CreateBackupTest {

    private CreateBackup createBackup;

    @Mock
    private MessageRepository messageRepositoryMock;

    @Mock
    private BackupRepository backupRepositoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createBackup = new CreateBackup(messageRepositoryMock, backupRepositoryMock);
    }

    @Test
    void shouldCreateABackupWithStatusOK() throws InterruptedException {
        Backup backup = BackupFixture.newDefault();
        Message message1 = MessageFixture.newDefault();
        Message message2 = MessageFixture.newDefault();
        ArgumentCaptor<Backup> backupCaptor = ArgumentCaptor.forClass(Backup.class);

        MessageRepositoryMock.whenFindAll(messageRepositoryMock, Future.successful(List.of(message1, message2)));
        BackupRepositoryMock.whenSave(backupRepositoryMock, backupCaptor, Try.success(backup));

        Either<UseCaseException, Backup> result = createBackup.execute();
        ForkJoinPool.commonPool().awaitTermination(2, TimeUnit.SECONDS);

        assertThat(result.isRight(), is(Boolean.TRUE));
        assertThat(result.get(), notNullValue());
        assertThat(result.get().getId(), notNullValue());
        assertThat(result.get().getStatus(), equalTo(Status.IN_PROGRESS));

        List<Status> statuses = List.ofAll(backupCaptor.getAllValues())
                .map(Backup::getStatus);

        assertThat(statuses.size(), equalTo(2));
        assertThat(statuses, containsInAnyOrder(Status.IN_PROGRESS, Status.OK));

        Backup backupOK = List.ofAll(backupCaptor.getAllValues())
                .find(oBackup -> Status.OK.equals(oBackup.getStatus()))
                .get();

        assertThat(backupOK.getMessages().values(), containsInAnyOrder(message1, message2));
        assertThat(backupOK.getLabelsPresent().keySet(), containsInAnyOrder(Constants.LABEL_INBOX, Constants.LABEL_TODO));
        assertThat(backupOK.getLabelsPresent().get(Constants.LABEL_INBOX).get(), containsInAnyOrder(message1.getId(), message2.getId()));
        assertThat(backupOK.getLabelsPresent().get(Constants.LABEL_TODO).get(), containsInAnyOrder(message1.getId(), message2.getId()));
    }

    @Test
    void shouldCreateABackupWithStatusFAILED() throws InterruptedException {
        Backup backup = BackupFixture.newDefault();
        ArgumentCaptor<Backup> backupCaptor = ArgumentCaptor.forClass(Backup.class);

        MessageRepositoryMock.whenFindAll(messageRepositoryMock, Future.failed(new Exception("Message Repository Error")));
        BackupRepositoryMock.whenSave(backupRepositoryMock, backupCaptor, Try.success(backup));

        Either<UseCaseException, Backup> result = createBackup.execute();
        ForkJoinPool.commonPool().awaitTermination(2, TimeUnit.SECONDS);

        assertThat(result.isRight(), is(Boolean.TRUE));
        assertThat(result.get(), notNullValue());
        assertThat(result.get().getId(), notNullValue());
        assertThat(result.get().getStatus(), equalTo(Status.IN_PROGRESS));

        List<Status> statuses = List.ofAll(backupCaptor.getAllValues())
                .map(Backup::getStatus);

        assertThat(statuses.size(), equalTo(2));
        assertThat(statuses, containsInAnyOrder(Status.IN_PROGRESS, Status.FAILED));
    }
}
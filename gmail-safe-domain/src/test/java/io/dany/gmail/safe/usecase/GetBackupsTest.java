package io.dany.gmail.safe.usecase;

import io.dany.gmail.safe.fixture.BackupFixture;
import io.dany.gmail.safe.kernel.exception.UseCaseException;
import io.dany.gmail.safe.kernel.model.Backup;
import io.dany.gmail.safe.mock.BackupRepositoryMock;
import io.dany.gmail.safe.usecase.port.BackupRepository;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

class GetBackupsTest {

    private GetBackups getBackups;

    @Mock
    private BackupRepository backupRepositoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        getBackups = new GetBackups(backupRepositoryMock);
    }

    @Test
    void shouldReturnAllTheBackupsStoredInTheRepository() {
        Backup backup1 = BackupFixture.newDefault();
        Backup backup2 = BackupFixture.newDefault();

        BackupRepositoryMock.whenFindAll(backupRepositoryMock, Try.success(List.of(backup1, backup2)));

        Either<UseCaseException, List<Backup>> result = getBackups.execute();

        assertThat(result.isRight(), is(Boolean.TRUE));
        assertThat(result.get(), contains(backup1, backup2));
    }

    @Test
    void shouldReturnAnException() {
        BackupRepositoryMock.whenFindAll(backupRepositoryMock, Try.failure(new Exception("Backup Repository Error")));

        Either<UseCaseException, List<Backup>> result = getBackups.execute();

        assertThat(result.isLeft(), is(Boolean.TRUE));
        assertThat(result.getLeft(), instanceOf(UseCaseException.class));
    }
}
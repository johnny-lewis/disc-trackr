package dev.johnnylewis.disctrackr.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.github.michaelbull.result.unwrap
import com.google.common.truth.Truth.assertThat
import dev.johnnylewis.disctrackr.data.database.AppDatabase
import dev.johnnylewis.disctrackr.data.database.dao.DiscDao
import dev.johnnylewis.disctrackr.domain.builder.buildDisc
import dev.johnnylewis.disctrackr.util.finishFlow
import dev.johnnylewis.disctrackr.util.okFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DatabaseRepositoryTest {
  private lateinit var database: AppDatabase
  private lateinit var discDao: DiscDao
  private lateinit var repository: DatabaseRepository

  private val testScope = TestScope()

  @Before
  fun before() {
    database = Room.inMemoryDatabaseBuilder(
      context = ApplicationProvider.getApplicationContext(),
      klass = AppDatabase::class.java,
    ).allowMainThreadQueries().build()
    discDao = database.discDao()
    repository = DatabaseRepository(
      appDatabase = database,
      discDao = discDao,
    )
  }

  @After
  fun after() {
    database.close()
  }

  @Test
  fun `Given empty database, when getting all discs, then it returns empty flow`() =
    testScope.runTest {
      repository.getAllDiscs().okFlow().test {
        assertThat(awaitItem()).isEmpty()

        finishFlow()
      }
    }

  @Test
  fun `Given empty database, when inserting a disc, then it inserts and is returned on the get all flow`() =
    testScope.runTest {
      repository.getAllDiscs().okFlow().test {
        assertThat(awaitItem()).isEmpty()

        val disc = buildDisc()
        repository.upsertDisc(disc)
        assertThat(awaitItem()).containsExactly(disc)

        finishFlow()
      }
    }

  @Test
  fun `Given empty database, when inserting a list of discs, then they are inserted and are returned on the get all flow and sorted by name`() =
    testScope.runTest {
      repository.getAllDiscs().okFlow().test {
        assertThat(awaitItem()).isEmpty()

        val discs = listOf(
          buildDisc(id = 1, title = "DISC_1"),
          buildDisc(id = 2, title = "DISC_5"),
          buildDisc(id = 3, title = "DISC_2"),
        )
        repository.addDiscs(discs)

        assertThat(awaitItem()).isEqualTo(discs.sortedBy { it.title })

        finishFlow()
      }
    }

  @Test
  fun `Given database with matching disc, when inserting a disc, then it updates existing disc`() =
    testScope.runTest {
      repository.getAllDiscs().okFlow().test {
        assertThat(awaitItem()).isEmpty()

        val discOld = buildDisc(id = 1, title = "DISC_1")
        repository.upsertDisc(discOld)
        assertThat(awaitItem()).containsExactly(discOld)

        val discNew = discOld.copy(title = "DISC_2")
        repository.upsertDisc(discNew)
        assertThat(awaitItem()).containsExactly(discNew)

        finishFlow()
      }
    }

  @Test
  fun `Given non-empty database, when deleting disc, then get all returns flow without that disc`() =
    testScope.runTest {
      repository.getAllDiscs().okFlow().test {
        assertThat(awaitItem()).isEmpty()

        val discs = listOf(
          buildDisc(id = 1, title = "DISC_1"),
          buildDisc(id = 2, title = "DISC_2"),
        )

        repository.addDiscs(discs)
        assertThat(awaitItem()).isEqualTo(discs)

        repository.deleteDisc(id = 1)
        assertThat(awaitItem()).isEqualTo(discs.filterNot { it.id == 1 })

        finishFlow()
      }
    }

  @Test
  fun `Given empty database, when getting disc, then it returns flow of null`() =
    testScope.runTest {
      with(repository.getDisc(1)) {
        assertThat(isOk).isTrue()
        assertThat(unwrap()).isNull()
      }
    }

  @Test
  fun `Given database with no matching disc, when getting disc, then it returns flow of null`() =
    testScope.runTest {
      repository.upsertDisc(buildDisc(id = 1))
      with(repository.getDisc(2)) {
        assertThat(isOk).isTrue()
        assertThat(unwrap()).isNull()
      }
    }

  @Test
  fun `Given database with matching disc, when getting disc, then it returns flow of that disc`() =
    testScope.runTest {
      val disc = buildDisc(id = 1)
      repository.upsertDisc(disc)
      with(repository.getDisc(1)) {
        assertThat(isOk).isTrue()
        assertThat(unwrap()).isEqualTo(disc)
      }
    }
}

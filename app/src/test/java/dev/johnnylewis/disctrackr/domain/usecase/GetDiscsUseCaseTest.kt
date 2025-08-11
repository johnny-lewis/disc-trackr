package dev.johnnylewis.disctrackr.domain.usecase

import app.cash.turbine.test
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth.assertThat
import dev.johnnylewis.disctrackr.domain.builder.buildBluRayDiscFormat
import dev.johnnylewis.disctrackr.domain.builder.buildDVDDiscFormat
import dev.johnnylewis.disctrackr.domain.builder.buildDisc
import dev.johnnylewis.disctrackr.domain.builder.buildDiscFilter
import dev.johnnylewis.disctrackr.domain.builder.buildEmptyDiscFilter
import dev.johnnylewis.disctrackr.domain.builder.buildUHDDiscFormat
import dev.johnnylewis.disctrackr.domain.model.DiscFormat
import dev.johnnylewis.disctrackr.domain.repository.DatabaseRepositoryContract
import dev.johnnylewis.disctrackr.util.okFlow
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetDiscsUseCaseTest {
  private val mockDatabaseRepository = mockk<DatabaseRepositoryContract>()

  private lateinit var usecase: GetDiscsUseCase

  private val testScope = TestScope()

  @Before
  fun before() {
    clearAllMocks()
    usecase = GetDiscsUseCase(
      databaseRepository = mockDatabaseRepository,
    )
  }

  @Test
  fun `Given error from repository, when getting all discs, then it passes through error`() =
    testScope.runTest {
      coEvery { mockDatabaseRepository.getAllDiscs() } returns Err(Exception())
      usecase(filterFlow = flowOf(buildEmptyDiscFilter())).let { result ->
        assertThat(result.isErr).isTrue()
      }
    }

  @Test
  fun `Given no discs from repository, when getting all discs, then it returns flow of empty list`() =
    testScope.runTest {
      coEvery { mockDatabaseRepository.getAllDiscs() } returns Ok(flowOf(emptyList()))
      usecase(filterFlow = flowOf(buildEmptyDiscFilter())).okFlow().test {
        assertThat(awaitItem()).isEmpty()
        awaitComplete()
      }
    }

  @Test
  fun `Given discs from repository with no filters, when getting all discs, then it returns flow of discs`() =
    testScope.runTest {
      val discs = listOf(
        buildDisc(id = 1, title = "TITLE_1"),
        buildDisc(id = 2, title = "TITLE_2"),
      )
      coEvery { mockDatabaseRepository.getAllDiscs() } returns Ok(flowOf(discs))

      usecase(filterFlow = flowOf(buildEmptyDiscFilter())).okFlow().test {
        assertThat(awaitItem()).isEqualTo(discs)
        awaitComplete()
      }
    }

  @Test
  fun `Given discs from repository and filtering by format, when getting all discs, then it returns flow of filtered discs`() =
    testScope.runTest {
      val discs = listOf(
        buildDisc(id = 1, format = buildBluRayDiscFormat()),
        buildDisc(id = 2, format = buildDVDDiscFormat()),
        buildDisc(id = 3, format = buildUHDDiscFormat()),
      )
      val filter = buildDiscFilter(format = DiscFormat.BluRay::class)
      coEvery { mockDatabaseRepository.getAllDiscs() } returns Ok(flowOf(discs))

      usecase(filterFlow = flowOf(filter)).okFlow().test {
        assertThat(awaitItem()).isEqualTo(listOf(discs.first()))
        awaitComplete()
      }
    }

  @Test
  fun `Given discs from repository and filtering by country code, when getting all discs, then it returns flow of filtered discs`() =
    testScope.runTest {
      val discs = listOf(
        buildDisc(id = 1, countryCode = "au"),
        buildDisc(id = 2, countryCode = "us"),
        buildDisc(id = 3, countryCode = "au"),
      )
      val filter = buildDiscFilter(countryCode = "au")
      coEvery { mockDatabaseRepository.getAllDiscs() } returns Ok(flowOf(discs))

      usecase(filterFlow = flowOf(filter)).okFlow().test {
        assertThat(awaitItem()).isEqualTo(listOf(discs.first(), discs.last()))
        awaitComplete()
      }
    }

  @Test
  fun `Given discs from repository and filtering by distributor, when getting all discs, then it returns flow of filtered discs`() =
    testScope.runTest {
      val discs = listOf(
        buildDisc(id = 1, distributor = "DISTRIBUTOR_1"),
        buildDisc(id = 2, distributor = "DISTRIBUTOR_1"),
        buildDisc(id = 3, distributor = "DISTRIBUTOR_2"),
      )
      val filter = buildDiscFilter(distributor = "DISTRIBUTOR_2")
      coEvery { mockDatabaseRepository.getAllDiscs() } returns Ok(flowOf(discs))

      usecase(filterFlow = flowOf(filter)).okFlow().test {
        assertThat(awaitItem()).isEqualTo(listOf(discs.last()))
        awaitComplete()
      }
    }

  @Test
  fun `Given discs from repository and filtering by format and country code, when getting all discs, then it returns flow of filtered discs`() =
    testScope.runTest {
      val discs = listOf(
        buildDisc(id = 1, format = buildBluRayDiscFormat(), countryCode = "au"),
        buildDisc(id = 2, format = buildBluRayDiscFormat(), countryCode = "us"),
        buildDisc(id = 3, format = buildDVDDiscFormat(), countryCode = "au"),
      )
      val filter = buildDiscFilter(
        format = DiscFormat.BluRay::class,
        countryCode = "au",
      )
      coEvery { mockDatabaseRepository.getAllDiscs() } returns Ok(flowOf(discs))

      usecase(filterFlow = flowOf(filter)).okFlow().test {
        assertThat(awaitItem()).isEqualTo(listOf(discs.first()))
        awaitComplete()
      }
    }

  @Test
  fun `Given discs from repository and filtering by format and distributor, when getting all discs, then it returns flow of filtered discs`() =
    testScope.runTest {
      val discs = listOf(
        buildDisc(id = 1, format = buildBluRayDiscFormat(), distributor = "DISTRIBUTOR_1"),
        buildDisc(id = 2, format = buildBluRayDiscFormat(), distributor = "DISTRIBUTOR_1"),
        buildDisc(id = 3, format = buildDVDDiscFormat(), distributor = "DISTRIBUTOR_2"),
        buildDisc(id = 4, format = buildUHDDiscFormat(), distributor = "DISTRIBUTOR_1"),
      )
      val filter = buildDiscFilter(
        format = DiscFormat.BluRay::class,
        distributor = "DISTRIBUTOR_1",
      )
      coEvery { mockDatabaseRepository.getAllDiscs() } returns Ok(flowOf(discs))

      usecase(filterFlow = flowOf(filter)).okFlow().test {
        assertThat(awaitItem()).isEqualTo(listOf(discs[0], discs[1]))
        awaitComplete()
      }
    }

  @Test
  fun `Given discs from repository and filtering by country code and distributor, when getting all discs, then it returns flow of filtered discs`() =
    testScope.runTest {
      val discs = listOf(
        buildDisc(id = 1, countryCode = "au", distributor = "DISTRIBUTOR_1"),
        buildDisc(id = 2, countryCode = "us", distributor = "DISTRIBUTOR_1"),
        buildDisc(id = 3, countryCode = "au", distributor = "DISTRIBUTOR_1"),
        buildDisc(id = 4, countryCode = "au", distributor = "DISTRIBUTOR_2"),
      )
      val filter = buildDiscFilter(
        countryCode = "au",
        distributor = "DISTRIBUTOR_1",
      )
      coEvery { mockDatabaseRepository.getAllDiscs() } returns Ok(flowOf(discs))

      usecase(filterFlow = flowOf(filter)).okFlow().test {
        assertThat(awaitItem()).isEqualTo(listOf(discs[0], discs[2]))
        awaitComplete()
      }
    }

  @Test
  fun `Given discs from repository and filtering by format, country code and distributor, when getting all discs, then it returns flow of filtered discs`() =
    testScope.runTest {
      val discs = listOf(
        buildDisc(id = 1, format = buildBluRayDiscFormat(), countryCode = "au", distributor = "DISTRIBUTOR_1"),
        buildDisc(id = 2, format = buildBluRayDiscFormat(), countryCode = "us", distributor = "DISTRIBUTOR_1"),
        buildDisc(id = 3, format = buildDVDDiscFormat(), countryCode = "au", distributor = "DISTRIBUTOR_1"),
        buildDisc(id = 4, format = buildUHDDiscFormat(), countryCode = "au", distributor = "DISTRIBUTOR_2"),
        buildDisc(id = 5, format = buildUHDDiscFormat(), countryCode = "us", distributor = "DISTRIBUTOR_2"),
        buildDisc(id = 5, format = buildUHDDiscFormat(), countryCode = "us", distributor = "DISTRIBUTOR_1"),
      )
      val filter = buildDiscFilter(
        format = DiscFormat.UHD::class,
        countryCode = "au",
        distributor = "DISTRIBUTOR_2",
      )
      coEvery { mockDatabaseRepository.getAllDiscs() } returns Ok(flowOf(discs))

      usecase(filterFlow = flowOf(filter)).okFlow().test {
        assertThat(awaitItem()).isEqualTo(listOf(discs[3]))
        awaitComplete()
      }
    }
}

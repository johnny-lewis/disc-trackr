package dev.johnnylewis.disctrackr.presentation.mapper

import com.google.common.truth.Truth.assertThat
import dev.johnnylewis.disctrackr.domain.model.DiscFormat
import dev.johnnylewis.disctrackr.presentation.builder.buildDiscFormResult
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import junitparams.naming.TestCaseName
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class DiscFormResultMapperTest {
  @Test
  fun `Given form result with title with leading and trailing spaces, when mapping to disc, then it maps with trimmed title`() {
    val formResult = buildDiscFormResult(title = " TITLE ")
    with(formResult.mapToDisc()) {
      assertThat(title).isEqualTo("TITLE")
    }
  }

  @Test
  fun `Given form result with no country, when mapping to disc, then it maps with null country code`() {
    val formResult = buildDiscFormResult(country = null)
    with(formResult.mapToDisc()) {
      assertThat(countryCode).isNull()
    }
  }

  @Test
  fun `Given form result with empty distributor, when mapping to disc, then it maps with null distributor`() {
    val formResult = buildDiscFormResult(distributor = "")
    with(formResult.mapToDisc()) {
      assertThat(distributor).isNull()
    }
  }

  @Test
  fun `Given form result with non-numeric year, when mapping to disc, then it maps with null year`() {
    val formResult = buildDiscFormResult(year = "NOT A NUMBER")
    with(formResult.mapToDisc()) {
      assertThat(year).isNull()
    }
  }

  @Test
  fun `Given form result with empty bluray id, when mapping to disc, then it maps with null bluray id`() {
    val formResult = buildDiscFormResult(blurayId = "")
    with(formResult.mapToDisc()) {
      assertThat(blurayId).isNull()
    }
  }

  @Test
  fun `Given form result with uhd format, when mapping disc, then it maps`() {
    val formResult = buildDiscFormResult(format = DiscFormResult.DiscFormFormat.UHD)
    with(formResult.mapToDisc()) {
      assertThat(id).isNull()
      assertThat(title).isEqualTo(formResult.title)
      assertThat(imageUrl).isNull()
      assertThat(format).isEqualTo(DiscFormat.UHD)
      assertThat(countryCode).isEqualTo(formResult.country?.code)
      assertThat(distributor).isEqualTo(formResult.distributor)
      assertThat(year).isEqualTo(formResult.year.toInt())
      assertThat(blurayId).isEqualTo(formResult.blurayId)
    }
  }

  @Test
  @Parameters(method = "dvdParameters")
  @TestCaseName("Given form result with dvd regions {0}, when mapping to disc, then it maps with dvd regions {1}")
  fun testMapToDiscDvd(
    formRegions: List<DiscFormResult.DiscFormRegion>,
    domainRegions: List<DiscFormat.DVD.Region>,
  ) {
    val formResult = buildDiscFormResult(
      format = DiscFormResult.DiscFormFormat.DVD,
      regions = formRegions,
    )
    with(formResult.mapToDisc()) {
      assertThat(id).isNull()
      assertThat(title).isEqualTo(formResult.title)
      assertThat(imageUrl).isNull()
      assertThat(format).isEqualTo(DiscFormat.DVD(regions = domainRegions))
      assertThat(countryCode).isEqualTo(formResult.country?.code)
      assertThat(distributor).isEqualTo(formResult.distributor)
      assertThat(year).isEqualTo(formResult.year.toInt())
      assertThat(blurayId).isEqualTo(formResult.blurayId)
    }
  }

  @Test
  @Parameters(method = "bluRayParameters")
  @TestCaseName("Given form result with blu-ray regions {0}, when mapping to disc, then it maps with bluRay regions {1}")
  fun testMapToDiscBluRay(
    formRegions: List<DiscFormResult.DiscFormRegion>,
    domainRegions: List<DiscFormat.BluRay.Region>,
  ) {
    val formResult = buildDiscFormResult(
      format = DiscFormResult.DiscFormFormat.BLU_RAY,
      regions = formRegions,
    )
    with(formResult.mapToDisc()) {
      assertThat(id).isNull()
      assertThat(title).isEqualTo(formResult.title)
      assertThat(imageUrl).isNull()
      assertThat(format).isEqualTo(DiscFormat.BluRay(regions = domainRegions))
      assertThat(countryCode).isEqualTo(formResult.country?.code)
      assertThat(distributor).isEqualTo(formResult.distributor)
      assertThat(year).isEqualTo(formResult.year.toInt())
      assertThat(blurayId).isEqualTo(formResult.blurayId)
    }
  }

  @Suppress("unused")
  private fun dvdParameters(): Array<Any> =
    arrayOf(
      arrayOf(listOf(DiscFormResult.DiscFormRegion.ONE), listOf(DiscFormat.DVD.Region.ONE)),
      arrayOf(listOf(DiscFormResult.DiscFormRegion.TWO), listOf(DiscFormat.DVD.Region.TWO)),
      arrayOf(listOf(DiscFormResult.DiscFormRegion.THREE), listOf(DiscFormat.DVD.Region.THREE)),
      arrayOf(listOf(DiscFormResult.DiscFormRegion.FOUR), listOf(DiscFormat.DVD.Region.FOUR)),
      arrayOf(listOf(DiscFormResult.DiscFormRegion.FIVE), listOf(DiscFormat.DVD.Region.FIVE)),
      arrayOf(listOf(DiscFormResult.DiscFormRegion.SIX), listOf(DiscFormat.DVD.Region.SIX)),
      arrayOf(listOf(DiscFormResult.DiscFormRegion.ZERO), listOf(DiscFormat.DVD.Region.ALL)),
      arrayOf(
        listOf(
          DiscFormResult.DiscFormRegion.ONE,
          DiscFormResult.DiscFormRegion.THREE,
          DiscFormResult.DiscFormRegion.FOUR,
          DiscFormResult.DiscFormRegion.FIVE,
        ),
        listOf(
          DiscFormat.DVD.Region.ONE,
          DiscFormat.DVD.Region.THREE,
          DiscFormat.DVD.Region.FOUR,
          DiscFormat.DVD.Region.FIVE,
        )
      ),
      arrayOf(
        listOf(
          DiscFormResult.DiscFormRegion.ONE,
          DiscFormResult.DiscFormRegion.TWO,
          DiscFormResult.DiscFormRegion.THREE,
          DiscFormResult.DiscFormRegion.FOUR,
          DiscFormResult.DiscFormRegion.FIVE,
          DiscFormResult.DiscFormRegion.SIX,
        ),
        listOf(DiscFormat.DVD.Region.ALL)
      ),
      // Invalid region
      arrayOf(listOf(DiscFormResult.DiscFormRegion.A), listOf()),
    )

  @Suppress("unused")
  private fun bluRayParameters(): Array<Any> =
    arrayOf(
      arrayOf(listOf(DiscFormResult.DiscFormRegion.A), listOf(DiscFormat.BluRay.Region.A)),
      arrayOf(listOf(DiscFormResult.DiscFormRegion.B), listOf(DiscFormat.BluRay.Region.B)),
      arrayOf(listOf(DiscFormResult.DiscFormRegion.C), listOf(DiscFormat.BluRay.Region.C)),
      arrayOf(listOf(DiscFormResult.DiscFormRegion.ALL), listOf(DiscFormat.BluRay.Region.ALL)),
      arrayOf(
        listOf(
          DiscFormResult.DiscFormRegion.A,
          DiscFormResult.DiscFormRegion.B,
        ),
        listOf(
          DiscFormat.BluRay.Region.A,
          DiscFormat.BluRay.Region.B,
        )
      ),
      arrayOf(
        listOf(
          DiscFormResult.DiscFormRegion.A,
          DiscFormResult.DiscFormRegion.C,
        ),
        listOf(
          DiscFormat.BluRay.Region.A,
          DiscFormat.BluRay.Region.C,
        )
      ),
      arrayOf(
        listOf(
          DiscFormResult.DiscFormRegion.B,
          DiscFormResult.DiscFormRegion.C,
        ),
        listOf(
          DiscFormat.BluRay.Region.B,
          DiscFormat.BluRay.Region.C
        )
      ),
      arrayOf(
        listOf(
          DiscFormResult.DiscFormRegion.A,
          DiscFormResult.DiscFormRegion.B,
          DiscFormResult.DiscFormRegion.C,
        ),
        listOf(DiscFormat.BluRay.Region.ALL)
      ),
      // Invalid region
      arrayOf(listOf(DiscFormResult.DiscFormRegion.ONE), listOf()),
    )
}

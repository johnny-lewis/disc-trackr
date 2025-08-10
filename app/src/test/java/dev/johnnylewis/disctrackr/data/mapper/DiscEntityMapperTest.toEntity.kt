package dev.johnnylewis.disctrackr.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.johnnylewis.disctrackr.domain.builder.buildBluRayDiscFormat
import dev.johnnylewis.disctrackr.domain.builder.buildDVDDiscFormat
import dev.johnnylewis.disctrackr.domain.builder.buildDisc
import dev.johnnylewis.disctrackr.domain.builder.buildUHDDiscFormat
import dev.johnnylewis.disctrackr.domain.model.DiscFormat
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import junitparams.naming.TestCaseName
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.Any

@RunWith(JUnitParamsRunner::class)
class DiscEntityMapperTestToEntity {
  @Test
  @Parameters(method = "dvdParameters")
  @TestCaseName("Given dvd disc with region {0}, when mapping to disc entity, then it maps to disc entity with regions {1}")
  fun testMapToEntityDvd(
    domainRegions: List<DiscFormat.DVD.Region>,
    entityRegions: String,
  ) {
    val disc = buildDisc(format = buildDVDDiscFormat(domainRegions))
    with(disc.mapToDiscEntity()) {
      assertThat(id).isEqualTo(disc.id)
      assertThat(title).isEqualTo(disc.title)
      assertThat(imageUrl).isEqualTo(disc.imageUrl)
      assertThat(format).isEqualTo("dvd")
      assertThat(region).isEqualTo(entityRegions)
      assertThat(countryCode).isEqualTo(disc.countryCode)
      assertThat(distributor).isEqualTo(disc.distributor)
      assertThat(year).isEqualTo(disc.year)
      assertThat(blurayId).isEqualTo(disc.blurayId)
    }
  }

  @Test
  @Parameters(method = "bluRayParameters")
  @TestCaseName("Given blu-ray disc with region {0}, when mapping to disc entity, then it maps to disc entity with regions {1}")
  fun testMapToEntityBluRay(
    domainRegions: List<DiscFormat.BluRay.Region>,
    entityRegions: String,
  ) {
    val disc = buildDisc(format = buildBluRayDiscFormat(domainRegions))
    with(disc.mapToDiscEntity()) {
      assertThat(id).isEqualTo(disc.id)
      assertThat(title).isEqualTo(disc.title)
      assertThat(imageUrl).isEqualTo(disc.imageUrl)
      assertThat(format).isEqualTo("br")
      assertThat(region).isEqualTo(entityRegions)
      assertThat(countryCode).isEqualTo(disc.countryCode)
      assertThat(distributor).isEqualTo(disc.distributor)
      assertThat(year).isEqualTo(disc.year)
      assertThat(blurayId).isEqualTo(disc.blurayId)
    }
  }

  @Test
  fun `Given disc with uhd format, when mapping to disc entity, then it maps to disc entity with format uhd`() {
    val disc = buildDisc(format = buildUHDDiscFormat())
    with(disc.mapToDiscEntity()) {
      assertThat(id).isEqualTo(disc.id)
      assertThat(title).isEqualTo(disc.title)
      assertThat(imageUrl).isEqualTo(disc.imageUrl)
      assertThat(format).isEqualTo("uhd")
      assertThat(region).isNull()
      assertThat(countryCode).isEqualTo(disc.countryCode)
      assertThat(distributor).isEqualTo(disc.distributor)
      assertThat(year).isEqualTo(disc.year)
      assertThat(blurayId).isEqualTo(disc.blurayId)
    }
  }

  @Test
  @Parameters(method = "ignoredWordsParameters")
  @TestCaseName("Given disc with title starting with '{0}', when mapping to disc entity, then it maps to disc entity with title '{1}'")
  fun testMapToEntityTitleSort(
    titleStart: String,
    titleSort: String,
  ) {
    val disc = buildDisc(title = "$titleStart name")
    with(disc.mapToDiscEntity()) {
      assertThat(titleSort).isEqualTo(titleSort)
    }
  }

  @Suppress("unused")
  private fun dvdParameters(): Array<Any> = arrayOf(
    arrayOf(emptyList<DiscFormat.DVD.Region>(), ""),
    arrayOf(listOf(DiscFormat.DVD.Region.ONE), "one"),
    arrayOf(listOf(DiscFormat.DVD.Region.TWO), "two"),
    arrayOf(listOf(DiscFormat.DVD.Region.THREE), "three"),
    arrayOf(listOf(DiscFormat.DVD.Region.FOUR), "four"),
    arrayOf(listOf(DiscFormat.DVD.Region.FIVE), "five"),
    arrayOf(listOf(DiscFormat.DVD.Region.SIX), "six"),
    arrayOf(listOf(DiscFormat.DVD.Region.ONE, DiscFormat.DVD.Region.TWO, DiscFormat.DVD.Region.THREE, DiscFormat.DVD.Region.FOUR, DiscFormat.DVD.Region.FIVE), "one,two,three,four,five"),
    arrayOf(listOf(DiscFormat.DVD.Region.ALL), "all"),
  )

  @Suppress("unused")
  private fun bluRayParameters(): Array<Any> = arrayOf(
    arrayOf(emptyList<DiscFormat.BluRay.Region>(), ""),
    arrayOf(listOf(DiscFormat.BluRay.Region.A), "a"),
    arrayOf(listOf(DiscFormat.BluRay.Region.B), "b"),
    arrayOf(listOf(DiscFormat.BluRay.Region.C), "c"),
    arrayOf(listOf(DiscFormat.BluRay.Region.A, DiscFormat.BluRay.Region.B), "a,b"),
    arrayOf(listOf(DiscFormat.BluRay.Region.A, DiscFormat.BluRay.Region.C), "a,c"),
    arrayOf(listOf(DiscFormat.BluRay.Region.B, DiscFormat.BluRay.Region.C), "b,c"),
    arrayOf(listOf(DiscFormat.BluRay.Region.ALL), "all"),
  )

  @Suppress("unused")
  private fun ignoredWordsParameters(): Array<Any> = arrayOf(
    arrayOf("the", "name, the"),
    arrayOf("a", "name, a"),
    arrayOf("an", "name, an"),
    arrayOf("of", "name, of"),
  )
}

package dev.johnnylewis.disctrackr.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.johnnylewis.disctrackr.data.builder.buildDiscEntity
import dev.johnnylewis.disctrackr.data.database.entity.DiscEntity
import dev.johnnylewis.disctrackr.domain.model.DiscFormat
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import junitparams.naming.TestCaseName
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class DiscEntityMapperTestToDisc {
  @Test
  @Parameters(method = "dvdParameters")
  @TestCaseName("Given dvd disc entity with region {0}, when mapping to disc, then it maps to disc with regions {1}")
  fun testMapToDiscDvd(
    entityRegions: String?,
    domainRegions: List<DiscFormat.DVD.Region>,
  ) {
    val entity = buildDiscEntity(format = "dvd", region = entityRegions)
    with(entity.mapToDisc()) {
      assertThat(id).isEqualTo(entity.id)
      assertThat(title).isEqualTo(entity.title)
      assertThat(imageUrl).isEqualTo(entity.imageUrl)
      assertThat(format).isEqualTo(DiscFormat.DVD(regions = domainRegions))
      assertThat(countryCode).isEqualTo(entity.countryCode)
      assertThat(distributor).isEqualTo(entity.distributor)
      assertThat(year).isEqualTo(entity.year)
      assertThat(blurayId).isEqualTo(entity.blurayId)
    }
  }

  @Test
  @Parameters(method = "bluRayParameters")
  @TestCaseName("Given blu-ray disc entity with region {0}, when mapping to disc, then it maps to disc with regions {1}")
  fun testMapToDiscBluRay(
    entityRegions: String?,
    domainRegions: List<DiscFormat.BluRay.Region>,
  ) {
    val entity = buildDiscEntity(format = "br", region = entityRegions)
    with(entity.mapToDisc()) {
      assertThat(id).isEqualTo(entity.id)
      assertThat(title).isEqualTo(entity.title)
      assertThat(imageUrl).isEqualTo(entity.imageUrl)
      assertThat(format).isEqualTo(DiscFormat.BluRay(regions = domainRegions))
      assertThat(countryCode).isEqualTo(entity.countryCode)
      assertThat(distributor).isEqualTo(entity.distributor)
      assertThat(year).isEqualTo(entity.year)
      assertThat(blurayId).isEqualTo(entity.blurayId)
    }
  }

  @Test
  fun `Given uhd disc entity, when mapping to disc, then it maps to disc`() {
    val entity = buildDiscEntity(format = "uhd")
    with(entity.mapToDisc()) {
      assertThat(id).isEqualTo(entity.id)
      assertThat(title).isEqualTo(entity.title)
      assertThat(imageUrl).isEqualTo(entity.imageUrl)
      assertThat(format).isEqualTo(DiscFormat.UHD)
      assertThat(countryCode).isEqualTo(entity.countryCode)
      assertThat(distributor).isEqualTo(entity.distributor)
      assertThat(year).isEqualTo(entity.year)
      assertThat(blurayId).isEqualTo(entity.blurayId)
    }
  }

  @Test
  fun `Given disc entity with invalid format, when mapping to disc, then it throws exception`() {
    val entity = buildDiscEntity(format = "INVALID")
    assertThrows(/* expectedThrowable = */ Exception::class.java) {
      entity.mapToDisc()
    }
  }

  @Test
  fun `Given empty list of disc entities, when mapping to discs, then it returns empty list`() {
    val results = emptyList<DiscEntity>().mapToDisc()
    assertThat(results).isEmpty()
  }

  @Test
  fun `Given list of disc entities, when mapping to discs, then it maps`() {
    val entities = listOf(
      buildDiscEntity(id = 1),
      buildDiscEntity(id = 2),
      buildDiscEntity(id = 3),
    )
    with(entities.mapToDisc()) {
      assertThat(size).isEqualTo(3)
      assertThat(get(0).id).isEqualTo(1)
      assertThat(get(1).id).isEqualTo(2)
      assertThat(get(2).id).isEqualTo(3)
    }
  }

  @Suppress("unused")
  private fun dvdParameters(): Array<Any?> {
    return arrayOf(
      arrayOf<Any?>(null, emptyList<DiscFormat.DVD.Region>()),
      arrayOf<Any?>("", emptyList<DiscFormat.DVD.Region>()),
      arrayOf<Any?>("one", listOf(DiscFormat.DVD.Region.ONE)),
      arrayOf<Any?>("two", listOf(DiscFormat.DVD.Region.TWO)),
      arrayOf<Any?>("three", listOf(DiscFormat.DVD.Region.THREE)),
      arrayOf<Any?>("four", listOf(DiscFormat.DVD.Region.FOUR)),
      arrayOf<Any?>("five", listOf(DiscFormat.DVD.Region.FIVE)),
      arrayOf<Any?>("six", listOf(DiscFormat.DVD.Region.SIX)),
      arrayOf<Any?>("one,two,three,four,five", listOf(DiscFormat.DVD.Region.ONE, DiscFormat.DVD.Region.TWO, DiscFormat.DVD.Region.THREE, DiscFormat.DVD.Region.FOUR, DiscFormat.DVD.Region.FIVE)),
      arrayOf<Any?>("one,two,three,four,five,six", listOf(DiscFormat.DVD.Region.ALL)),
      arrayOf<Any?>("all", listOf(DiscFormat.DVD.Region.ALL)),
    )
  }

  @Suppress("unused")
  private fun bluRayParameters(): Array<Any?> {
    return arrayOf(
      arrayOf<Any?>(null, emptyList<DiscFormat.BluRay.Region>()),
      arrayOf<Any?>("", emptyList<DiscFormat.BluRay.Region>()),
      arrayOf<Any?>("a", listOf(DiscFormat.BluRay.Region.A)),
      arrayOf<Any?>("b", listOf(DiscFormat.BluRay.Region.B)),
      arrayOf<Any?>("c", listOf(DiscFormat.BluRay.Region.C)),
      arrayOf<Any?>("a,b", listOf(DiscFormat.BluRay.Region.A, DiscFormat.BluRay.Region.B)),
      arrayOf<Any?>("a,c", listOf(DiscFormat.BluRay.Region.A, DiscFormat.BluRay.Region.C)),
      arrayOf<Any?>("b,c", listOf(DiscFormat.BluRay.Region.B, DiscFormat.BluRay.Region.C)),
      arrayOf<Any?>("a,b,c", listOf(DiscFormat.BluRay.Region.ALL)),
      arrayOf<Any?>("all", listOf(DiscFormat.BluRay.Region.ALL)),
    )
  }
}

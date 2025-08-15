package dev.johnnylewis.disctrackr.presentation.util

import com.google.common.truth.Truth.assertThat
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import junitparams.naming.TestCaseName
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class DiscFormResultUtilTest {
  @Test
  @Parameters(method = "hasRegionsParameters")
  @TestCaseName("Given disc form format {0}, when checking if it has regions, then it returns {1}")
  fun testHasRegions(
    format: DiscFormResult.DiscFormFormat,
    expected: Boolean,
  ) {
    assertThat(format.hasRegions()).isEqualTo(expected)
  }

  @Test
  @Parameters(method = "getRegionsParameters")
  @TestCaseName("Given disc form format {0}, when getting regions, then it returns {1}")
  fun testGetRegions(
    format: DiscFormResult.DiscFormFormat,
    expected: List<DiscFormResult.DiscFormRegion>,
  ) {
    assertThat(format.getRegions()).containsExactlyElementsIn(expected)
  }

  @Test
  @Parameters(method = "isAllRegionsParameters")
  @TestCaseName("Given disc form region {0}, when checking if it is all regions, then it returns {1}")
  fun testIsAllRegions(
    region: DiscFormResult.DiscFormRegion,
    expected: Boolean,
  ) {
    assertThat(region.isAllRegions()).isEqualTo(expected)
  }

  @Suppress("unused")
  private fun hasRegionsParameters(): Array<Any> =
    arrayOf(
      arrayOf<Any>(DiscFormResult.DiscFormFormat.DVD, true),
      arrayOf<Any>(DiscFormResult.DiscFormFormat.BLU_RAY, true),
      arrayOf<Any>(DiscFormResult.DiscFormFormat.UHD, false),
    )

  @Suppress("unused")
  private fun getRegionsParameters(): Array<Any> =
    arrayOf(
      arrayOf(
        DiscFormResult.DiscFormFormat.DVD,
        listOf(
          DiscFormResult.DiscFormRegion.ZERO,
          DiscFormResult.DiscFormRegion.ONE,
          DiscFormResult.DiscFormRegion.TWO,
          DiscFormResult.DiscFormRegion.THREE,
          DiscFormResult.DiscFormRegion.FOUR,
          DiscFormResult.DiscFormRegion.FIVE,
          DiscFormResult.DiscFormRegion.SIX,
        ),
      ),
      arrayOf(
        DiscFormResult.DiscFormFormat.BLU_RAY,
        listOf(
          DiscFormResult.DiscFormRegion.ALL,
          DiscFormResult.DiscFormRegion.A,
          DiscFormResult.DiscFormRegion.B,
          DiscFormResult.DiscFormRegion.C,
        ),
      ),
      arrayOf(
        DiscFormResult.DiscFormFormat.UHD,
        emptyList<DiscFormResult.DiscFormRegion>(),
      ),
    )

  @Suppress("unused")
  private fun isAllRegionsParameters(): Array<Any> =
    arrayOf(
      arrayOf<Any>(DiscFormResult.DiscFormRegion.ZERO, true),
      arrayOf<Any>(DiscFormResult.DiscFormRegion.ONE, false),
      arrayOf<Any>(DiscFormResult.DiscFormRegion.TWO, false),
      arrayOf<Any>(DiscFormResult.DiscFormRegion.THREE, false),
      arrayOf<Any>(DiscFormResult.DiscFormRegion.FOUR, false),
      arrayOf<Any>(DiscFormResult.DiscFormRegion.FIVE, false),
      arrayOf<Any>(DiscFormResult.DiscFormRegion.SIX, false),
      arrayOf<Any>(DiscFormResult.DiscFormRegion.ALL, true),
      arrayOf<Any>(DiscFormResult.DiscFormRegion.A, false),
      arrayOf<Any>(DiscFormResult.DiscFormRegion.B, false),
      arrayOf<Any>(DiscFormResult.DiscFormRegion.C, false),
    )
}

package dev.johnnylewis.disctrackr.presentation.util

import com.google.common.truth.Truth.assertThat
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import junitparams.naming.TestCaseName
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class StringUtilTest {
  @Test
  @Parameters(method = "isOnlyDigitsParameters")
  @TestCaseName("Given string {0}, when checking if it is only digits, then it returns {1}")
  fun testIsOnlyDigits(
    string: String,
    expected: Boolean,
  ) {
    assertThat(string.isOnlyDigits()).isEqualTo(expected)
  }

  @Suppress("unused")
  private fun isOnlyDigitsParameters(): Array<Any> =
    arrayOf(
      arrayOf<Any>("", false),
      arrayOf<Any>("TEXT_ONLY", false),
      arrayOf<Any>("TEXT_WITH_NUMBERS 1234", false),
      arrayOf<Any>("1234", true),
    )
}

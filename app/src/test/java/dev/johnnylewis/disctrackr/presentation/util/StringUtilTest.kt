package dev.johnnylewis.disctrackr.presentation.util

import com.google.common.truth.Truth.assertThat
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import junitparams.naming.TestCaseName
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner

@RunWith(Enclosed::class)
class StringUtilTest {
  @RunWith(JUnitParamsRunner::class)
  class IsDigitsOnlyTest {
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

  @RunWith(ParameterizedRobolectricTestRunner::class)
  class IsValidUrlTest(
    private val string: String,
    private val expected: Boolean,
  ) {
    @Test
    fun testIsValidUrl() {
      assertThat(string.isValidUrl()).isEqualTo(expected)
    }

    companion object {
      @Suppress("unused")
      @JvmStatic
      @ParameterizedRobolectricTestRunner.Parameters(name = "Given string {0}, when checking if it is a valid url, then it returns {1}")
      fun params() = arrayOf(
        arrayOf<Any>("", false),
        arrayOf<Any>("TEXT_ONLY", false),
        arrayOf<Any>("TEXT_WITH_NUMBERS 1234", false),
        arrayOf<Any>("http://example.com", true),
        arrayOf<Any>("https://example.com", true),
        arrayOf<Any>("http://example.com/", true),
        arrayOf<Any>("https://example.com/", true),
        arrayOf<Any>("http://example.com/path", true),
        arrayOf<Any>("https://example.com/path", true),
        arrayOf<Any>("http://example.com/path/", true),
        arrayOf<Any>("https://example.com/path/", true),
      )
    }
  }
}

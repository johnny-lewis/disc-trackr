package dev.johnnylewis.disctrackr.data.repository

import android.content.Context
import android.content.Intent
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExternalAppRepositoryTest {
  private val context: Context = mockk()

  private lateinit var repository: ExternalAppRepository

  @Before
  fun before() {
    clearAllMocks()
    repository = ExternalAppRepository(
      context = context,
    )
  }

  @Test
  fun `Given some url is provided, when opening web link in external app, then it starts intent with url`() {
    every { context.startActivity(any()) } answers { }

    repository.openWebLink("SOME_URL")

    verify {
      context.startActivity(match { intent: Intent ->
        intent.data?.toString() == "SOME_URL"
      })
    }
  }
}

package dev.johnnylewis.disctrackr.presentation.util

import com.google.common.truth.Truth.assertThat
import dev.johnnylewis.disctrackr.domain.builder.buildDisc
import org.junit.Test

class DiscUtilTest {
  @Test
  fun `Given disc with no image url or blu-ray id, when getting image url, then it returns null`() {
    val disc = buildDisc(imageUrl = null, blurayId = null)

    assertThat(disc.getImageUrl()).isNull()
  }

  @Test
  fun `Given disc with no image url but has blu-ray id, when getting image url, then it returns the blu-ray(dot)com image url`() {
    val disc = buildDisc(imageUrl = null, blurayId = "BLURAY_ID")

    assertThat(disc.getImageUrl()).isEqualTo(
      "https://images.static-bluray.com/movies/covers/BLURAY_ID_front.jpg"
    )
  }

  @Test
  fun `Given disc with image url but no blu-ray id, when getting image url, then it returns the image url`() {
    val disc = buildDisc(imageUrl = "IMAGE_URL", blurayId = null)

    assertThat(disc.getImageUrl()).isEqualTo("IMAGE_URL")
  }

  @Test
  fun `Given disc with both image url and blu-ray id, when getting image url, then it returns the image url`() {
    val disc = buildDisc(imageUrl = "IMAGE_URL", blurayId = "BLURAY_ID")

    assertThat(disc.getImageUrl()).isEqualTo("IMAGE_URL")
  }
}

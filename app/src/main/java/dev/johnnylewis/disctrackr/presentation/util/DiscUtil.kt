package dev.johnnylewis.disctrackr.presentation.util

import dev.johnnylewis.disctrackr.domain.model.Disc

private const val BLU_RAY_IMAGE_URL =
  "https://images.static-bluray.com/movies/covers/{{id}}_front.jpg"

fun List<Disc>.isLastIndex(id: Int?): Boolean =
  indexOfLast { it.id == id } == indices.last

fun Disc.getImageUrl(): String? =
  imageUrl ?: blurayId?.let {
    BLU_RAY_IMAGE_URL.replace("{{id}}", blurayId)
  }

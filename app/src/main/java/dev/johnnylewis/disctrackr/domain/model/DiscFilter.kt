package dev.johnnylewis.disctrackr.domain.model

import kotlin.reflect.KClass

data class DiscFilter(
  val format: KClass<out DiscFormat>? = null,
  val countryCode: String? = null,
  val distributor: String? = null,
)

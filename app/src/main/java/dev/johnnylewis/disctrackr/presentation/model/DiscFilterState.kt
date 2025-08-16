package dev.johnnylewis.disctrackr.presentation.model

import dev.johnnylewis.disctrackr.domain.model.DiscFormat
import kotlin.reflect.KClass

data class DiscFilterState(
  val selection: Selection = Selection(),
  val options: Options = Options(),
) {
  data class Selection(
    val format: KClass<out DiscFormat>? = null,
    val country: Country? = null,
    val distributor: String? = null,
  )
  data class Options(
    val formats: List<KClass<out DiscFormat>> = emptyList(),
    val countries: List<Country> = emptyList(),
    val distributors: List<String> = emptyList(),
  )
}

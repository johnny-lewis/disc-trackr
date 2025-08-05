package dev.johnnylewis.disctrackr.presentation.model

data class DiscFilterState(
  val selection: Selection = Selection(),
  val options: Options = Options(),
) {
  data class Selection(
    val format: DiscFormat? = null,
    val country: Country? = null,
    val distributor: String? = null,
  )
  data class Options(
    val formats: List<DiscFormat> = emptyList(),
    val countries: List<Country> = emptyList(),
    val distributors: List<String> = emptyList(),
  )
}

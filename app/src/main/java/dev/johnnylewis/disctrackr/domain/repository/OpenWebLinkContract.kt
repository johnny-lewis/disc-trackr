package dev.johnnylewis.disctrackr.domain.repository

fun interface OpenWebLinkContract {
  fun openWebLink(url: String)

  companion object {
    operator fun OpenWebLinkContract.invoke(url: String) = openWebLink(url)
  }
}

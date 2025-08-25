package dev.johnnylewis.disctrackr.domain.contract

fun interface OpenWebLinkContract {
  fun openWebLinkExternally(url: String)

  companion object {
    operator fun OpenWebLinkContract.invoke(url: String) = openWebLinkExternally(url)
  }
}

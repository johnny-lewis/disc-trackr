package dev.johnnylewis.disctrackr.domain.repository

import dev.johnnylewis.disctrackr.domain.contract.OpenWebLinkContract

interface ExternalAppRepositoryContract : OpenWebLinkContract {
  fun openWebLink(url: String)

  override fun openWebLinkExternally(url: String) =
    openWebLink(url)
}

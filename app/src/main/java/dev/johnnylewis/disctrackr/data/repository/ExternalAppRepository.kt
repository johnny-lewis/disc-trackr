package dev.johnnylewis.disctrackr.data.repository

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import dev.johnnylewis.disctrackr.domain.repository.OpenWebLinkContract

class ExternalAppRepository(
  private val context: Context,
) : OpenWebLinkContract {
  override fun openWebLink(url: String) {
    val intent: Intent = Intent(Intent.ACTION_VIEW).apply {
      data = url.toUri()
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
    }
    context.startActivity(intent)
  }
}

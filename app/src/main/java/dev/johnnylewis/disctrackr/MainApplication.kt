package dev.johnnylewis.disctrackr

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    initLogging()
  }

  private fun initLogging() {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}

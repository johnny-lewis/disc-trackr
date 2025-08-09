import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ktlint)
  alias(libs.plugins.kover)
}

android {
  namespace = "dev.johnnylewis.disctrackr"
  compileSdk = 36

  defaultConfig {
    applicationId = "dev.johnnylewis.disctrackr"
    minSdk = 30
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    javaCompileOptions {
      annotationProcessorOptions {
        arguments.put("room.schemaLocation", "$projectDir/schemas")
      }
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlin {
    compilerOptions {
      jvmTarget = JvmTarget.JVM_11
    }
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
}

ktlint {
  android = true
  ignoreFailures = false
  version.set("0.47.1")
  reporters {
    reporter(ReporterType.JSON)
    reporter(ReporterType.CHECKSTYLE)
  }
}

dependencies {
  // ktlint
  ktlintRuleset(libs.ktlint.compose)

  // KTX
  implementation(libs.androidx.core.ktx)

  // Jetpack Compose
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.material3)
  implementation(libs.androidx.google.fonts)
  implementation(libs.androidx.navigation)
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  debugImplementation(libs.androidx.ui.tooling)

  // Hilt
  implementation(libs.hilt)
  ksp(libs.hilt.compiler)

  // Timber
  implementation(libs.timber)

  // Serialization
  implementation(libs.kotlin.serialization)

  // Room
  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  ksp(libs.room.compiler)

  // Result Monad
  implementation(libs.result.monad)
  implementation(libs.result.monad.coroutines)

  // Coil
  implementation(libs.coil)
  implementation(libs.coil.network)

  // Testing
  testImplementation(libs.junit)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.robolectric)
  testImplementation(libs.mockk)
  testImplementation(libs.junit.params)
}

package dev.johnnylewis.disctrackr.util

import app.cash.turbine.TurbineTestContext
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.unwrap
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow

fun <T> Result<Flow<T>, *>.okFlow(): Flow<T> = let {
  assertThat(isOk).isTrue()
  unwrap()
}

suspend fun TurbineTestContext<*>.finishFlow() {
  cancel()
  ensureAllEventsConsumed()
}

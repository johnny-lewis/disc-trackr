package dev.johnnylewis.disctrackr.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

fun <T1, T2> Flow<List<T1>>.filterWith(
  other: Flow<T2>,
  predicate: (T1, T2) -> Boolean,
): Flow<List<T1>> =
  combine(other) { t1, t2 ->
    t1.filter { predicate(it, t2) }
  }

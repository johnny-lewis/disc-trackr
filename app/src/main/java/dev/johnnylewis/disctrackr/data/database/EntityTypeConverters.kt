package dev.johnnylewis.disctrackr.data.database

import androidx.room.TypeConverter
import java.time.Instant

class EntityTypeConverters {
  @TypeConverter
  fun fromInstant(instant: Instant): Long =
    instant.epochSecond

  @TypeConverter
  fun toInstant(epochSecond: Long): Instant =
    Instant.ofEpochSecond(epochSecond)
}

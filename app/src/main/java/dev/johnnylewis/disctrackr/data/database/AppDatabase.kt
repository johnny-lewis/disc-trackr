package dev.johnnylewis.disctrackr.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.johnnylewis.disctrackr.data.database.dao.DiscDao
import dev.johnnylewis.disctrackr.data.database.entity.DiscEntity

@Database(
  entities = [
    DiscEntity::class,
  ],
  version = 1,
)
@TypeConverters(EntityTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun discDao(): DiscDao

  companion object {
    private const val DATABASE_NAME = "disc-trackr-database.db3"

    fun build(applicationContext: Context): AppDatabase =
      Room.databaseBuilder(
        context = applicationContext,
        klass = AppDatabase::class.java,
        name = DATABASE_NAME,
      ).build()
  }
}

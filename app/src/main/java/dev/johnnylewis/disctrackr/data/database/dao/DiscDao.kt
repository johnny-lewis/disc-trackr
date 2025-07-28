package dev.johnnylewis.disctrackr.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import dev.johnnylewis.disctrackr.data.database.entity.DISC_TABLE_NAME
import dev.johnnylewis.disctrackr.data.database.entity.DiscEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiscDao {
  @Insert(onConflict = REPLACE)
  suspend fun insert(vararg discEntity: DiscEntity)

  @Query("DELETE FROM $DISC_TABLE_NAME")
  suspend fun deleteAll()

  @Query("DELETE FROM $DISC_TABLE_NAME WHERE id = :id")
  suspend fun deleteById(id: Int)

  @Query("SELECT * FROM $DISC_TABLE_NAME ORDER BY title")
  fun getAll(): Flow<List<DiscEntity>>
}

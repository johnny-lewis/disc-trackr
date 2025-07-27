package dev.johnnylewis.disctrackr.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val DISC_TABLE_NAME: String = "disc"

@Entity(tableName = DISC_TABLE_NAME)
data class DiscEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int?,
  val title: String,
  val imageUrl: String?,
  val format: String,
  val region: String?,
  val countryCode: String?,
  val distributor: String,
  val blurayId: String,
)

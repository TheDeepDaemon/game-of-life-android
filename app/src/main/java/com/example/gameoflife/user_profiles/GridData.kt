package com.example.gameoflife.user_profiles

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "grid_data_table",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userID"),
        onDelete = ForeignKey.CASCADE,
    )])
data class GridData(
    @PrimaryKey(autoGenerate = false)
    val saveName: String,
    val userID: Int,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GridData

        if (saveName != other.saveName) return false
        if (userID != other.userID) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = saveName.hashCode()
        result = 31 * result + userID
        result = 31 * result + data.contentHashCode()
        return result
    }
}
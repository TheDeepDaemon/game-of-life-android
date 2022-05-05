package com.example.gameoflife.user_profiles

import androidx.room.Entity
import androidx.room.PrimaryKey




@Entity(tableName = "users_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    var passwordHash: ByteArray
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (name != other.name) return false
        if (!passwordHash.contentEquals(other.passwordHash)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + passwordHash.contentHashCode()
        return result
    }
}
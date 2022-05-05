package com.example.gameoflife.user_profiles

import androidx.room.*


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM users_table WHERE name = :name LIMIT 1")
    fun getUser(name: String): User?

    @Query("SELECT id FROM users_table WHERE name = :name LIMIT 1")
    fun getUserID(name: String): Int?

    @Query("SELECT COUNT(*) FROM users_table WHERE name = :name LIMIT 1")
    fun usernameExists(name: String): Int

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT COUNT(*) FROM users_table WHERE name = :name and passwordHash = :hash")
    fun matchHash(name: String, hash: ByteArray): Int

    @Query("DELETE FROM users_table WHERE id = :id AND name = :name")
    fun deleteUser(id: Int, name: String)

}
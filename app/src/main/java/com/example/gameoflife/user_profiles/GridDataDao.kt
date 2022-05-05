package com.example.gameoflife.user_profiles

import androidx.room.*


@Dao
interface GridDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGrid(gridData: GridData)

    @Query("SELECT grid_data_table.* FROM " +
            "grid_data_table JOIN users_table " +
            "ON users_table.id = grid_data_table.userID " +
            "AND users_table.name == :name"
    )
    fun getGridData(name: String): List<GridData>

    @Query("DELETE FROM grid_data_table WHERE saveName = :name")
    fun deleteGrid(name: String)

}

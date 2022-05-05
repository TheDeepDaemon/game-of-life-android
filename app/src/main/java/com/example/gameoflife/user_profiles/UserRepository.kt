package com.example.gameoflife.user_profiles

class UserRepository(private val userDao: UserDao, private val gridDataDao: GridDataDao) {

    suspend fun addUser(user: User) {
        val numWithName: Int = userDao.usernameExists(user.name)

        // redundant check to make sure
        // the same name is not included twice
        if (numWithName == 0) {
            userDao.addUser(user)
        }
    }

    fun getUser(name: String): User? {
        return userDao.getUser(name)
    }

    fun getUserID(name: String): Int? {
        return userDao.getUserID(name)
    }

    fun usernameExists(name: String): Boolean {
        return userDao.usernameExists(name) > 0
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    fun matchHash(name: String, hash: ByteArray): Int {
        return userDao.matchHash(name, hash)
    }

    suspend fun addGrid(grid: GridData) {
        gridDataDao.addGrid(grid)
    }

    // return the list of all grids saved by the user
    fun getUserSaves(name: String): List<GridData> {
        return gridDataDao.getGridData(name)
    }

    fun deleteUser(name: String) {
        val userID: Int? = userDao.getUserID(name)
        if (userID != null) {
            userDao.deleteUser(userID, name)
        }
    }

    fun deleteGrid(name: String) {
        gridDataDao.deleteGrid(name)
    }

}
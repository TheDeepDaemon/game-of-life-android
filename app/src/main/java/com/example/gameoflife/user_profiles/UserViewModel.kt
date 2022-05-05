package com.example.gameoflife.user_profiles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameoflife.debugLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserViewModel(application: Application): AndroidViewModel(application) {

    private val repository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        val gridDataDao = UserDatabase.getDatabase(application).gridDataDao()
        repository = UserRepository(userDao, gridDataDao)
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
            debugLog("user added")
        }
    }

    fun usernameExists(name: String, isNotUsed: () -> Unit, alreadyUsed: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val exists = repository.usernameExists(name)

            launch(Dispatchers.Main) {
                if (!exists) {
                    isNotUsed()
                }
                else {
                    alreadyUsed()
                }
            }
        }
    }

    fun updatePasswordHash(username: String, hash: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUser(username)

            if (user != null) {
                user.passwordHash = hash
                repository.updateUser(user)
            }

        }
    }

    fun verifyUser(name: String, hash: ByteArray,
                   onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.matchHash(name, hash)

            launch(Dispatchers.Main) {
                if (result == 1) {
                    onSuccess()
                }
                else {
                    onFailure()
                }
            }
        }
    }

    fun addGrid(username: String, gridName: String, data: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId: Int? = repository.getUserID(username)

            if (userId != null) {
                val grid = GridData(gridName, userId, data)
                repository.addGrid(grid)
            }
        }
    }

    fun getUserSaves(name: String,
                     onCompletion: (gridList: List<GridData>) -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {

            val saves = repository.getUserSaves(name)

            launch(Dispatchers.Main) {
                onCompletion(saves)
            }
        }

    }

    fun deleteUser(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(name)
        }
    }

    fun deleteGrid(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteGrid(name)
        }
    }


}

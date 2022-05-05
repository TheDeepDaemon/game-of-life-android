package com.example.gameoflife

import android.app.Application
import com.example.gameoflife.user_profiles.GridData
import com.example.gameoflife.user_profiles.User
import com.example.gameoflife.user_profiles.UserViewModel
import com.example.gameoflife.user_profiles.superHash

// the single repository for everything
// to interact with the database through
object ApplicationRepository {
    private var userViewModel: UserViewModel? = null

    fun initViewModel(application: Application) {
        userViewModel = UserViewModel(application)
    }

    fun addUser(username: String, password: String) {
        if (userViewModel != null) {
            val uvm = userViewModel!!
            val hashedPassword = superHash(username, password)
            val user = User(0, username, hashedPassword)
            uvm.addUser(user)
        }
    }

    fun verifyUser(username: String, password: String,
                   onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (userViewModel != null) {
            val uvm = userViewModel!!
            val hash = superHash(username, password)
            uvm.verifyUser(username, hash, onSuccess, onFailure)
        }
    }

    fun usernameExists(username: String,
                       isNotUsed: () -> Unit, alreadyUsed: () -> Unit) {
        if (userViewModel != null) {
            val uvm = userViewModel!!
            uvm.usernameExists(username, isNotUsed, alreadyUsed)
        }
    }

    fun updatePassword(username: String, newPassword: String) {
        if (userViewModel != null) {
            val uvm = userViewModel!!
            val hash = superHash(username, newPassword)
            uvm.updatePasswordHash(username, hash)
        }
    }

    fun addGrid(name: String, gridName: String, data: ByteArray) {
        if (userViewModel != null) {
            val uvm = userViewModel!!
            uvm.addGrid(name, gridName, data)
        }
    }

    fun getGridData(name: String,
                    onCompletion: (gridList: List<GridData>) -> Unit) {
        if (userViewModel != null) {
            val uvm = userViewModel!!
            uvm.getUserSaves(name, onCompletion)
        }
    }

    fun deleteGrid(name: String) {
        if (userViewModel != null) {
            val uvm = userViewModel!!
            uvm.deleteGrid(name)
        }
    }

    fun deleteUser(name: String) {
        if (userViewModel != null) {
            userViewModel!!.deleteUser(name)
        }
    }

}

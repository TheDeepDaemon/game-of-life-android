package com.example.gameoflife

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gameoflife.login_screens.CreateProfileFragment
import com.example.gameoflife.login_screens.DeleteProfileFragment
import com.example.gameoflife.login_screens.LoginFragment
import com.example.gameoflife.login_screens.UpdatePasswordFragment


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ApplicationRepository.initViewModel(application)
        gotoLogin()

        val code = intent.getIntExtra("code", 0)
        if (code == -1) {
            toast("There was an issue logging in.")
        }

    }

    fun goToMainActivity(username: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    fun gotoLogin() {
        val loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.loginFrameLayout, loginFragment)
            commit()
        }
    }

    fun gotoCreateProfile() {
        val createProfileFragment = CreateProfileFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.loginFrameLayout, createProfileFragment)
            commit()
        }
    }

    fun gotoUpdatePassword() {
        val updatePasswordFragment = UpdatePasswordFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.loginFrameLayout, updatePasswordFragment)
            commit()
        }
    }

    fun gotoDeleteProfile() {
        val deleteProfileFragment = DeleteProfileFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.loginFrameLayout, deleteProfileFragment)
            commit()
        }
    }

    private fun toast(message: String) {
        toast(this, message)
    }


}
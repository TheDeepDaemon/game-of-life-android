package com.example.gameoflife

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    lateinit var gridDisplayFragment: MainGridDisplayFragment
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recievedName = intent.getStringExtra("username")
        if (recievedName != null) {
            username = recievedName
            setupDisplay()
        }
        else {
            goToLoginScreen(-1)
        }

    }

    fun goToLoginScreen(code: Int) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("code", code)
        startActivity(intent)
    }

    private fun setupDisplay() {
        gridDisplayFragment = MainGridDisplayFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFrameLayout, gridDisplayFragment)
            commit()
        }
    }

    // you can get but not set
    fun getUsername(): String {
        return username
    }

}
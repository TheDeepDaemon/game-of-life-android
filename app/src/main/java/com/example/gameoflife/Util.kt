package com.example.gameoflife

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.gameoflife.grid.Grid

// used for debugging
fun debugLog(message: String) {
    Log.d("Log", message)
}

fun toast(context: Context, message: String, long: Boolean=false) {
    val toastLength = if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(context, message, toastLength).show()
}

fun isPasswordValid(password: String): Boolean {
    return password.length >= 4 && password.filter { it != ' ' } == password
}

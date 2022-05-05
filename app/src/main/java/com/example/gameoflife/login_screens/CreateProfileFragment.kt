package com.example.gameoflife.login_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.gameoflife.ApplicationRepository
import com.example.gameoflife.LoginActivity
import com.example.gameoflife.R
import com.example.gameoflife.isPasswordValid


class CreateProfileFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.new_profile_screen, container, false)

        val activity = requireActivity() as LoginActivity

        val usernameEt: EditText = view.findViewById(R.id.et_enterNewName)
        val passwordEt: EditText = view.findViewById(R.id.et_enterNewPass)
        val goBackBtn: Button = view.findViewById(R.id.btn_newProfGoBack)
        val submitBtn = view.findViewById<Button>(R.id.btn_submitNewAccount)

        submitBtn.setOnClickListener {

            val username: String = usernameEt.text.toString()
            val password: String = passwordEt.text.toString()

            val usernameIsValid: Boolean = isUsernameValid(username)
            val passwordIsValid: Boolean = isPasswordValid(password)

            ApplicationRepository.usernameExists(username,
                {
                    // if it does not exist
                    usernameEt.text.clear()
                    passwordEt.text.clear()
                    if (usernameIsValid && passwordIsValid) {
                        ApplicationRepository.addUser(username, password)
                        activity.gotoLogin()
                    } else {
                        if (!usernameIsValid) {
                            toast("Username is not valid.")
                        } else if (!passwordIsValid) {
                            toast("Password is not valid.")
                        }
                    }
                },
                {
                    // if it does exist
                    toast("That username is already being used.")
                })

        }

        goBackBtn.setOnClickListener {
            activity.gotoLogin()
        }

        return view
    }

    private fun isUsernameValid(username: String): Boolean {
        return !username.isNullOrEmpty() && username.filter { it.isLetterOrDigit() } == username
    }

    private fun toast(message: String) {
        com.example.gameoflife.toast(requireContext(), message)
    }

}
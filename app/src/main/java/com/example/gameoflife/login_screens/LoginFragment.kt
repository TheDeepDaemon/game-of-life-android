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

class LoginFragment: Fragment()  {

    lateinit var loginBtn: Button
    lateinit var createProfBtn: Button
    lateinit var updatePassBtn: Button
    lateinit var deleteProfBtn: Button
    lateinit var usernameEt: EditText
    lateinit var passwordEt: EditText

    private fun initVariables(view: View) {
        loginBtn = view.findViewById(R.id.btn_login)
        createProfBtn = view.findViewById(R.id.btn_createNew)
        updatePassBtn = view.findViewById(R.id.btn_updatePassword)
        usernameEt = view.findViewById(R.id.et_enterName)
        passwordEt = view.findViewById(R.id.et_enterPass)
        deleteProfBtn = view.findViewById(R.id.btn_deleteProfile)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.login_screen, container, false)

        val activity = requireActivity() as LoginActivity

        initVariables(view)

        loginBtn.setOnClickListener {

            val username: String = usernameEt.text.toString()
            val password: String = passwordEt.text.toString()
            usernameEt.text.clear()
            passwordEt.text.clear()

            ApplicationRepository.verifyUser(username, password, {
                // success
                activity.goToMainActivity(username)
            }, {
                // failure
                toast("Username and/or password is wrong.")
            })

        }

        createProfBtn.setOnClickListener {
            activity.gotoCreateProfile()
        }

        updatePassBtn.setOnClickListener {
            activity.gotoUpdatePassword()
        }

        deleteProfBtn.setOnClickListener {
            activity.gotoDeleteProfile()
        }

        return view
    }

    private fun toast(message: String) {
        com.example.gameoflife.toast(requireContext(), message)
    }

}
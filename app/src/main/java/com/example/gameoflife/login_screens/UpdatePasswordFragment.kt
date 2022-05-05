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

class UpdatePasswordFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.update_password_screen, container, false)

        val activity = requireActivity() as LoginActivity

        val usernameEt = view.findViewById<EditText>(R.id.et_udName)
        val oldPassEt = view.findViewById<EditText>(R.id.et_udPassOld)
        val newPassEt = view.findViewById<EditText>(R.id.et_udPassNew)
        val goBackBtn = view.findViewById<Button>(R.id.btn_udPassGoBack)

        val submitBtn = view.findViewById<Button>(R.id.btn_submitNewPass)
        submitBtn.setOnClickListener {

            val username = usernameEt.text.toString()
            val oldPassword = oldPassEt.text.toString()
            val newPassword = newPassEt.text.toString()

            if (isPasswordValid(newPassword)) {
                ApplicationRepository.verifyUser(username, oldPassword, {
                    ApplicationRepository.updatePassword(username, newPassword)
                    activity.gotoLogin()
                }, {
                    toast("The password you entered was incorrect.")
                })
            }
            else {
                toast("Invalid password entered.")
            }
        }

        goBackBtn.setOnClickListener {
            activity.gotoLogin()
        }

        return view
    }

    private fun toast(message: String) {
        com.example.gameoflife.toast(requireContext(), message)
    }

}
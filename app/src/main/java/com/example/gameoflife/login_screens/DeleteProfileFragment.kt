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

class DeleteProfileFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.delete_profile_screen, container, false)
        val activity = requireActivity() as LoginActivity

        val deleteNameEt = view.findViewById<EditText>(R.id.et_deleteName)
        val deletePassEt = view.findViewById<EditText>(R.id.et_deletePass)
        val submitDeleteBtn = view.findViewById<Button>(R.id.btn_sumbitDelete)
        val goBack = view.findViewById<Button>(R.id.btn_delGoBack)

        submitDeleteBtn.setOnClickListener {
            val username = deleteNameEt.text.toString()
            val password = deletePassEt.text.toString()
            deleteNameEt.text.clear()
            deletePassEt.text.clear()

            ApplicationRepository.verifyUser(username, password, {
                // success
                ApplicationRepository.deleteUser(username)
                activity.gotoLogin()
            }, {
                // failure
                toast("Username and/or password is wrong.")
            })

        }

        goBack.setOnClickListener {
            activity.gotoLogin()
        }

        return view
    }

    private fun toast(message: String) {
        com.example.gameoflife.toast(requireContext(), message)
    }

}
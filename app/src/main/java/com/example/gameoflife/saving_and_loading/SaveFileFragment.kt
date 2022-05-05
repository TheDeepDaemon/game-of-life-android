package com.example.gameoflife.saving_and_loading

import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.gameoflife.MainGridDisplayFragment
import com.example.gameoflife.R
import com.example.gameoflife.debugLog
import com.example.gameoflife.toast


class SaveFileFragment(private val fragment: MainGridDisplayFragment): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.enter_sim_name, container, false)

        val enterSaveNameEt = view.findViewById<EditText>(R.id.et_enterSaveName)
        val goBtn = view.findViewById<Button>(R.id.btn_SubmitSimName)
        val clearBg = view.findViewById<ImageView>(R.id.enterSaveNameBg)

        goBtn.setOnClickListener {
            val text = enterSaveNameEt.text.toString()
            if (isValidSaveName(text)) {
                fragment.submitSaveGrid(text)
            }
            else {
                toast(fragment.requireContext(), "Name entered must be alphanumeric")
            }
        }

        enterSaveNameEt.setOnEditorActionListener { _, _, event ->
            if (event != null &&
                (event.keyCode === KeyEvent.KEYCODE_ENTER)) {
                //do what you want on the press of 'done'
                debugLog("Enter or Done")
                goBtn.performClick()
            }
            false
        }

        clearBg.setOnClickListener {
            fragment.closeSaveDialog()
        }

        return view
    }

    private fun isValidSaveName(text: String): Boolean {
        val filteredText = text.filter {
            it.isLetterOrDigit() || it == ' ' || it == '-'
        }
        return filteredText == text
    }

}
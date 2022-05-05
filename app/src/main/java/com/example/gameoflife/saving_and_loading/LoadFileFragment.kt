package com.example.gameoflife.saving_and_loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameoflife.MainActivity
import com.example.gameoflife.MainGridDisplayFragment
import com.example.gameoflife.R


class LoadFileFragment(private val fragment: MainGridDisplayFragment): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.load_screen, container, false)
        val activity = requireActivity() as MainActivity

        val listAdapter = LoadFileListAdapter(fragment)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_loadFile)
        recyclerView.adapter = listAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        listAdapter.setData(activity.getUsername())

        val btnClose = view.findViewById<ImageButton>(R.id.btn_closeLoadScreen)
        btnClose.setOnClickListener {
            fragment.closeLoadScreen()
        }

        view.bringToFront()
        return view
    }

}
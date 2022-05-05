package com.example.gameoflife

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameoflife.saving_and_loading.LoadFileFragment
import com.example.gameoflife.saving_and_loading.SaveFileFragment
import com.example.gameoflife.grid.GridFragment
import com.example.gameoflife.patterns.CellPattern
import com.example.gameoflife.patterns.PatternListAdapter
import com.example.gameoflife.user_profiles.GridData


class MainGridDisplayFragment: Fragment()  {

    private lateinit var gridFragment: GridFragment
    private lateinit var runBtn: ImageButton
    private lateinit var oneStepBtn: ImageButton
    private lateinit var titleTv: TextView
    private lateinit var quickSaveBtn: ImageButton
    private var loadFileFragment: LoadFileFragment? = null
    private var saveFileFragment: SaveFileFragment? = null

    private fun initViews(view: View) {
        runBtn = view.findViewById(R.id.btn_run)
        oneStepBtn = view.findViewById(R.id.btn_stepForward)
        titleTv = view.findViewById(R.id.tv_gridTitleText)
        quickSaveBtn = view.findViewById<ImageButton>(R.id.btn_saveQuick)
    }

    private fun initGrid() {
        gridFragment = GridFragment()
        val fragMan: FragmentManager = childFragmentManager
        val fragTrans: FragmentTransaction = fragMan.beginTransaction()
        fragTrans.add(R.id.gridFrame, gridFragment)
        fragTrans.commit()
    }

    private fun initList(view: View) {
        val listAdapter = PatternListAdapter(this)
        val recyclerView = view.findViewById<RecyclerView>(R.id.patternsRecyclerView)
        recyclerView.adapter = listAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val patterns: List<CellPattern> = listOf(
            CellPattern(resources.openRawResource(R.raw.pattern_glider)),
            CellPattern(resources.openRawResource(R.raw.pattern_blinker)),
            CellPattern(resources.openRawResource(R.raw.pattern_toad)),
            CellPattern(resources.openRawResource(R.raw.pattern_boat)),
            CellPattern(resources.openRawResource(R.raw.pattern_tub)),
            CellPattern(resources.openRawResource(R.raw.pattern_loaf)),
            CellPattern(resources.openRawResource(R.raw.pattern_beehive)),
            CellPattern(resources.openRawResource(R.raw.pattern_beacon))
        )

        listAdapter.setData(patterns)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_grid_layout, container, false)
        initGrid()
        initViews(view)
        initList(view)

        runBtn.setOnClickListener {
            startOrPause()
        }

        oneStepBtn.setOnClickListener {
            if (!gridFragment.isRunning()) {
                gridFragment.step()
            }
        }

        val saveBtn = view.findViewById<ImageButton>(R.id.btn_save)
        saveBtn.setOnClickListener {
            saveDialog()
        }

        quickSaveBtn.setOnClickListener {
            submitSaveGrid(titleTv.text.toString())
        }

        val loadBtn = view.findViewById<Button>(R.id.btn_load)
        loadBtn.setOnClickListener {
            openLoadScreen()
        }

        val backBtn = view.findViewById<ImageButton>(R.id.btn_logout)
        backBtn.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.goToLoginScreen(0)
        }

        val prettyDisplaySwitch = view.findViewById<Switch>(R.id.sch_pretty)
        prettyDisplaySwitch.setOnClickListener {
            val isChecked: Boolean = prettyDisplaySwitch.isChecked
            gridFragment.setIfPrettyDisplay(isChecked)
            gridFragment.refresGridDisplay()
        }

        val clearBtn = view.findViewById<ImageButton>(R.id.btn_clearGrid)
        clearBtn.setOnClickListener {
            gridFragment.clearGrid()
        }

        return view
    }

    private fun startOrPause() {
        if (!gridFragment.isRunning()) {
            gridFragment.startSimulation()
            val img = getDrawable(this.requireContext(), R.drawable.ic_baseline_pause_24)
            runBtn.setImageDrawable(img)
        }
        else {
            gridFragment.pauseSimulation()
            val img = getDrawable(this.requireContext(), R.drawable.ic_baseline_play_arrow_24)
            runBtn.setImageDrawable(img)
        }
    }

    private fun openLoadScreen() {
        val frameLayout = requireView().findViewById<FrameLayout>(R.id.loadMenuFrame)

        frameLayout.isVisible = true
        frameLayout.bringToFront()

        loadFileFragment = LoadFileFragment(this)
        childFragmentManager.beginTransaction().apply {
            replace(R.id.loadMenuFrame, loadFileFragment!!)
            commit()
        }
        gridFragment.suspended = true
    }

    fun loadGridData(grid: GridData, title: String) {
        gridFragment.setGridData(grid.data)
        closeLoadScreen()
        titleTv.text = title
        quickSaveBtn.visibility = View.VISIBLE
    }

    fun closeLoadScreen() {
        if (loadFileFragment != null) {
            childFragmentManager.beginTransaction().apply {
                remove(loadFileFragment!!)
                commit()
            }
            loadFileFragment = null
            gridFragment.suspended = false
        }
    }

    private fun saveDialog() {
        val frameLayout = requireView().findViewById<FrameLayout>(R.id.saveGridFrame)

        frameLayout.isVisible = true
        frameLayout.bringToFront()

        saveFileFragment = SaveFileFragment(this)
        childFragmentManager.beginTransaction().apply {
            replace(R.id.saveGridFrame, saveFileFragment!!)
            commit()
        }
        gridFragment.suspended = true
    }

    fun setSelectedPattern(cellPattern: CellPattern, onPlaced: () -> Unit) {
        gridFragment.setSelectedPattern(cellPattern, onPlaced)
    }

    fun deselectPattern() {
        gridFragment.deselectPattern()
    }

    fun submitSaveGrid(saveName: String) {
        closeSaveDialog()
        val bytes = gridFragment.getSaveData()
        val activity = requireActivity() as MainActivity
        val username = activity.getUsername()
        ApplicationRepository.addGrid(username, saveName, bytes)
        titleTv.text = saveName
        quickSaveBtn.visibility = View.VISIBLE
    }

    fun closeSaveDialog() {
        if (saveFileFragment != null) {
            childFragmentManager.beginTransaction().apply {
                remove(saveFileFragment!!)
                commit()
            }
            saveFileFragment = null
            gridFragment.suspended = false
        }
    }

    fun getUsername(): String {
        val mainActivity = requireActivity() as MainActivity
        return mainActivity.getUsername()
    }

}
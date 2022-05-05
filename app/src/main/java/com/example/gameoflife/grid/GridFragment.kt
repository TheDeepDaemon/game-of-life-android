package com.example.gameoflife.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gameoflife.MainGridDisplayFragment
import com.example.gameoflife.R
import com.example.gameoflife.grid.Grid.Companion.GRID_SIZE
import com.example.gameoflife.patterns.CellPattern
import kotlinx.coroutines.*

// this class is dedicated to displaying the grid on screen
class GridFragment:
    Fragment() {

    private lateinit var grid: Grid
    private lateinit var layout: FrameLayout
    private lateinit var shownCells: MutableList<ImageView?>

    private val whiteBoxImg = R.drawable.white_box
    private val blackBoxImg = R.drawable.black_box
    private var cellImg = R.drawable.cell_image
    private var clearBoxImg = R.drawable.clear_box
    private var prettyDisplay = false

    private var running: Boolean = false
    private var simJob: Job? = null
    var suspended = false
    private var selectedPattern: CellPattern? = null
    private var onPatternPlaced: () -> Unit = {}

    private fun toGridIndex(row: Int, col: Int): Int {
        return (row * GRID_SIZE) + col
    }

    // set the cell Drawable based on whether it is on
    private fun setCellImage(cell: ImageView, on: Boolean) {
        val img =  if (prettyDisplay) {
            if (on) cellImg else clearBoxImg
        }
        else {
            if (on) whiteBoxImg else blackBoxImg
        }
        cell.setImageDrawable(getDrawable(layout.context, img))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.grid_layout, container, false)
        grid = ViewModelProvider(this).get(Grid::class.java)
        shownCells = MutableList<ImageView?>(GRID_SIZE * GRID_SIZE) { null }

        layout = view as FrameLayout
        createGrid()

        grid.liveGridData.observe(viewLifecycleOwner, Observer { gridData ->
            updateGridDisplay(gridData)
        })

        return view
    }

    // create the grid of cells shown to the user
    private fun createGrid() {
        val cellWidth = layout.layoutParams.width / GRID_SIZE
        val cellHeight = layout.layoutParams.height / GRID_SIZE

        for (i in 0 until GRID_SIZE) {
            for (j in 0 until GRID_SIZE) {
                createCell(i, j, cellWidth, cellHeight)
            }
        }
    }

    // create an individual grid cell: ImageView
    private fun createCell(
        row: Int, col: Int,
        width: Int, height: Int) {
        val cell = ImageView(layout.context)
        setCellImage(cell, false)

        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)

        params.leftMargin = col * width
        params.topMargin = row * height
        params.width = width
        params.height = height

        cell.layoutParams = params

        cell.adjustViewBounds = true
        cell.scaleType = ImageView.ScaleType.FIT_XY

        cell.setOnClickListener {
            onCellClick(row, col)
        }

        layout.addView(cell, params)
        val ind: Int = toGridIndex(row, col)
        shownCells[ind] = cell
    }

    private fun onCellClick(row: Int, col: Int) {
        if (selectedPattern == null) {
            grid.cellClicked(row, col)
        }
        else {
            grid.placePattern(selectedPattern!!, row, col)
            onPatternPlaced()
            deselectPattern()
        }
    }

    // use the stored grid data to
    // update what is shown on screen
    private fun updateGridDisplay(gridData: BooleanArray) {
        val n = GRID_SIZE * GRID_SIZE
        for (i in 0 until n) {
            val cell = shownCells[i]
            if (cell != null) {
                setCellImage(cell, gridData[i])
            }
        }
    }

    fun refresGridDisplay() {
        grid.refresh()
    }

    // start periodically stepping forward
    // with a delay of WAIT_TIME between steps
    fun startSimulation() {
        running = true
        if (simJob == null) {
            simJob = GlobalScope.launch(Dispatchers.Main) {
                while (running) {
                    step()
                    delay(WAIT_TIME)
                }
                simJob = null
            }
        }
    }

    fun pauseSimulation() {
        running = false
    }

    fun isRunning(): Boolean {
        return running
    }

    fun step() {
        if (!suspended) {
            grid.step()
        }
    }

    fun getSaveData(): ByteArray {
        return grid.getSaveData()
    }

    fun setGridData(data: ByteArray) {
        grid.setGrid(data)
    }

    fun setSelectedPattern(cellPattern: CellPattern, onPlaced: () -> Unit) {
        selectedPattern = cellPattern
        onPatternPlaced = onPlaced
    }

    fun deselectPattern() {
        selectedPattern = null
        onPatternPlaced = {}
    }

    fun setIfPrettyDisplay(isChecked: Boolean) {
        prettyDisplay = isChecked
    }

    fun clearGrid() {
        grid.clearGrid()
    }

    companion object {
        // the wait time between steps
        const val WAIT_TIME = 250L
    }

}
package com.example.gameoflife.grid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gameoflife.patterns.CellPattern

// this class is dedicated to the
// actual logic of the rules for updating the grid
class Grid: ViewModel() {
    var liveGridData = MutableLiveData<BooleanArray>()
    init {
        liveGridData.value = BooleanArray(NUM_CELLS) { false }
    }

    private fun toGridIndex(row: Int, col: Int): Int {
        return (row * GRID_SIZE) + col
    }

    // if grid data exists, return it.
    // if not, init it then return it.
    private fun requireGridData(): BooleanArray {
        return if (liveGridData.value != null) {
            liveGridData.value!!
        } else {
            liveGridData.value = BooleanArray(NUM_CELLS) { false }
            liveGridData.value!!
        }
    }

    fun refresh() {
        liveGridData.value = liveGridData.value
    }

    fun clearGrid() {
        liveGridData.value = BooleanArray(NUM_CELLS) { false }
    }

    fun cellClicked(row: Int, col: Int) {
        liveGridData.value = liveGridData.value?.also {
            it[toGridIndex(row, col)] = !it[toGridIndex(row, col)]
        }
    }

    fun step() {
        liveGridData.value = updateGrid(requireGridData(), GRID_SIZE, GRID_SIZE)
    }

    fun getSaveData(): ByteArray {
        return boolsToBytes(requireGridData())
    }

    fun setGrid(bytes: ByteArray) {
        liveGridData.value = bytesToBools(bytes)
    }

    private fun wrap(input: Int, ceiling: Int): Int {
        return if (input < ceiling) {
            if (input >= 0) {
                input
            } else {
                ceiling + input
            }
        } else {
            input % ceiling
        }
    }

    fun placePattern(cellPattern: CellPattern, row: Int, col: Int) {

        val pRows = cellPattern.rows
        val pCols = cellPattern.cols
        val centerR = pRows / 2
        val centerC = pCols / 2

        val startRow = row - centerR

        val startCol = col - centerC

        val data: BooleanArray = requireGridData()

        for (i in 0 until pRows) {
            for (j in 0 until pCols) {
                val curRow = wrap(startRow + i, GRID_SIZE)
                val curCol = wrap(startCol + j, GRID_SIZE)
                val cellValue = cellPattern.get(i, j)
                data[toGridIndex(curRow, curCol)] = cellValue
            }
        }

        liveGridData.value = data
    }

    companion object {
        // number of cells across
        const val GRID_SIZE = 10
        const val NUM_CELLS = GRID_SIZE * GRID_SIZE

        init {
            System.loadLibrary("gameoflife")
        }
        external fun updateGrid(gridData: BooleanArray, rows: Int, cols: Int) : BooleanArray
        external fun boolsToBytes(boolArr: BooleanArray): ByteArray
        external fun bytesToBools(byteArr: ByteArray): BooleanArray
    }

}
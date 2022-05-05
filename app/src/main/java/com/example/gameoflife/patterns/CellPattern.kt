package com.example.gameoflife.patterns

import java.io.*


class CellPattern(inputStream: InputStream) {
    var rows: Int = 0
    var cols: Int = 0
    var data: BooleanArray
    var name: String
    init {
        val buffer = BufferedReader(InputStreamReader(inputStream))

        var lines: MutableList<String> = mutableListOf()

        try {
            buffer.forEachLine {
                lines.add(it)
            }
        } catch (e: IOException) {
            println(e)
        }
        finally {
            buffer.close()
        }

        val firstLine = lines[0].split(',')

        rows = firstLine[0].toInt()
        cols = firstLine[1].toInt()

        data = BooleanArray(rows * cols) { false }

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val c = lines[1+i][j]
                data[(i * cols) + j] = (c == '1')
            }
        }

        name = lines[rows + 1]

    }

    fun get(row: Int, col: Int): Boolean {
        return data[(row * cols) + col]
    }

}
package com.example.gameoflife.patterns

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameoflife.MainGridDisplayFragment
import com.example.gameoflife.R


class PatternListAdapter(private val fragment: MainGridDisplayFragment):
    RecyclerView.Adapter<PatternListAdapter.ViewHolder>() {

    private var patternsList: List<CellPattern> = emptyList()
    private var selecting: Boolean = false

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pattern_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return patternsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = holder.itemView
        val cardView = itemView as CardView
        val pattern = patternsList[position]

        val nameText = itemView.findViewById<TextView>(R.id.patternName)
        val image = itemView.findViewById<ImageView>(R.id.patternDisplay)
        val selImg = itemView.findViewById<ImageView>(R.id.patternSelected)

        nameText.text = pattern.name

        val whiteColor = Color.parseColor("#FFFFFF")
        val blackColor = Color.parseColor("#000000")

        val inverted = false
        var colors = if (inverted) {
            pattern.data.map { if (it) blackColor else whiteColor }.toIntArray()
        } else {
            pattern.data.map { if (it) whiteColor else blackColor }.toIntArray()
        }

        val bmp = createBitmap(colors, pattern.cols, pattern.rows, Bitmap.Config.ARGB_8888)

        val resources = fragment.resources
        val drawable: Drawable = BitmapDrawable(resources, bmp)

        drawable.isFilterBitmap = false

        image.setImageDrawable(drawable)

        image.setOnClickListener {

            if (!selecting) {

                val col = Color.parseColor("#7FFC99")
                cardView.setCardBackgroundColor(col)
                selImg.visibility = View.VISIBLE

                selecting = true

                fragment.setSelectedPattern(pattern) {
                    selImg.visibility = View.INVISIBLE
                    val col = Color.parseColor("#00FA9A")
                    cardView.setCardBackgroundColor(col)
                    selecting = false
                }
            }
            else {
                selImg.visibility = View.INVISIBLE
                val col = Color.parseColor("#00FA9A")
                cardView.setCardBackgroundColor(col)
                selecting = false
                fragment.deselectPattern()
            }
        }

    }

    fun setData(data: List<CellPattern>) {
        this.patternsList = data
        notifyDataSetChanged()
    }

}
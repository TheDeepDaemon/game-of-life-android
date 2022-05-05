package com.example.gameoflife.saving_and_loading

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameoflife.*
import com.example.gameoflife.user_profiles.GridData


class LoadFileListAdapter(private val fragment: MainGridDisplayFragment):
    RecyclerView.Adapter<LoadFileListAdapter.ViewHolder>() {

    private var gridDataList: List<GridData> = listOf()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.load_screen_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return gridDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = holder.itemView
        val gridData: GridData = gridDataList[position]

        val nameText = itemView.findViewById<TextView>(R.id.savedFileName)
        val delBtn = itemView.findViewById<ImageButton>(R.id.btn_deleteGrid)

        nameText.text = gridData.saveName

        itemView.setOnClickListener {
            fragment.loadGridData(gridData, gridData.saveName)
        }

        delBtn.setOnClickListener {
            ApplicationRepository.deleteGrid(gridData.saveName)
            setData(fragment.getUsername())
        }

    }

    fun setData(username: String) {
        ApplicationRepository.getGridData(username) { loadedData ->
            gridDataList = loadedData
            notifyDataSetChanged()
        }
    }

}
package com.example.nt.app.textrepeat.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.model.QuickCategory
import com.example.nt.app.textrepeat.model.QuickRepeatModel

class QuickRepeatParentAdapter(
    private val categories: List<QuickCategory>,
    private val onContentClick: (QuickRepeatModel) -> Unit
) : RecyclerView.Adapter<QuickRepeatParentAdapter.ParentViewHolder>() {

    class ParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvCategoryName)
        val rvRow1: RecyclerView = view.findViewById(R.id.rvRow1)
        val rvRow2: RecyclerView = view.findViewById(R.id.rvRow2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quick_repeat_group, parent, false)
        return ParentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val category = categories[position]
        holder.tvTitle.text = category.title

        // Chia đôi danh sách item để làm 2 hàng vuốt ngang
        val mid = category.items.size / 2
        val list1 = category.items.subList(0, mid)
        val list2 = category.items.subList(mid, category.items.size)

        holder.rvRow1.layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        holder.rvRow2.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)

        holder.rvRow1.adapter = QuickRepeatAdapter(list1, onContentClick)
        holder.rvRow2.adapter = QuickRepeatAdapter(list2, onContentClick)
    }

    override fun getItemCount() = categories.size
}
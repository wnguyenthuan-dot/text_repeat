package com.example.nt.app.textrepeat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nt.app.textrepeat.databinding.ItemRecentTextBinding
import com.example.nt.app.textrepeat.model.QuickRepeatModel

class QuickRepeatAdapter(
    private val list: List<QuickRepeatModel>,
    private val onItemClick: (QuickRepeatModel) -> Unit
) : RecyclerView.Adapter<QuickRepeatAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRecentTextBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // Cố định chiều rộng item khoảng 160dp để thấy được các item phía sau khi vuốt ngang
        val params = binding.root.layoutParams
        params.width = (parent.context.resources.displayMetrics.density * 160).toInt()
        binding.root.layoutParams = params

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvContent.text = item.content
        val randomCount = (100..200).random()
        holder.binding.tvCount.text = "x $randomCount"

        holder.itemView.setOnClickListener { onItemClick(item.copy(count = randomCount)) }
    }

    override fun getItemCount() = list.size
}
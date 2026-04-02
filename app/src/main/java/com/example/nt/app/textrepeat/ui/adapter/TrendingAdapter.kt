package com.example.nt.app.textrepeat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nt.app.textrepeat.databinding.ItemTrendingBinding
import com.example.nt.app.textrepeat.model.TrendingModel

class TrendingAdapter(
    private val list: List<TrendingModel>,
    private val onAction: (TrendingModel, String) -> Unit
) : RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTrendingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvTitle.text = item.title
        holder.binding.tvSubtitle.text = item.subtitle // Sẽ hiển thị "Trending x150"
        holder.binding.tvMainContent.text = item.fullContent

        // Click vào cả item để qua màn Repeat
        holder.itemView.setOnClickListener {
            onAction(item, "ITEM_CLICK")
        }

        holder.binding.btnCopy.setOnClickListener { onAction(item, "COPY") }
        holder.binding.btnShare.setOnClickListener { onAction(item, "SHARE") }
        holder.binding.btnDelete.setOnClickListener { onAction(item, "DELETE") }
    }

    override fun getItemCount() = list.size
}
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
        // Chỉ dùng 'position' này để hiển thị dữ liệu ban đầu
        val item = list[position]
        holder.binding.tvTitle.text = item.title
        holder.binding.tvSubtitle.text = item.subtitle
        holder.binding.tvMainContent.text = item.fullContent

        // Khi Click, phải lấy vị trí thực tế lúc đó
        holder.binding.btnDelete.setOnClickListener {
            val currentPos = holder.bindingAdapterPosition
            // Kiểm tra xem vị trí có hợp lệ không (tránh trường hợp item đang bị xóa dở)
            if (currentPos != RecyclerView.NO_POSITION) {
                onAction(list[currentPos], "DELETE")
            }
        }

        holder.itemView.setOnClickListener {
            val currentPos = holder.bindingAdapterPosition
            if (currentPos != RecyclerView.NO_POSITION) {
                onAction(list[currentPos], "ITEM_CLICK")
            }
        }

        holder.binding.btnCopy.setOnClickListener { onAction(item, "COPY") }
        holder.binding.btnShare.setOnClickListener { onAction(item, "SHARE") }
        holder.binding.btnDelete.setOnClickListener { onAction(item, "DELETE") }
    }

    override fun getItemCount() = list.size
}
package com.example.nt.app.textrepeat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nt.app.textrepeat.databinding.ItemSavedTextBinding
import com.example.nt.app.textrepeat.model.SavedTextModel

class SavedTextAdapter(
    private var list: MutableList<SavedTextModel>,
    private val onCopy: (String) -> Unit,
    private val onDelete: (Int) -> Unit,
    private val onShare: (String) -> Unit,
    private val onItemClick: (SavedTextModel) -> Unit
) : RecyclerView.Adapter<SavedTextAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSavedTextBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSavedTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvSavedTitle.text = item.originalText
        holder.binding.tvCountLabel.text = "x ${item.repeatCount}"

        // --- PHẦN TỐI ƯU HIỂN THỊ ---
        // Nếu kết quả quá dài (ví dụ > 500 ký tự), chỉ lấy một đoạn đầu để hiện preview
        // Điều này giúp RecyclerView cuộn cực mượt mà không bị khựng
        val previewText = if (item.resultText.length > 500) {
            item.resultText.substring(0, 500) + "..."
        } else {
            item.resultText
        }
        holder.binding.tvSavedContent.text = previewText

        // Sự kiện khi ấn vào Card
        holder.itemView.setOnClickListener { onItemClick(item) }

        // Sự kiện Copy: Vẫn copy TOÀN BỘ (item.resultText)
        holder.binding.btnItemCopy.setOnClickListener {
            onCopy(item.resultText)
        }

        holder.binding.btnDelete.setOnClickListener { onDelete(position) }

        // Thêm nút Share nếu bạn có khai báo trong callback
        holder.binding.btnItemShare.setOnClickListener {
            onShare(item.resultText)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: MutableList<SavedTextModel>) {
        this.list = newList
        notifyDataSetChanged()
    }
}
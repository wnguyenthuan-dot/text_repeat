package com.example.nt.app.textrepeat.ui.splash.language

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.databinding.ItemLanguageBinding

class MyLanguageAdapter(private val onClick: (Int) -> Unit) :
    ListAdapter<LanguageEntity, MyLanguageAdapter.ViewHolder>(DiffCallback()) {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position == selectedPosition)
    }

    fun updatePosition(position: Int) {
        val oldPos = selectedPosition
        selectedPosition = position
        if (oldPos != -1) notifyItemChanged(oldPos)
        notifyItemChanged(selectedPosition)
    }

    inner class ViewHolder(private val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LanguageEntity, isSelected: Boolean) {
            // ĐẢO NGƯỢC THỨ TỰ THEO Ý BẠN:
            // 1. Dòng 1: Ngôn ngữ quốc tế (titleGlobal)
            binding.tvName.text = item.enum.titleGlobal

            // 2. Dòng 2: Ngôn ngữ bản địa (title)
            binding.tvNativeName.text = item.enum.title

            // 3. Xử lý RadioButton
            binding.rbCheck.isChecked = isSelected

            // 4. Thay đổi giao diện khi Select
            if (isSelected) {
                binding.root.setBackgroundResource(R.drawable.bg_item_language_selected)
            } else {
                binding.root.setBackgroundResource(R.drawable.bg_item_language_unselected)
            }

            binding.root.setOnClickListener {
                onClick(adapterPosition)
            }

            // Đảm bảo RadioButton không chặn sự kiện click của root
            binding.rbCheck.setOnClickListener {
                binding.root.performClick()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<LanguageEntity>() {
        override fun areItemsTheSame(oldItem: LanguageEntity, newItem: LanguageEntity) =
            oldItem.enum.code == newItem.enum.code

        override fun areContentsTheSame(oldItem: LanguageEntity, newItem: LanguageEntity) =
            oldItem == newItem
    }
}
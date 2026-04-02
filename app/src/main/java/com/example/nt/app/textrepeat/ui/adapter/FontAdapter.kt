package com.example.nt.app.textrepeat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.nt.app.textrepeat.base.BaseAdapterRecyclerView
import com.example.nt.app.textrepeat.databinding.ItemFontBinding
import com.example.nt.app.textrepeat.model.FontEntity
import com.example.nt.app.textrepeat.utils.applyFont

class FontAdapter : BaseAdapterRecyclerView<FontEntity, ItemFontBinding>() {

    // Biến lưu vị trí đang được chọn
    var selectedPosition: Int = -1

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemFontBinding {
        return ItemFontBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemFontBinding, item: FontEntity, position: Int) {
        // 1. Áp dụng font và text
        applyFont(item.pathFont, item.text) {
            binding.tvFont.text = it
        }

        // 2. Xử lý hiển thị dấu tích (Giả sử ID dấu tích trong item_font là imgCheck)
        // Nếu position trùng với selectedPosition thì hiện, ngược lại ẩn
        binding.imgCheck.visibility = if (position == selectedPosition) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }

        // 3. Xử lý click vào item để chọn
        binding.root.setOnClickListener {
            val oldPosition = selectedPosition
            selectedPosition = position

            // Thông báo cập nhật để vẽ lại dấu tích
            notifyItemChanged(oldPosition)
            notifyItemChanged(selectedPosition)

            // Gọi callback nếu bạn vẫn cần xử lý gì đó ở Activity (không bắt buộc)
            //listener?.invoke(item, position)
        }
    }
}
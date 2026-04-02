package com.example.nt.app.textrepeat.model

data class SavedTextModel(
    val id: Long = System.currentTimeMillis(), // Dùng ID để phân biệt
    val originalText: String, // Chữ gốc người dùng nhập
    val resultText: String,   // Chữ sau khi đã lặp (để hiển thị preview)
    val repeatCount: Int,     // Số lần lặp (ví dụ: 104)
    val isNewLine: Boolean,   // Lưu trạng thái checkbox có xuống dòng hay không
    val fontIndex: Int = 0 // THÊM DÒNG NÀY
)
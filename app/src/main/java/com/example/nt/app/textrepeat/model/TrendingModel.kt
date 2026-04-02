package com.example.nt.app.textrepeat.model

data class TrendingModel(
    val title: String,      // Tiêu đề ngắn (vd: Miss you 💓)
    val subtitle: String,   // Phụ đề (vd: Miss you so much!)
    val fullContent: String // Toàn bộ đoạn văn bản lặp lại bên dưới
)
package com.example.nt.app.textrepeat.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class StrokeTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val strokeWidthValue = 15f
    // Tăng extraPadding lên một chút để tạo không gian an toàn cho chữ T
    private val extraPadding = strokeWidthValue * 2f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // Nới rộng chiều rộng và chiều cao để Stroke không bao giờ chạm biên
        val width = measuredWidth + (extraPadding * 2).toInt()
        val height = measuredHeight + (extraPadding * 2).toInt()
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        val text = text.toString()
        if (text.isEmpty()) return

        val paint = paint
        paint.isAntiAlias = true
        paint.textSize = textSize
        paint.typeface = typeface

        // Tính toán vị trí Y để chữ nằm giữa View theo chiều dọc
        val fontMetrics = paint.fontMetrics
        val x = extraPadding // Bắt đầu vẽ từ điểm đã cách lề
        val y = (height / 2f) - ((fontMetrics.descent + fontMetrics.ascent) / 2f)

        // 1. Vẽ viền (Stroke)
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeMiter = 10f
        paint.strokeWidth = strokeWidthValue
        paint.color = Color.parseColor("#1E88E5")
        canvas.drawText(text, x, y, paint)

        // 2. Vẽ chữ (Fill) đè lên
        paint.style = Paint.Style.FILL
        paint.color = textColors.defaultColor
        canvas.drawText(text, x, y, paint)

        // Lưu ý: Không gọi super.onDraw(canvas) để tránh vẽ đè thêm một lần nữa bị lẹm
    }
}
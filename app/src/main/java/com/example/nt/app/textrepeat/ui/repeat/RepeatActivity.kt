package com.example.nt.app.textrepeat.ui.repeat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import clickSafe
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.base.BaseActivity
import com.example.nt.app.textrepeat.databinding.ActivityRepeatBinding
import com.example.nt.app.textrepeat.model.SavedTextModel
import com.example.nt.app.textrepeat.ui.stylize.FontActivity
import com.example.nt.app.textrepeat.utils.Constant
import com.example.nt.app.textrepeat.utils.applyFont
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.lifecycle.lifecycleScope
import com.example.nt.app.textrepeat.utils.ex.setOnTouchScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepeatActivity : BaseActivity<ActivityRepeatBinding>() {

    private var isFavorited = false // Trạng thái lưu văn bản
    private var currentEditingId: Long = -1L // -1 nghĩa là đang tạo mới
    private var selectedFontPosition: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRepeatBinding.inflate(layoutInflater)
        binding.layoutResult.webViewResult.settings.javaScriptEnabled = true
        setContentView(binding.root)

        binding.edtInput.setOnTouchListener { v, event ->
            if (v.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }

        // Quay lại màn hình trước
        binding.btnBack.setOnClickListener { finish() }


        // Tăng số lượng
        binding.btnPlus.setOnClickListener {
            val count = binding.edtCount.text.toString().toIntOrNull() ?: 0
            binding.edtCount.setText((count + 1).toString())
        }

        // Giảm số lượng
        binding.btnMinus.setOnClickListener {
            val count = binding.edtCount.text.toString().toIntOrNull() ?: 0
            if (count > 1) binding.edtCount.setText((count - 1).toString())
        }

        // Xử lý tạo Text lặp
        binding.btnRepeatAction.setOnTouchScale {
            val input = binding.edtInput.text.toString()
            val count = binding.edtCount.text.toString().toIntOrNull() ?: 0

            if (count > 10000) {
                Toast.makeText(this, getString(R.string.toast_limit_10000), Toast.LENGTH_SHORT).show()
                return@setOnTouchScale
            }

            if (input.isNotEmpty() && count > 0) {
                // Hiển thị một cái Loading nếu cần vì 10,000 lần sẽ mất chút thời gian xử lý
                applyFont(selectedFontPosition, input) { stylized ->

                    // Dùng Coroutine để không làm đơ UI
                    lifecycleScope.launch(Dispatchers.Default) {
                        val finalResult = generateRepeatText(stylized, count)

                        // Tạo chuỗi Preview ngắn hơn để hiển thị cho mượt
                        val previewText = if (finalResult.length > 2000) {
                            finalResult.take(2000) + "\n..."
                        } else {
                            finalResult
                        }

                        withContext(Dispatchers.Main) {
                            // Hiển thị lên UI bằng chuỗi Preview ngắn
                            showResult(finalResult, previewText)

                            // Lưu vào lịch sử (nên để trong IO thread)
                            launch(Dispatchers.IO) {
                                saveToRecent(finalResult)
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.toast_enter_text_count), Toast.LENGTH_SHORT).show()
            }
        }

        currentEditingId = intent.getLongExtra("EXTRA_ID", -1L)
        val oldInput = intent.getStringExtra("EXTRA_INPUT")
        val oldCount = intent.getIntExtra("EXTRA_COUNT", -1)
        val oldNewLine = intent.getBooleanExtra("EXTRA_NEWLINE", true)
        val oldFontIndex = intent.getIntExtra("EXTRA_FONT_INDEX", 0)
        selectedFontPosition = oldFontIndex // Cập nhật biến toàn cục

        if (oldInput != null && oldCount != -1) {
            binding.edtInput.setText(oldInput)
            binding.edtCount.setText(oldCount.toString())
            binding.cbNewLine.isChecked = oldNewLine

            // Sử dụng lifecycleScope để tính toán chuỗi lớn ở luồng nền, tránh đơ UI khi mở màn hình
            lifecycleScope.launch(Dispatchers.Default) {
                applyFont(selectedFontPosition, oldInput) { stylized ->
                    // 1. Tạo chuỗi lặp đầy đủ
                    val finalResult = generateRepeatText(stylized, oldCount)

                    // 2. Tạo chuỗi Preview (chỉ lấy 2000 ký tự đầu) để hiển thị mượt hơn
                    val preview = if (finalResult.length > 2000) {
                        finalResult.take(2000) + "\n..."
                    } else {
                        finalResult
                    }

                    // 3. Quay lại luồng chính để cập nhật giao diện
                    lifecycleScope.launch(Dispatchers.Main) {
                        showResult(finalResult, preview)
                        binding.layoutResult.root.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private val fontLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedFontPosition = result.data?.getIntExtra(Constant.DATA, 0) ?: 0
                processRepeatText() // Gọi lại hàm xử lý chung để fix lỗi đỏ cũ
            }
        }

    private fun processRepeatText() {
        val input = binding.edtInput.text.toString()
        val count = binding.edtCount.text.toString().toIntOrNull() ?: 0

        if (count > 10000) {
            Toast.makeText(this, getString(R.string.toast_limit_general), Toast.LENGTH_SHORT).show()
            return
        }

        if (input.isNotEmpty() && count > 0) {
            applyFont(selectedFontPosition, input) { stylized ->
                lifecycleScope.launch(Dispatchers.Default) {
                    val finalResult = generateRepeatText(stylized, count)

                    // Tạo preview (tối đa 2000 ký tự) để tránh đơ UI khi setText
                    val preview = if (finalResult.length > 2000) {
                        finalResult.take(2000) + "\n..."
                    } else {
                        finalResult
                    }

                    withContext(Dispatchers.Main) {
                        showResult(finalResult, preview)
                        saveToRecent(finalResult)
                    }
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.toast_enter_text_count), Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateRepeatText(text: String, count: Int): String {
        val result = StringBuilder()
        val isNewLine = binding.cbNewLine.isChecked
        for (i in 1..count) {
            result.append(text)
            if (isNewLine) result.append("\n") else result.append(" ")
        }
        return result.toString().trim()
    }

    //    private fun showResult(text: String) {
//        // Hiển thị layout kết quả (Màn hình 3)
//        binding.layoutResult.root.visibility = View.VISIBLE
//        binding.layoutResult.tvResultDisplay.text = text
//        binding.layoutResult.tvResultDisplay.setOnTouchListener { v, event ->
//            v.parent.requestDisallowInterceptTouchEvent(true)
//            false
//        }
//        // Reset trạng thái trái tim mỗi khi nhấn lặp lại mới
//        isFavorited = false
//        binding.layoutResult.ivFavourite.setImageResource(R.drawable.ic_heart_outline)
//
//        // Xử lý nút Trái tim (Lưu vào Saved Text)
//        binding.layoutResult.ivFavourite.setOnClickListener {
//            isFavorited = !isFavorited
//            if (isFavorited) {
//                binding.layoutResult.ivFavourite.setImageResource(R.drawable.ic_heart_online) // Nhớ đổi sang filled
//                saveToFavorites(text)
//            } else {
//                binding.layoutResult.ivFavourite.setImageResource(R.drawable.ic_heart_outline)
//            }
//        }
//
//        binding.layoutResult.btnStylize.clickSafe {
//            val text = binding.edtInput.text.toString().trim()
//            if (text.isNotEmpty()) {
//                val intent = Intent(this, FontActivity::class.java)
//                intent.putExtra(Constant.DATA, text)
//                fontLauncher.launch(intent) // Sử dụng launcher để nhận kết quả trả về
//            } else {
//                Toast.makeText(this, "Please enter text first", Toast.LENGTH_SHORT).show()
//            }
//        }
//        // Xử lý nút Copy
//        binding.layoutResult.btnCopy.setOnClickListener {
//            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            val clip = ClipData.newPlainText("Repeated Text", text)
//            clipboard.setPrimaryClip(clip)
//            //Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
//        }
//
//        // Xử lý nút Send (Share)
//        binding.layoutResult.btnSend.setOnClickListener {
//            val shareIntent = Intent(Intent.ACTION_SEND).apply {
//                type = "text/plain"
//                putExtra(Intent.EXTRA_TEXT, text)
//            }
//            startActivity(Intent.createChooser(shareIntent, "Share via"))
//        }
//    }
    private fun showResult(fullText: String, previewText: String) {
        binding.layoutResult.root.visibility = View.VISIBLE

        // 1. Lấy thông số cơ bản
        val countLoop = binding.edtCount.text.toString().toIntOrNull() ?: 0
        val isNewLine = binding.cbNewLine.isChecked
        val separator = if (isNewLine) "<br>" else " "
        val rawInput = binding.edtInput.text.toString()

        // 2. Gọi applyFont để lấy chính xác cụm văn bản đã đổi font
        applyFont(selectedFontPosition, rawInput) { stylizedSingle ->

            // Fix lỗi cuộn cho WebView
            binding.layoutResult.webViewResult.setOnTouchListener { v, event ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }

            // 3. Tạo HTML với biến stylizedSingle chuẩn
            val htmlData = """
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body { 
                    word-wrap: break-word; 
                    font-family: sans-serif; 
                    padding: 12px; 
                    color: #444444; 
                    line-height: 1.5;
                    font-size: 16px;
                }
            </style>
        </head>
        <body>
            <div id="content"></div>
            <script>
                const container = document.getElementById('content');
                // stylizedSingle là văn bản gốc đã đổi font, không bị cắt mất chữ
                const text = '${stylizedSingle.replace("'", "\\'")}'; 
                const total = $countLoop; 
                const sep = '$separator';
                
                let current = 0;
                function appendBatch() {
                    if (current < total) {
                        let batch = "";
                        let limit = Math.min(current + 500, total);
                        for (let i = current; i < limit; i++) {
                            batch += text + sep;
                        }
                        container.innerHTML += batch;
                        current = limit;
                        if (current < total) {
                            setTimeout(appendBatch, 0);
                        }
                    }
                }
                appendBatch();
            </script>
        </body>
        </html>
        """.trimIndent()

            // 4. Load vào WebView
            binding.layoutResult.webViewResult.loadDataWithBaseURL(
                null, htmlData, "text/html", "UTF-8", null
            )
        }

        // --- Các nút chức năng (Copy, Share, Stylize) giữ nguyên như cũ ---
        isFavorited = false
        binding.layoutResult.ivFavourite.setImageResource(R.drawable.ic_heart_outline)

        binding.layoutResult.ivFavourite.setOnClickListener {
            isFavorited = !isFavorited
            if (isFavorited) {
                binding.layoutResult.ivFavourite.setImageResource(R.drawable.ic_heart_online)
                saveToFavorites(fullText)
            } else {
                binding.layoutResult.ivFavourite.setImageResource(R.drawable.ic_heart_outline)
            }
        }

        binding.layoutResult.btnStylize.clickSafe {
            val text = binding.edtInput.text.toString().trim()
            if (text.isNotEmpty()) {
                val intent = Intent(this, FontActivity::class.java)
                intent.putExtra(Constant.DATA, text)
                fontLauncher.launch(intent)
            } else {
                Toast.makeText(this, getString(R.string.toast_enter_text_first), Toast.LENGTH_SHORT).show()
            }
        }

        binding.layoutResult.btnCopy.setOnTouchScale {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Repeated Text", fullText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, getString(R.string.toast_copied_success), Toast.LENGTH_SHORT).show()
        }

        binding.layoutResult.btnSend.setOnTouchScale {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, fullText)
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
        binding.imvshare.setOnTouchScale {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, fullText)
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }

    private fun saveToFavorites(resultText: String) {
        val input = binding.edtInput.text.toString()
        val count = binding.edtCount.text.toString().toIntOrNull() ?: 0
        val isNewLine = binding.cbNewLine.isChecked

        val sharedPrefs = getSharedPreferences("text_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString("saved_list", null)
        val type = object : TypeToken<MutableList<SavedTextModel>>() {}.type
        val savedList: MutableList<SavedTextModel> =
            if (json == null) mutableListOf() else gson.fromJson(json, type)

        // Tạo model mới với đầy đủ thông tin
        // Trong RepeatActivity.kt -> hàm saveToFavorites
        if (currentEditingId != -1L) {
            val index = savedList.indexOfFirst { it.id == currentEditingId }
            if (index != -1) {
                savedList[index] = SavedTextModel(
                    id = currentEditingId,
                    originalText = input,
                    resultText = resultText,
                    repeatCount = count,
                    isNewLine = isNewLine,
                    fontIndex = selectedFontPosition // THÊM DÒNG NÀY VÀO PHẦN UPDATE
                )
                Toast.makeText(this, getString(R.string.toast_update_success), Toast.LENGTH_SHORT).show()
            }
        } else {
            // CHẾ ĐỘ THÊM MỚI: (Lưu cái mới toanh) -> BẠN ĐANG THIẾU PHẦN NÀY
            val newItem = SavedTextModel(
                id = System.currentTimeMillis(),
                originalText = input,
                resultText = resultText,
                repeatCount = count,
                isNewLine = isNewLine,
                fontIndex = selectedFontPosition
            )
            savedList.add(0, newItem) // Thêm vào đầu danh sách
            Toast.makeText(this, getString(R.string.toast_saved_favorites), Toast.LENGTH_SHORT).show()
        }

        // Lưu lại danh sách đã cập nhật
        sharedPrefs.edit().putString("saved_list", gson.toJson(savedList)).apply()

        // Sau khi save/update xong thì có thể đóng màn hình hoặc reset ID
        // finish()
    }

    private fun saveToRecent(resultText: String) {
        val input = binding.edtInput.text.toString()
        val count = binding.edtCount.text.toString().toIntOrNull() ?: 0
        val isNewLine = binding.cbNewLine.isChecked

        val sharedPrefs = getSharedPreferences("text_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString("recent_list", null)
        val type = object : TypeToken<MutableList<SavedTextModel>>() {}.type
        val recentList: MutableList<SavedTextModel> =
            if (json == null) mutableListOf() else gson.fromJson(json, type)

        val safeResultText = if (resultText.length > 50000) {
            resultText.take(1000) + "...(too long to save)"
        } else {
            resultText
        }
        // SỬA TẠI ĐÂY: Thêm fontIndex vào newItem
        val newItem = SavedTextModel(
            id = System.currentTimeMillis(),
            originalText = input,
            resultText = safeResultText,
            repeatCount = count,
            isNewLine = isNewLine,
            fontIndex = selectedFontPosition // Thêm dòng này để lưu font vào Recent
        )

        recentList.removeAll { it.resultText == resultText }
        recentList.add(0, newItem)

        if (recentList.size > 20) {
            recentList.removeAt(recentList.size - 1)
        }

        sharedPrefs.edit().putString("recent_list", gson.toJson(recentList)).apply()
    }

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityRepeatBinding {
        return ActivityRepeatBinding.inflate(layoutInflater)
    }
}
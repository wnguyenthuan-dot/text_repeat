package com.example.nt.app.textrepeat.ui.trending

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.base.BaseActivity
import com.example.nt.app.textrepeat.databinding.ActivitySavedTextBinding
import com.example.nt.app.textrepeat.databinding.ActivityTrendingBinding
import com.example.nt.app.textrepeat.model.TrendingModel
import com.example.nt.app.textrepeat.ui.adapter.TrendingAdapter
import com.example.nt.app.textrepeat.ui.repeat.RepeatActivity

class TrendingActivity : BaseActivity<ActivityTrendingBinding>() {
    private val trendingList = mutableListOf<TrendingModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrendingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
        setupRecyclerView()

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setupData() {
        val titles = resources.getStringArray(R.array.trending_titles)

        trendingList.clear()
        for (title in titles) {
            // Tạo số lần lặp ngẫu nhiên từ 50 đến 500 để trông đúng chất "Trending"
            val randomCount = (50..500).random()

            // Tạo nội dung lặp lại giả lập để hiển thị ở màn hình Trending
            val previewContent = "$title\n$title\n$title..."

            trendingList.add(TrendingModel(title, "Trending x$randomCount", previewContent))
        }
    }

    private fun setupRecyclerView() {
        binding.rvTrending.layoutManager = LinearLayoutManager(this)
        binding.rvTrending.adapter = TrendingAdapter(trendingList) { item, action ->
            when (action) {
                "ITEM_CLICK" -> {
                    // Khi click vào item thì chuyển sang màn hình Repeat
                    val intent = Intent(this, RepeatActivity::class.java).apply {
                        putExtra("EXTRA_INPUT", item.title)
                        // Lấy con số từ subtitle (ví dụ "Trending x150" -> 150)
                        val count = item.subtitle.replace("Trending x", "").toIntOrNull() ?: 100
                        putExtra("EXTRA_COUNT", count)
                    }
                    startActivity(intent)
                }
                "COPY" -> copyToClipboard(item.title) // Hoặc logic copy lặp lại tùy bạn
                "SHARE" -> shareContent(item.title)
            }
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Trending Text", text)
        clipboard.setPrimaryClip(clip)
        // Cách chuẩn nhất
        Toast.makeText(this, getString(R.string.msg_copied), Toast.LENGTH_SHORT).show()
    }

    private fun shareContent(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(intent, "Share via"))
    }
    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityTrendingBinding {
        return ActivityTrendingBinding.inflate(layoutInflater)
    }
}
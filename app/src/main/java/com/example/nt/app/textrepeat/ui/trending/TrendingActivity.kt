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
import com.example.nt.app.textrepeat.utils.ex.setOnTouchScale
import com.example.nt.app.textrepeat.utils.sharedpreference.SharePreferUtils
import com.example.nt.app.textrepeat.utils.sharedpreference.SharePreferUtils.getString
import com.example.nt.app.textrepeat.utils.sharedpreference.SharePreferUtils.saveKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrendingActivity : BaseActivity<ActivityTrendingBinding>() {
    private val trendingList = mutableListOf<TrendingModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrendingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
        setupRecyclerView()

        binding.btnBack.setOnTouchScale { finish() }
    }

    private fun setupData() {
        // Thử lấy danh sách đã lưu từ SharePreferUtils
        val savedList = SharePreferUtils.getTrendingList()

        if (savedList != null) {
            trendingList.clear()
            trendingList.addAll(savedList)
        } else {
            // Lần đầu vào app: Lấy từ strings.xml
            val titles = resources.getStringArray(R.array.trending_titles)
            trendingList.clear()
            for (title in titles) {
                val randomCount = (50..500).random()
                val previewContent = "$title\n$title\n$title..."
                trendingList.add(TrendingModel(title, "Trending x$randomCount", previewContent))
            }
            // Lưu lại bản gốc vào máy ngay lập tức
            SharePreferUtils.saveTrendingList(trendingList)
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
                "DELETE" -> {
                    // Tìm lại vị trí thực tế của item trong list hiện tại để đảm bảo an toàn
                    val positionInList = trendingList.indexOf(item)

                    if (positionInList != -1) {
                        // 1. Xóa trong Data Source
                        trendingList.removeAt(positionInList)

                        // 2. Thông báo xóa cho UI
                        binding.rvTrending.adapter?.notifyItemRemoved(positionInList)

                        // 3. QUAN TRỌNG: Cập nhật lại dải vị trí để tránh sai lệch cho các item còn lại
                        binding.rvTrending.adapter?.notifyItemRangeChanged(positionInList, trendingList.size)

                        // 4. Lưu lại vào máy
                        SharePreferUtils.saveTrendingList(trendingList)
                    }
                }
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

    //Trending

    fun saveTrendingList(list: List<TrendingModel>) {
        val json = Gson().toJson(list)
        saveKey("saved_trending_list", json)
    }

    fun getTrendingList(): MutableList<TrendingModel>? {
        val json = getString("saved_trending_list", "")
        if (json.isEmpty()) return null

        val type = object : TypeToken<MutableList<TrendingModel>>() {}.type
        return try {
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            null
        }
    }

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityTrendingBinding {
        return ActivityTrendingBinding.inflate(layoutInflater)
    }
}
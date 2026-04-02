package com.example.nt.app.textrepeat.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.databinding.ActivityMainBinding
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.nt.app.textrepeat.base.BaseActivity
import com.example.nt.app.textrepeat.model.SavedTextModel
import com.example.nt.app.textrepeat.ui.dialog.DialogExitApp
import com.example.nt.app.textrepeat.ui.quickrepeat.QuickRepeatActivity
import com.example.nt.app.textrepeat.ui.recent.RecentActivity
import com.example.nt.app.textrepeat.ui.repeat.RepeatActivity
import com.example.nt.app.textrepeat.ui.savetext.SavedTextActivity
import com.example.nt.app.textrepeat.ui.setting.SettingActivity
import com.example.nt.app.textrepeat.ui.trending.TrendingActivity
import com.example.nt.app.textrepeat.utils.ex.openActivity
import com.example.nt.app.textrepeat.utils.ex.setOnTouchScale
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()

    }

    private val dialogExitApp by lazy {
        DialogExitApp(this) { finishAffinity() }
    }
    private fun initUI() {
        binding.btnRepeatText.setOnClickListener {
            val intent = Intent(this, RepeatActivity::class.java)
            startActivity(intent)
        }
        binding.btnSavedText.setOnClickListener {
            startActivity(Intent(this, SavedTextActivity::class.java))
        }
        // 1. Quick Repeat
        binding.sectionQuickRepeat.let { section ->
            // Cài đặt nội dung Header
            section.root.findViewById<TextView>(R.id.tvSectionTitle).text = getString(R.string.quick_repeat)
            section.root.findViewById<ImageView>(R.id.ivSectionIcon).setImageResource(R.drawable.ic_quick_repeat)

            // Bấm vào nút "More" (btnMore) để mở màn hình QuickRepeatActivity
            section.root.findViewById<View>(R.id.btnMore).setOnClickListener {
                startActivity(Intent(this, QuickRepeatActivity::class.java))
            }

            // Hoặc bấm vào cả cái section cũng mở được (tùy bạn chọn)
            section.root.setOnClickListener {
                startActivity(Intent(this, QuickRepeatActivity::class.java))
            }
        }

        // 2. Recent
        binding.sectionRecent.let { section ->
            section.root.findViewById<TextView>(R.id.tvSectionTitle).text = getString(R.string.recent)
            section.root.findViewById<ImageView>(R.id.ivSectionIcon).setImageResource(R.drawable.ic_clock_recent)

            section.root.findViewById<View>(R.id.btnMore).setOnClickListener {
                startActivity(Intent(this, RecentActivity::class.java))
            }
            // Bắt sự kiện click vào cả cái section này
            section.root.setOnClickListener {
                startActivity(Intent(this, RecentActivity::class.java))
            }
        }

        // 3. Trending
        binding.sectionTrending.let { section ->
            section.root.findViewById<TextView>(R.id.tvSectionTitle).text = getString(R.string.trending)
            section.root.findViewById<ImageView>(R.id.ivSectionIcon).setImageResource(R.drawable.ic_fire_trending)

            section.root.findViewById<View>(R.id.btnMore).setOnClickListener {
                startActivity(Intent(this, TrendingActivity::class.java))
            }

            section.root.setOnClickListener {
                startActivity(Intent(this, TrendingActivity::class.java))
            }
        }
        binding.ivSettings.setOnTouchScale {
            openActivity(SettingActivity::class.java, false)
        }

        // 4. Saved Text
//        binding.sectionSavedBottom.let { section ->
//            section.root.findViewById<TextView>(R.id.tvSectionTitle).text = "Saved Text"
//            section.root.findViewById<ImageView>(R.id.ivSectionIcon).setImageResource(R.drawable.ic_save_white)
//        }
        loadQuickRepeatPreview()
    }
    private fun loadRecentPreview() {
        val sharedPrefs = getSharedPreferences("text_prefs", Context.MODE_PRIVATE)
        val json = sharedPrefs.getString("recent_list", null)
        val gson = Gson()

        // Tìm Header của section Recent để gán nút "More"
        val sectionRecent = binding.sectionRecent // Đây là id của include trong activity_main.xml
        val btnMore = sectionRecent.root.findViewById<View>(R.id.btnMore)

        btnMore?.setOnClickListener {
            startActivity(Intent(this, RecentActivity::class.java))
        }

        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<SavedTextModel>>() {}.type
            val recentList: MutableList<SavedTextModel> = gson.fromJson(json, type)

            // Lấy view của 2 item
            val leftItem = sectionRecent.root.findViewById<View>(R.id.itemRecentLeft)
            val rightItem = sectionRecent.root.findViewById<View>(R.id.itemRecentRight)

            // Hiển thị dữ liệu
            if (recentList.isNotEmpty()) {
                sectionRecent.root.visibility = View.VISIBLE

                // Đổ dữ liệu vào ô bên trái (Left)
                updateMiniItem(leftItem, recentList[0])

                // Đổ dữ liệu vào ô bên phải (Right) nếu có phần tử thứ 2
                if (recentList.size > 1) {
                    rightItem.visibility = View.VISIBLE
                    updateMiniItem(rightItem, recentList[1])
                } else {
                    // Nếu chỉ có 1 item thì ẩn ô bên phải đi
                    rightItem.visibility = View.INVISIBLE
                }
            } else {
                // Không có dữ liệu thì ẩn cả cụm Recent đi
                sectionRecent.root.visibility = View.GONE
            }
        } else {
            binding.sectionRecent.root.visibility = View.GONE
        }
    }

    // Hàm phụ để set text cho từng ô nhỏ
    private fun updateMiniItem(itemView: View, item: SavedTextModel) {
        val tvContent = itemView.findViewById<TextView>(R.id.tvContent)
        val tvCount = itemView.findViewById<TextView>(R.id.tvCount)

        // SỬA TẠI ĐÂY: Hiển thị resultText để thấy chữ nghệ thuật ngay tại màn Main
        // Dùng substring để tránh việc text quá dài làm vỡ giao diện mini
        val previewText = if (item.resultText.length > 50) {
            item.resultText.substring(0, 50) + "..."
        } else {
            item.resultText
        }
        tvContent.text = previewText

        tvCount.text = "x ${item.repeatCount}"

        // Click vào ô nhỏ
        itemView.setOnClickListener {
            val intent = Intent(this, RepeatActivity::class.java).apply {
                putExtra("EXTRA_ID", item.id)
                putExtra("EXTRA_INPUT", item.originalText)
                putExtra("EXTRA_COUNT", item.repeatCount)
                putExtra("EXTRA_NEWLINE", item.isNewLine)

                // QUAN TRỌNG NHẤT: Phải gửi thêm fontIndex đi
                // Nếu thiếu dòng này, RepeatActivity sẽ nhận giá trị mặc định là 0 (chữ thường)
                putExtra("EXTRA_FONT_INDEX", item.fontIndex)
            }
            startActivity(intent)
        }
    }
    private fun loadQuickRepeatPreview() {
        val sectionQuick = binding.sectionQuickRepeat

        // Lấy dữ liệu từ file arrays.xml (lấy đại diện mảng Love chẳng hạn)
        val loveArray = resources.getStringArray(R.array.love_message_titles)

        if (loveArray.isNotEmpty()) {
            val leftItem = sectionQuick.root.findViewById<View>(R.id.itemRecentLeft)
            val rightItem = sectionQuick.root.findViewById<View>(R.id.itemRecentRight)

            // Hiển thị item bên trái (Lấy câu đầu tiên trong mảng Love)
            updateQuickItem(leftItem, loveArray[0])

            // Hiển thị item bên phải (Lấy câu thứ 2)
            if (loveArray.size > 1) {
                rightItem.visibility = View.VISIBLE
                updateQuickItem(rightItem, loveArray[1])
            } else {
                rightItem.visibility = View.INVISIBLE
            }
        }
    }
    private fun updateQuickItem(itemView: View, content: String) {
        val tvContent = itemView.findViewById<TextView>(R.id.tvContent)
        val tvCount = itemView.findViewById<TextView>(R.id.tvCount)

        tvContent.text = content
        // Random một số để hiển thị cho đẹp
        val randomCount = (100..200).random()
        tvCount.text = "x $randomCount"

        itemView.setOnClickListener {
            val intent = Intent(this, RepeatActivity::class.java).apply {
                putExtra("EXTRA_INPUT", content)
                putExtra("EXTRA_COUNT", randomCount)
            }
            startActivity(intent)
        }
    }

    private fun loadTrendingPreview() {
        val sectionTrending = binding.sectionTrending

        // Lấy danh sách tiêu đề từ arrays.xml
        val trendingTitles = resources.getStringArray(R.array.trending_titles)

        if (trendingTitles.isNotEmpty()) {
            sectionTrending.root.visibility = View.VISIBLE

            val leftItem = sectionTrending.root.findViewById<View>(R.id.itemRecentLeft)
            val rightItem = sectionTrending.root.findViewById<View>(R.id.itemRecentRight)

            // Hiển thị item 1 (Vị trí 0)
            updateTrendingItem(leftItem, trendingTitles[0])

            // Hiển thị item 2 (Vị trí 1) nếu mảng có từ 2 phần tử trở lên
            if (trendingTitles.size > 1) {
                rightItem.visibility = View.VISIBLE
                updateTrendingItem(rightItem, trendingTitles[1])
            } else {
                rightItem.visibility = View.GONE
            }
        } else {
            sectionTrending.root.visibility = View.GONE
        }
    }
    private fun updateTrendingItem(itemView: View, title: String) {
        val tvContent = itemView.findViewById<TextView>(R.id.tvContent)
        val tvCount = itemView.findViewById<TextView>(R.id.tvCount)

        tvContent.text = title
        // Tạo số lần lặp ngẫu nhiên để trông giống "xu hướng"
        val randomCount = (200..999).random()
        tvCount.text = "x $randomCount"

        // Click vào item ở màn Main thì nhảy thẳng sang màn RepeatActivity
        itemView.setOnClickListener {
            val intent = Intent(this, RepeatActivity::class.java).apply {
                putExtra("EXTRA_INPUT", title)
                putExtra("EXTRA_COUNT", randomCount)
            }
            startActivity(intent)
        }
    }
    override fun onBack() {
        if (!dialogExitApp.isShowing) dialogExitApp.show()
    }
    override fun onResume() {
        super.onResume()
        loadRecentPreview()
        loadTrendingPreview()
    }
    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}
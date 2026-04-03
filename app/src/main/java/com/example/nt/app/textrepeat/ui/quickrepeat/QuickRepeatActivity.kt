package com.example.nt.app.textrepeat.ui.quickrepeat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.base.BaseActivity
import com.example.nt.app.textrepeat.databinding.ActivityQuickRepeatBinding
import com.example.nt.app.textrepeat.model.QuickCategory
import com.example.nt.app.textrepeat.model.QuickRepeatModel
import com.example.nt.app.textrepeat.ui.adapter.QuickRepeatParentAdapter
import com.example.nt.app.textrepeat.ui.repeat.RepeatActivity
import com.example.nt.app.textrepeat.utils.ex.setOnTouchScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuickRepeatActivity : BaseActivity<ActivityQuickRepeatBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Không cần inflate lại nếu BaseActivity đã làm, nhưng giữ theo cấu trúc của bạn:
        binding = ActivityQuickRepeatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initControl()
        loadData()
    }

    private fun initControl() {
        binding.btnBack.setOnTouchScale { finish() }

        // Thiết lập tiêu đề (đề phòng string dài)
        binding.tvTitle.text = getString(R.string.quick_repeat)

        binding.rvMain.layoutManager = LinearLayoutManager(this)
    }

    private fun loadData() {
        // Sử dụng Coroutine để nạp mảng từ resources tránh làm lag UI khi vào màn hình
        lifecycleScope.launch(Dispatchers.Default) {
            val arrayData = listOf(
                R.array.love_message_titles to "Love",
                R.array.goodnight_message_titles to "Good Night",
                R.array.apology_message_titles to "Apology",
                R.array.hbd_message_titles to "Birthday",
                R.array.attitude_message_titles to "Attitude",
                R.array.motivation_message_titles to "Motivation"
            )

            val categories = arrayData.map { (resId, title) ->
                val items = resources.getStringArray(resId).map {
                    // Random một con số count mặc định cho Quick Repeat (ví dụ 100)
                    QuickRepeatModel(it, 100, title)
                }
                QuickCategory(title, items)
            }

            withContext(Dispatchers.Main) {
                binding.rvMain.adapter = QuickRepeatParentAdapter(categories) { item ->
                    val intent = Intent(this@QuickRepeatActivity, RepeatActivity::class.java).apply {
                        putExtra("EXTRA_INPUT", item.content)
                        putExtra("EXTRA_COUNT", item.count)
                    }
                    startActivity(intent)
                }
            }
        }
    }

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityQuickRepeatBinding {
        return ActivityQuickRepeatBinding.inflate(layoutInflater)
    }
}
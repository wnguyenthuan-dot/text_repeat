package com.example.nt.app.textrepeat.ui.quickrepeat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.base.BaseActivity
import com.example.nt.app.textrepeat.databinding.ActivityQuickRepeatBinding
import com.example.nt.app.textrepeat.model.QuickCategory
import com.example.nt.app.textrepeat.model.QuickRepeatModel
import com.example.nt.app.textrepeat.ui.adapter.QuickRepeatAdapter
import com.example.nt.app.textrepeat.ui.adapter.QuickRepeatParentAdapter
import com.example.nt.app.textrepeat.ui.repeat.RepeatActivity

class QuickRepeatActivity : BaseActivity<ActivityQuickRepeatBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuickRepeatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        // Danh sách các mảng muốn hiển thị (Bạn thêm bớt ở đây là xong, không cần sửa UI)
        val arrayData = listOf(
            R.array.love_message_titles to "Love",
            R.array.goodnight_message_titles to "Good Night",
            R.array.apology_message_titles to "Apology",
            R.array.hbd_message_titles to "Birthday",
            R.array.attitude_message_titles to "Attitude",
            R.array.motivation_message_titles to "Motivation"
        )

        val categories = arrayData.map { (resId, title) ->
            val items = resources.getStringArray(resId).map { QuickRepeatModel(it, 0, title) }
            QuickCategory(title, items)
        }

        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = QuickRepeatParentAdapter(categories) { item ->
            val intent = Intent(this, RepeatActivity::class.java).apply {
                putExtra("EXTRA_INPUT", item.content)
                putExtra("EXTRA_COUNT", item.count)
            }
            startActivity(intent)
        }
    }
    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityQuickRepeatBinding {
        return ActivityQuickRepeatBinding.inflate(layoutInflater)
    }
}
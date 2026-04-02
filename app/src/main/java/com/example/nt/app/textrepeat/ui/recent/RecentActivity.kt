package com.example.nt.app.textrepeat.ui.recent

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.base.BaseActivity
import com.example.nt.app.textrepeat.databinding.ActivityRecentBinding // Nhớ tạo layout tương ứng
import com.example.nt.app.textrepeat.model.SavedTextModel
import com.example.nt.app.textrepeat.ui.adapter.SavedTextAdapter
import com.example.nt.app.textrepeat.ui.repeat.RepeatActivity
import com.example.nt.app.textrepeat.utils.ex.showToast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecentActivity : BaseActivity<ActivityRecentBinding>() {
    private lateinit var adapter: SavedTextAdapter
    private var recentList = mutableListOf<SavedTextModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun setupRecyclerView() {
        adapter = SavedTextAdapter(recentList,
            onCopy = { text ->
                try {
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = android.content.ClipData.newPlainText("Copied", text)
                    clipboard.setPrimaryClip(clip)
                    showToast(R.string.msg_copied)
                } catch (e: Exception) {
                    Toast.makeText(this, getString(R.string.toast_text_too_long), Toast.LENGTH_SHORT).show()
                }
            },
            onDelete = { position -> deleteItem(position) },
            onItemClick = { item ->
                val intent = Intent(this, RepeatActivity::class.java).apply {
                    putExtra("EXTRA_ID", item.id)
                    putExtra("EXTRA_INPUT", item.originalText)
                    putExtra("EXTRA_COUNT", item.repeatCount)
                    putExtra("EXTRA_NEWLINE", item.isNewLine)
                    putExtra("EXTRA_FONT_INDEX", item.fontIndex)
                }
                startActivity(intent)
            }
        )
        binding.rvRecent.layoutManager = LinearLayoutManager(this)
        binding.rvRecent.adapter = adapter
    }

    private fun loadData() {
        // Hiển thị loading nếu cần
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPrefs = getSharedPreferences("text_prefs", Context.MODE_PRIVATE)
            val json = sharedPrefs.getString("recent_list", null)

            if (!json.isNullOrEmpty()) {
                val type = object : TypeToken<MutableList<SavedTextModel>>() {}.type
                val data: MutableList<SavedTextModel> = Gson().fromJson(json, type)

                withContext(Dispatchers.Main) {
                    recentList.clear()
                    recentList.addAll(data)
                    adapter.notifyDataSetChanged()
                    binding.rvRecent.visibility = View.VISIBLE
                }
            } else {
                withContext(Dispatchers.Main) {
                    binding.rvRecent.visibility = View.GONE
                }
            }
        }
    }

    private fun deleteItem(position: Int) {
        recentList.removeAt(position)
        adapter.notifyItemRemoved(position)
        val sharedPrefs = getSharedPreferences("text_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("recent_list", Gson().toJson(recentList)).apply()
        if (recentList.isEmpty()) loadData()
    }
    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityRecentBinding {
        return ActivityRecentBinding.inflate(layoutInflater)
    }}
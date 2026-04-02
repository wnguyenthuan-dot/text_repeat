package com.example.nt.app.textrepeat.ui.savetext

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nt.app.textrepeat.base.BaseActivity
import com.example.nt.app.textrepeat.databinding.ActivityRepeatBinding
import com.example.nt.app.textrepeat.databinding.ActivitySavedTextBinding
import com.example.nt.app.textrepeat.model.SavedTextModel
import com.example.nt.app.textrepeat.ui.adapter.SavedTextAdapter
import com.example.nt.app.textrepeat.ui.repeat.RepeatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SavedTextActivity : BaseActivity<ActivitySavedTextBinding>() {
    private lateinit var adapter: SavedTextAdapter
    private var savedList = mutableListOf<SavedTextModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        setupRecyclerView()
        loadData()
    }

    private fun setupRecyclerView() {
        adapter = SavedTextAdapter(savedList, onCopy = { text ->
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(android.content.ClipData.newPlainText("Copied", text))
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show()
        }, onDelete = { position ->
            deleteItem(position)
        },onItemClick = { item ->
            // Mở lại RepeatActivity và gửi dữ liệu sang
            val intent = Intent(this, RepeatActivity::class.java)
            intent.putExtra("EXTRA_ID", item.id)
            intent.putExtra("EXTRA_INPUT", item.originalText)
            intent.putExtra("EXTRA_COUNT", item.repeatCount)
            intent.putExtra("EXTRA_NEWLINE", item.isNewLine)
            intent.putExtra("EXTRA_FONT_INDEX", item.fontIndex)

            startActivity(intent)
        })

        binding.rvSavedText.layoutManager = LinearLayoutManager(this)
        binding.rvSavedText.adapter = adapter
    }

    private fun loadData() {
        val sharedPrefs = getSharedPreferences("text_prefs", Context.MODE_PRIVATE)
        val json = sharedPrefs.getString("saved_list", null)
        val gson = Gson()

        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<SavedTextModel>>() {}.type
            val listFromServer: MutableList<SavedTextModel> = gson.fromJson(json, type)

            if (listFromServer.isNotEmpty()) {
                savedList.clear()
                savedList.addAll(listFromServer)
                adapter.updateData(savedList) // Cập nhật lại Adapter với list mới nhất

                binding.rvSavedText.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
            } else {
                showEmpty()
            }
        } else {
            showEmpty()
        }
    }

    private fun deleteItem(position: Int) {
        savedList.removeAt(position)
        adapter.notifyItemRemoved(position)

        // Lưu lại danh sách mới sau khi xóa
        val sharedPrefs = getSharedPreferences("text_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("saved_list", Gson().toJson(savedList)).apply()

        if (savedList.isEmpty()) showEmpty()
    }
    override fun onResume() {
        super.onResume()
        loadData()
    }
    private fun showEmpty() {
        binding.rvSavedText.visibility = View.GONE
        binding.layoutEmpty.visibility = View.VISIBLE
    }
    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivitySavedTextBinding {
        return ActivitySavedTextBinding.inflate(layoutInflater)
    }
}
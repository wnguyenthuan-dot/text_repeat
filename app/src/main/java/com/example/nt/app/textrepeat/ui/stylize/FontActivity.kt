package com.example.nt.app.textrepeat.ui.stylize

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import clickSafe
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.base.BaseActivity
import com.example.nt.app.textrepeat.databinding.ActivityFontBinding
import com.example.nt.app.textrepeat.model.FontEntity
import com.example.nt.app.textrepeat.ui.adapter.FontAdapter
import com.example.nt.app.textrepeat.utils.Constant
import com.example.nt.app.textrepeat.utils.ex.showToast
import setLinearLayoutManager

class FontActivity : BaseActivity<ActivityFontBinding>() {
    private var fontList = mutableListOf<FontEntity>()
    private val fontAdapter by lazy {
        FontAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // GỌI CÁC HÀM KHỞI TẠO TẠI ĐÂY
        initView()
        initData()
        initListener()
    }

    fun initView() {
        binding.rcvFont.setLinearLayoutManager(this, fontAdapter)
    }

    fun initData() {
        val defaultText = getString(R.string.app_name)
        var text = (intent.getStringExtra(Constant.DATA) ?: defaultText).trim().ifEmpty {
            defaultText
        }.take(50)
        text = substringLine(text, 2)
        val lastSelectedPos = intent.getIntExtra("EXTRA_SELECTED_POS", 0)
        fontAdapter.setDataList((0..108).toList().map { FontEntity(text, it) })
        fontAdapter.selectedPosition = lastSelectedPos

        // 4. (Tùy chọn) Cuộn RecyclerView đến đúng vị trí đang chọn để người dùng thấy ngay dấu tích
        binding.rcvFont.scrollToPosition(lastSelectedPos)

        fontAdapter.notifyDataSetChanged()
    }

    private fun substringLine(text: String, lineNumber: Int): String {
        val lines: List<String> = text.split("\n")

        val result = StringBuilder()

        for (i in 0 until lineNumber.coerceAtMost(lines.size)) {
            result.append(lines[i])
            if (i < lineNumber.coerceAtMost(lines.size) - 1) {
                result.append("\n")
            }
        }

        return result.toString()
    }

    fun initListener() {
        // Adapter đã tự lo việc cập nhật UI (selectedPosition) khi click
        // Bạn có thể để trống hoặc dùng để log debug
        fontAdapter.setOnClickItem { item, position ->
            val oldPos = fontAdapter.selectedPosition
            fontAdapter.selectedPosition = position

            // Cập nhật lại 2 item để hiện/ẩn dấu tích
            fontAdapter.notifyItemChanged(oldPos)
            fontAdapter.notifyItemChanged(position)
        }

        // Xử lý nút Done (Nút có hình ic_done trên toolbar)
        binding.imgDone.clickSafe {
            val pos = fontAdapter.selectedPosition

            // Kiểm tra vị trí hợp lệ trong dataList của BaseAdapter
            if (pos != -1 && pos < fontAdapter.dataList.size) {

                // Lấy item trực tiếp từ biến dataList của Base
                val selectedFont = fontAdapter.dataList[pos]

                val intent = Intent().apply {
                    // pathFont bây giờ sẽ hết đỏ vì selectedFont đã xác định là FontEntity
                    //putExtra(Constant.DATA, selectedFont.pathFont)
                    putExtra(Constant.DATA, pos)
                }
                setResult(RESULT_OK, intent)
                finish()
            } else {
                // Toast báo lỗi nếu chưa chọn style nào
                showToast(getString(R.string.msg_select_style))
            }
        }

        binding.imgBack.clickSafe {
            finish()
        }
    }

    override fun onBackPressed() {
        onBack()
    }

    override fun onBack() {
        finish()
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityFontBinding {
        return ActivityFontBinding.inflate(inflater)
    }
}
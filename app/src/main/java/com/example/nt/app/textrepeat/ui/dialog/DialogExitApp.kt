package com.example.nt.app.textrepeat.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.nt.app.textrepeat.databinding.DialogExitBinding
import com.example.nt.app.textrepeat.utils.ex.setOnTouchScale

class DialogExitApp(context: Context, val onExit: () -> Unit) : AlertDialog(context) {
    private var binding: DialogExitBinding // Giả sử file XML tên là dialog_exit.xml

    init {
        binding = DialogExitBinding.inflate(LayoutInflater.from(context))
        setView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnCancel.setOnTouchScale {
            dismiss()
        }

        binding.btnExit.setOnTouchScale {
            onExit.invoke()
            dismiss()
        }
    }
}
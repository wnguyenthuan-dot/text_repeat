package com.example.nt.app.textrepeat.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.dino.ads.admob.OnResumeUtils
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.databinding.LayoutDialogNoInternetBinding
import com.example.nt.app.textrepeat.utils.ex.setOnTouchScale

class DialogNoInternet(private val context: Context) {

    private val binding by lazy {
        LayoutDialogNoInternetBinding.inflate(LayoutInflater.from(context))
    }

    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(context, R.style.dialog_transparent_width).setView(binding.root)
            .create()
    }

    init {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    fun hide() {
        OnResumeUtils.enableOnResume(context::class.java)
        dialog.dismiss()
    }

    fun show(action: () -> Unit) {

        dialog.setCancelable(false)

        binding.txtAction.setOnTouchScale {
            OnResumeUtils.disableOnResume(context::class.java)
            action()
        }

        if (!dialog.isShowing)
            dialog.show()
    }

}
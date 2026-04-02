package com.example.nt.app.textrepeat.base

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.example.nt.app.textrepeat.ui.dialog.DialogNoInternet
import com.example.nt.app.textrepeat.utils.ex.isNetworkConnected
import com.example.nt.app.textrepeat.utils.sharedpreference.SharePreferUtils

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    companion object {
        const val ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"
    }

    lateinit var binding: VB

    // Thêm hàm này vào trong BaseActivity.kt
    override fun attachBaseContext(newBase: Context) {
        // Khởi tạo Utils nếu cần (đề phòng)
        SharePreferUtils.init(newBase)
        val langCode = getSavedLanguage(newBase)
        val context = updateLocale(newBase, langCode)
        super.attachBaseContext(context)
    }

    // Trong BaseActivity.kt
    private fun getSavedLanguage(context: Context): String {
        // Đừng dùng PreferenceManager nữa, hãy dùng chính cái Utils của bạn
        return SharePreferUtils.getString("languageCode", "en")
    }

    // Trong BaseActivity.kt - Cập nhật lại hàm updateLocale một chút cho chắc chắn
    private fun updateLocale(context: Context, langCode: String): Context {
        val locale = java.util.Locale(langCode.lowercase())
        java.util.Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        // Ép nạp lại để không bị cache
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        return context.createConfigurationContext(config)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        fullStatusBar()
        super.onCreate(savedInstanceState)
        binding = inflateViewBinding(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        hideNavigationBar()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
    open fun onBack() {
        finish()
    }

    override fun onResume() {
        super.onResume()
        try {
            registerReceiver(networkReceiver, IntentFilter(ACTION_NETWORK_CHANGE))
        } catch (_: Exception) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(networkReceiver)
        } catch (_: Exception) {
        }
    }
    val dialogNoInternet by lazy {
        DialogNoInternet(this)
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_NETWORK_CHANGE) {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (isNetworkConnected()) {
                        try {
                            dialogNoInternet.hide()
                        } catch (_: Exception) {
                        }
                        actionHasInternet()
                    } else {
                        try {
                            if (isShowDialogInternet()) {
                                dialogNoInternet.show {
                                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                                }
                            }
                        } catch (_: Exception) {
                        }
                    }
                }, 2500L)
            }
        }
    }
    open fun actionHasInternet() {

    }
    open fun isShowDialogInternet(): Boolean {
        return true
    }
    private fun fullStatusBar() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun hideNavigationBar() {
        val decorView: View = window.decorView
        val uiOptions: Int =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions
    }
    fun showInterBack(action: () -> Unit) {
//        AdsManager.loadAndShowInter(this, RemoteConfig.INTER_BACK) {
//            action.invoke()
//        }
    }

    abstract fun inflateViewBinding(inflater: LayoutInflater): VB
}
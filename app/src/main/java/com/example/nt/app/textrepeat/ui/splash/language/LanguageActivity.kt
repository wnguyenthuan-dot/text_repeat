package com.example.nt.app.textrepeat.ui.splash.language

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.dino.ads.admob.AdmobUtils
import com.dino.ads.admob.OnResumeUtils
import com.dino.ads.onboading.LanguageModel
import com.dino.ads.remote.NativeHolder
import com.dino.ads.remote.NativeMultiHolder
import com.dino.ads.utils.prefs
import com.dino.ads.utils.toast
import com.dino.rate.replaceActivity
import com.example.nt.app.textrepeat.RemoteConfig
import com.example.nt.app.textrepeat.ads.AdsManager
import com.example.nt.app.textrepeat.base.BaseActivity
import com.example.nt.app.textrepeat.databinding.ActivityLanguageBinding
import com.example.nt.app.textrepeat.ui.main.MainActivity
import com.example.nt.app.textrepeat.ui.splash.IntroActivity
import com.example.nt.app.textrepeat.utils.sharedpreference.SharePreferUtils

class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }
    // Đổi từ LanguageAdapter của thư viện sang MyLanguageAdapter của bạn
    private var myAdapter: MyLanguageAdapter? = null

    private val nativeLanguage: NativeMultiHolder = RemoteConfig.NATIVE_LANGUAGE
    private val nativeSmall: NativeHolder = RemoteConfig.NATIVE_LANGUAGE_SMALL
    private val nativeFull: NativeHolder = RemoteConfig.NATIVE_INTRO_FULL
    private val nativeIntro: NativeMultiHolder = RemoteConfig.NATIVE_INTRO

    private val fromSplash by lazy { intent.getBooleanExtra("fromSplash", false) }
    private val uninstall by lazy { intent.getBooleanExtra("uninstall", false) }

    private var lang: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initUI()
        setupRvLanguage()
    }

    private fun initUI() {
        binding.btnSubmit.visibility = View.GONE
        binding.ivNext.visibility = View.GONE

        binding.toolbar.setNavigationOnClickListener {
            if (!fromSplash) finish()
        }

        if (fromSplash || uninstall) {
            binding.toolbar.navigationIcon = null
            loadAdsForNextScreens()
            showNativeAds()
        }

        // Cả 2 nút đều gọi chung một hàm xử lý
        binding.ivNext.setOnClickListener { handleConfirm() }
        binding.btnSubmit.setOnClickListener { handleConfirm() }

        onBackPressedDispatcher.addCallback(this) {
            if (!fromSplash) finish()
        }
    }

    private fun handleConfirm() {
        if (lang.isBlank()) {
            toast("Please select a language before continue!")
        } else {
            setCurrentLang(lang)
            navigateToNext()
        }
    }

    private fun loadAdsForNextScreens() {
        AdmobUtils.loadNativeFull(this, nativeFull, object : AdmobUtils.NativeCallback() {})
        AdmobUtils.loadNativeIntro(this, nativeIntro, object : AdmobUtils.NativeCallback() {})
    }

    private fun showNativeAds() {
        AdmobUtils.showNative(
            this,
            nativeSmall,
            binding.flNativeSmall,
            object : AdmobUtils.NativeCallbackSimple() {})

        showNativeLanguage(0)
    }

    private fun showNativeLanguage(position: Int) {
        AdmobUtils.showNativeLanguage(
            this,
            nativeLanguage,
            binding.flNative,
            position,
            object : AdmobUtils.NativeCallbackSimple() {
                override fun onNativeLoaded() {
                    binding.loading.isVisible = false
                }
                override fun onNativeFailed(error: String) {
                    binding.loading.isVisible = false
                }
            })
    }

    private fun setupRvLanguage() {
        // 1. Tạo danh sách dữ liệu từ Enum
        val listLanguageEntity = LanguageEnum.values().map {
            LanguageEntity(enum = it, isSelected = false)
        }

        myAdapter = MyLanguageAdapter { position ->
            if (position < 0) return@MyLanguageAdapter

            // Lấy code từ danh sách entity khi người dùng CLICK
            lang = listLanguageEntity[position].enum.code
            myAdapter?.updatePosition(position)

            // HIỆN NÚT SUBMIT CHỈ KHI NGƯỜI DÙNG CLICK VÀO ITEM
            if (binding.btnSubmit.visibility != View.VISIBLE) {
                binding.btnSubmit.visibility = View.VISIBLE
                binding.btnSubmit.alpha = 0f
                binding.btnSubmit.animate().alpha(1f).setDuration(300).start()
            }
            showNativeLanguage(1)

        }.apply {
            submitList(listLanguageEntity)
            binding.rvLanguage.adapter = this

            // --- ĐOẠN NÀY ĐÃ ĐƯỢC CHỈNH SỬA ---
            // Chúng ta KHÔNG tự động hiện nút Submit ở đây nữa.
            // Nếu bạn muốn hiển thị vị trí đã chọn trước đó nhưng KHÔNG hiện nút Submit,
            // hãy giữ 'updatePosition' nhưng xóa 'binding.btnSubmit.visibility = View.VISIBLE'.
            // Còn nếu muốn trống trơn hoàn toàn thì xóa cả đoạn 'if (!fromSplash)' này.

            val current = currentLanguage(this@LanguageActivity)
            val index = listLanguageEntity.indexOfFirst { it.enum.code.equals(current, ignoreCase = true) }
            if (index != -1) {
                // Chỉ gán để biết ngôn ngữ hiện tại là gì, KHÔNG hiện nút
                // updatePosition(index) // Bỏ comment nếu muốn nó tự tick vào ngôn ngữ cũ nhưng không hiện nút Submit
            }
        }
    }

    private fun navigateToNext() {
        AdsManager.loadAndShowInter(this, RemoteConfig.INTER_LANGUAGE) {
            if (uninstall || fromSplash) {
                replaceActivity<IntroActivity>()
            } else {
                // Restart toàn bộ app để nạp lại Resource EN
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            finishAffinity() // Kết thúc toàn bộ các Activity liên quan
        }
    }

    // Thay thế hàm setCurrentLang cũ bằng hàm này
    // Trong LanguageActivity.kt
    private fun setCurrentLang(langCode: String?) {
        if (langCode != null) {
            SharePreferUtils.saveKey("languageCode", langCode.lowercase())
        }
    }

    override fun onResume() {
        super.onResume()
        OnResumeUtils.enableOnResume(javaClass)
    }

    companion object {
        // Sửa hàm này để đọc đúng từ PreferenceManager mà BaseActivity đang dùng
        fun currentLanguage(context: Context): String {
            // PHẢI dùng SharePreferUtils để đọc, không dùng PreferenceManager nữa
            return SharePreferUtils.getString("languageCode", "en")
        }

        val languages = listOf(
            LanguageModel(0, "English", "en"),
            LanguageModel(0, "हिंदी", "hi"),
            LanguageModel(0, "Español", "es"),
            LanguageModel(0, "Français", "fr"),
            LanguageModel(0, "عربي", "ar"),
            LanguageModel(0, "Português", "pt"),
            LanguageModel(0, "Bahasa Indonesia", "in"),
            LanguageModel(0, "Deutsch", "de"),
            LanguageModel(0, "Italiano", "it"),
            LanguageModel(0, "한국어", "ko")
        )
    }
}
package com.example.nt.app.textrepeat.ui.splash.language

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.databinding.ActivityLanguageBinding
import com.example.nt.app.textrepeat.ui.main.MainActivity
import com.example.nt.app.textrepeat.ui.splash.IntroActivity
import com.example.nt.app.textrepeat.utils.sharedpreference.SharePreferUtils

/** App-owned language picker, independent of the former onboarding/ads SDK. */
class LanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageBinding
    private var selectedCode = "en"
    private val itemViews = mutableMapOf<String, LinearLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selectedCode = SharePreferUtils.getString("languageCode", "en")

        listOf(
            "English" to "en", "العربية" to "ar", "বাংলা" to "bn",
            "Deutsch" to "de", "Español" to "es", "Français" to "fr", "हिन्दी" to "hi",
            "Bahasa Indonesia" to "in", "Italiano" to "it", "한국어" to "ko", "Português" to "pt", "Русский" to "ru"
        ).forEach { (name, code) -> addLanguageItem(name, code) }

        binding.ivBack.setOnClickListener { finish() }
        binding.btnContinue.setOnClickListener {
            SharePreferUtils.saveKey("languageCode", selectedCode)
            val target = if (intent.getBooleanExtra("fromSplash", false)) IntroActivity::class.java else MainActivity::class.java
            startActivity(Intent(this, target))
            finish()
        }
    }

    private fun addLanguageItem(name: String, code: String) {
        val item = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(-1, 56.dp).apply { bottomMargin = 12.dp }
            gravity = Gravity.CENTER_VERTICAL
            setPadding(16.dp, 0, 16.dp, 0)
            setOnClickListener { selectedCode = code; updateSelection() }
        }
        item.addView(TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, -2, 1f)
            text = name; textSize = 16f; setTextColor(Color.rgb(34, 34, 34)); typeface = Typeface.DEFAULT_BOLD
        })
        item.addView(ImageView(this).apply { setImageResource(R.drawable.ic_check) })
        itemViews[code] = item
        binding.languageList.addView(item)
        updateSelection()
    }

    private fun updateSelection() {
        itemViews.forEach { (code, item) ->
            item.background = ContextCompat.getDrawable(this, if (code == selectedCode) R.drawable.bg_item_language_selected else R.drawable.bg_item_language_unselected)
            (item.getChildAt(1) as ImageView).visibility = if (code == selectedCode) View.VISIBLE else View.INVISIBLE
        }
    }

    private val Int.dp get() = (this * resources.displayMetrics.density).toInt()
}

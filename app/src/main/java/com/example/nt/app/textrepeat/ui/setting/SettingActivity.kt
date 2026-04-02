package com.example.nt.app.textrepeat.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.base.BaseActivity
import com.example.nt.app.textrepeat.databinding.ActivityMainBinding
import com.example.nt.app.textrepeat.databinding.ActivitySettingBinding
import com.example.nt.app.textrepeat.ui.splash.language.LanguageActivity

class SettingActivity : BaseActivity<ActivitySettingBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener { finish() }

        binding.btnLanguage.setOnClickListener {
            // Mở lại màn Language nhưng truyền flag để biết là từ Setting vào
            val intent = Intent(this, LanguageActivity::class.java)
            intent.putExtra("fromSplash", false)
            startActivity(intent)
        }

        binding.btnShare.setOnClickListener {
            // Logic share app
        }

        binding.btnFeedback.setOnClickListener {
            // Logic gửi feedback (mail)
        }
    }
    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }
}
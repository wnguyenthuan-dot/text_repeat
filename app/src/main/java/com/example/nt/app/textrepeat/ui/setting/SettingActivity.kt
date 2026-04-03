package com.example.nt.app.textrepeat.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.ads.toast
import com.example.nt.app.textrepeat.base.BaseActivity
import com.example.nt.app.textrepeat.databinding.ActivityMainBinding
import com.example.nt.app.textrepeat.databinding.ActivitySettingBinding
import com.example.nt.app.textrepeat.ui.splash.language.LanguageActivity
import com.example.nt.app.textrepeat.utils.ex.setOnTouchScale

class SettingActivity : BaseActivity<ActivitySettingBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnTouchScale { finish() }

        binding.btnLanguage.setOnTouchScale {
            // Mở lại màn Language nhưng truyền flag để biết là từ Setting vào
            val intent = Intent(this, LanguageActivity::class.java)
            intent.putExtra("fromSplash", false)
            startActivity(intent)
        }

        binding.btnShare.setOnTouchScale {
            val appPackageName = packageName
            val playStoreLink = "https://play.google.com/store/apps/details?id=$appPackageName"

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                val shareMessage = "Check out this amazing Text Repeater app:\n$playStoreLink"
                putExtra(Intent.EXTRA_TEXT, shareMessage)
            }
            startActivity(Intent.createChooser(shareIntent, "Share app via"))
        }

        binding.btnFeedback.setOnTouchScale {
            Toast.makeText(this, "Developer", Toast.LENGTH_SHORT).show()
        }
    }
    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }
}
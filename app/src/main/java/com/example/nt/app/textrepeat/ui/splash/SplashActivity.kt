package com.example.nt.app.textrepeat.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.example.nt.app.textrepeat.base.BaseActivity
import com.example.nt.app.textrepeat.databinding.ActivitySplashBinding
import com.example.nt.app.textrepeat.utils.ex.hide
import com.example.nt.app.textrepeat.utils.ex.launchWhenResumed
import com.example.nt.app.textrepeat.utils.ex.openActivity
import com.example.nt.app.textrepeat.ui.main.MainActivity
import com.example.nt.app.textrepeat.ui.splash.language.LanguageActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    var splash: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null && intent.action == Intent.ACTION_MAIN
        ) {
            finish()
            return
        }
        splash = intent.data?.getQueryParameter("splash")
            ?: intent.getStringExtra("splash")
    }

    private var isActionHasInternetRan = false

    override fun actionHasInternet() {
        if (isActionHasInternetRan) {
            return
        }
        isActionHasInternetRan = true

        actionNext()
    }

    private fun actionNext() {
        launchWhenResumed {
            if (intent.action != Intent.ACTION_VIEW) {
                openActivity(LanguageActivity::class.java, bundleOf("fromSplash" to true), true)
                return@launchWhenResumed
            }
            openActivity(MainActivity::class.java, true)
        }
    }

    override fun onBack() {
    }


    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }
}

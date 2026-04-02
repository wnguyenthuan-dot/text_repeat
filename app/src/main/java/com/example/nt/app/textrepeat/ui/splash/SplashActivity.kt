package com.example.nt.app.textrepeat.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.dino.ads.admob.AdmobUtils
import com.dino.ads.admob.RemoteUtils
import com.example.nt.app.textrepeat.base.BaseActivity
import com.example.nt.app.textrepeat.databinding.ActivitySplashBinding
import com.example.nt.app.textrepeat.ads.Common
import com.example.nt.app.textrepeat.ui.uninstall.UninstallActivity
import com.example.nt.app.textrepeat.utils.ex.hide
import com.example.nt.app.textrepeat.utils.ex.launchWhenResumed
import com.example.nt.app.textrepeat.utils.ex.openActivity
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.RemoteConfig
import com.example.nt.app.textrepeat.ads.AdsManager
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
        Common.showRate = 0
    }

    private var isActionHasInternetRan = false

    override fun actionHasInternet() {
        if (isActionHasInternetRan) {
            return
        }
        isActionHasInternetRan = true

        Common.onAppOpen(this)
        RemoteUtils.init(
            this,
            R.xml.remote_config_defaults,
            null,
            logOrg = true
        ) {
            AdmobUtils.setupCMP(this) {
                AdmobUtils.initAdmob(this, AdsManager.isDebug)
                if (splash != "uninstall") {
                    AdsManager.loadNativeLanguage(this)
                }
                showInterOrAoa()
                //actionNext()
            }
        }
    }

    private fun showInterOrAoa() {
        AdmobUtils.loadAndShowAdSplash(
            this, RemoteConfig.ADS_SPLASH,
            object : AdmobUtils.InterCallback() {
                override fun onInterClosed() {
                    actionNext()
                }

                override fun onInterFailed(error: String) {
                    if (dialogNoInternet.isShowing()) return
                    binding.tvLoading.hide()
                    Handler(Looper.getMainLooper()).postDelayed({ actionNext() }, 3000)
                }
            })
    }

    private fun actionNext() {
        launchWhenResumed {
            if (intent.action != Intent.ACTION_VIEW) {
                openActivity(LanguageActivity::class.java, bundleOf("fromSplash" to true), true)
                return@launchWhenResumed
            }
            if (splash == "uninstall") {
                openActivity(UninstallActivity::class.java, true)
            }
        }
    }

    override fun onBack() {
    }


    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }
}
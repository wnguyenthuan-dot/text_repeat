package com.example.nt.app.textrepeat.ui.splash

import android.os.Bundle
import com.dino.ads.onboading.BaseIntroActivity
import com.dino.ads.remote.NativeHolder
import com.dino.ads.remote.NativeMultiHolder
import com.dino.rate.replaceActivity
import com.example.nt.app.textrepeat.RemoteConfig
import com.example.nt.app.textrepeat.ads.AdsManager
import com.example.nt.app.textrepeat.ads.Common
import com.example.nt.app.textrepeat.ui.main.MainActivity

class IntroActivity : BaseIntroActivity() {
    override var nativeIntro: NativeMultiHolder = RemoteConfig.NATIVE_INTRO
    override var nativeIntroFull: NativeHolder = RemoteConfig.NATIVE_INTRO_FULL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Common.setLocale(this)
    }

    override fun nextActivity() {
        AdsManager.loadAndShowInter(this, RemoteConfig.INTER_INTRO) {
            replaceActivity<MainActivity>()
        }
    }
}

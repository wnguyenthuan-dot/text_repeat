package com.example.nt.app.textrepeat.ui.splash.language

import com.dino.ads.onboading.BaseLanguageActivity
import com.dino.ads.remote.NativeHolder
import com.dino.ads.remote.NativeMultiHolder
import com.dino.rate.replaceActivity
import com.example.nt.app.textrepeat.RemoteConfig
import com.example.nt.app.textrepeat.ads.AdsManager
import com.example.nt.app.textrepeat.ui.main.MainActivity
import com.example.nt.app.textrepeat.ui.splash.IntroActivity

class LanguageActivity : BaseLanguageActivity() {
    override var nativeLanguage: NativeMultiHolder = RemoteConfig.NATIVE_LANGUAGE
    override var nativeSmall: NativeHolder = RemoteConfig.NATIVE_LANGUAGE_SMALL
    override var nativeFull: NativeHolder = RemoteConfig.NATIVE_INTRO_FULL
    override var nativeIntro: NativeMultiHolder = RemoteConfig.NATIVE_INTRO
    override fun nextActivity() {
        AdsManager.loadAndShowInter(this, RemoteConfig.INTER_LANGUAGE) {
            if (fromSplash) {
                replaceActivity<IntroActivity>()
            } else {
                replaceActivity<MainActivity>()
            }
        }
    }
}
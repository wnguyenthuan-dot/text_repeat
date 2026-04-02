package com.example.nt.app.textrepeat.ui.uninstall

import android.content.Intent
import com.dino.ads.onboading.uninstall.BaseUninstallActivity
import com.dino.ads.remote.NativeHolder
import com.example.nt.app.textrepeat.RemoteConfig
import com.example.nt.app.textrepeat.ui.splash.language.LanguageActivity
import kotlin.jvm.java

class UninstallActivity(override var nativeUninstall: NativeHolder = RemoteConfig.NATIVE_UNINSTALL) : BaseUninstallActivity() {

    override val activityBack: Class<*>
        get() = LanguageActivity::class.java

    override fun nextActivity() {
        startActivity(Intent(this, UninstallSurveyActivity::class.java))
    }
}
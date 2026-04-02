package com.example.nt.app.textrepeat.ui.uninstall

import com.dino.ads.onboading.uninstall.BaseUninstallSurveyActivity
import com.dino.ads.remote.NativeHolder
import com.example.nt.app.textrepeat.RemoteConfig
import com.example.nt.app.textrepeat.ui.splash.language.LanguageActivity
import kotlin.jvm.java

class UninstallSurveyActivity(override var nativeUninstallSurvey: NativeHolder = RemoteConfig.NATIVE_UNINSTALL_SURVEY) : BaseUninstallSurveyActivity() {

    override val activityBack: Class<*>
        get() = LanguageActivity::class.java

}
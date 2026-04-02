package com.example.nt.app.textrepeat

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.dino.ads.application.AdsApplication
import com.dino.ads.solar.SolarUtils

import com.example.nt.app.textrepeat.ads.AdsManager
import com.example.nt.app.textrepeat.ads.Common
import com.example.nt.app.textrepeat.utils.sharedpreference.SharePreferUtils

class BaseApplication : AdsApplication(), ActivityLifecycleCallbacks {

    override fun onCreateApplication() {
        application = this
        registerActivityLifecycleCallbacks(this)
        SolarUtils.init(
            context = this,
            appKey = getString(R.string.solar_key),
            debug = AdsManager.isDebug
        )

        //SharedPreferences
        SharePreferUtils.init(this)

        //PRDownloader - thư viện download
//        val config = PRDownloaderConfig.newBuilder()
//            .setReadTimeout(50000)
//            .setConnectTimeout(50000)
//            .build()
//        PRDownloader.initialize(applicationContext, config)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        //Common.setLocale(this)
    }

    override fun attachBaseContext(base: Context) {
        SharePreferUtils.init(base)
        val lang = SharePreferUtils.getString("languageCode", "en")
        val locale = java.util.Locale(lang)
        java.util.Locale.setDefault(locale)
        val config = base.resources.configuration
        config.setLocale(locale)
        super.attachBaseContext(base.createConfigurationContext(config))
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d("onActivityStarted", "onActivityStarted: $activity")
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var application: BaseApplication

        fun getAppInstance(): BaseApplication {
            return application
        }
    }
}
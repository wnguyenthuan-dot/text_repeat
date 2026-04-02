package com.example.nt.app.textrepeat.ads

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.graphics.toColorInt
import com.dino.ads.admob.OnResumeUtils
import com.dino.ads.admob.RemoteUtils
import com.dino.ads.onboading.BaseLanguageActivity
import com.dino.rate.RatingDialog
import com.dino.rate.prefs
import com.example.nt.app.textrepeat.BuildConfig
import com.example.nt.app.textrepeat.R
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.Locale
import kotlin.jvm.javaClass
import kotlin.plus

class Common {

    companion object {
        var showRate = 0

        fun onAppOpen(context: Context) {
            showRate = 0
            setCurrentLang(context, null)
            if (getFirstOpen(context)) {
                logEvent(context, "first_open_app")
                setFirstOpen(context, false)
            } else {
                logEvent(context, "open_app")
            }
        }

        private fun logEvent(context: Context, eventName: String?) {
            val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val bundle = Bundle()
            bundle.putString("onEvent", context.javaClass.simpleName)
            firebaseAnalytics.logEvent(
                eventName + "_" + BuildConfig.VERSION_CODE,
                bundle
            )
            Log.d("===Event", eventName + "_" + BuildConfig.VERSION_CODE)
        }

        fun currentLang(context: Context) = context.prefs().getString("languageCode", null)

        fun setCurrentLang(context: Context, langCode: String?) {
            context.prefs().edit { putString("languageCode", langCode) }
        }

        fun setLocale(context: Context) {
            val language = BaseLanguageActivity.currentLanguage(context) ?: "en"
            val myLocale = Locale(language)
            Locale.setDefault(myLocale)
            val resource = context.resources
            val displayMetrics = resource.displayMetrics
            val configuration = resource.configuration
            configuration.setLocale(myLocale)
            resource.updateConfiguration(configuration, displayMetrics)
        }

        fun setFirstOpen(context: Context, isFirstOpen: Boolean) {
            context.prefs().edit { putBoolean("FirstOpen", isFirstOpen) }
        }

        fun getFirstOpen(context: Context): Boolean {
            return context.prefs().getBoolean("FirstOpen", true)
        }

        fun showRate(context: Context) {
            showRate++
            if (showRate != 2) {
                return
            }
            val ratingDialog: RatingDialog = RatingDialog.Builder(context as Activity)
                .session(1)
                .date(1)
                .setNameApp(context.getString(R.string.app_name))
                .setIcon(R.mipmap.ic_launcher_round)
                .setEmail("info@mandala.pro.vn")
                .setOnlickRate {
                    OnResumeUtils.disableOnResume(context.javaClass)
                }
                .ignoreRated(false)
                .isShowButtonLater(true)
                .isClickLaterDismiss(true)
                .setTextButtonLater("Maybe Later")
                .setOnlickMaybeLate { }
                .ratingButtonColor("#1D7FFF".toColorInt())
                .build()
            ratingDialog.setCanceledOnTouchOutside(false)
            ratingDialog.show()
        }

        fun requestNotificationPermission(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permissionState = ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                )
                if (permissionState == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        1
                    )
                }
            }
        }

        private fun getBaseUrl() = RemoteUtils.getValue("base_url")
        fun getUrlThumb() = getBaseUrl() + "/thumb/"
    }
}
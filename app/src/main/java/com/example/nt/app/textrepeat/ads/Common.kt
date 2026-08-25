package com.example.nt.app.textrepeat.ads

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.nt.app.textrepeat.utils.sharedpreference.SharePreferUtils
import java.util.Locale
import kotlin.jvm.javaClass
import kotlin.plus

class Common {

    companion object {
        var showRate = 0

        fun setLocale(context: Context) {
            val language = SharePreferUtils.getString("languageCode", "en")
            val myLocale = Locale(language)
            Locale.setDefault(myLocale)
            val resource = context.resources
            val displayMetrics = resource.displayMetrics
            val configuration = resource.configuration
            configuration.setLocale(myLocale)
            resource.updateConfiguration(configuration, displayMetrics)
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

        fun getUrlThumb() = ""
    }
}

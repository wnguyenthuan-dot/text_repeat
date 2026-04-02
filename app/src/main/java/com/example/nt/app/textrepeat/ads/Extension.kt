package com.example.nt.app.textrepeat.ads

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.BounceInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.nt.app.textrepeat.BuildConfig
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.jvm.java
import kotlin.math.roundToInt


fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(this, msg, length).show()
    }
}

fun log(msg: String) {
    if (BuildConfig.DEBUG) Log.d("===", msg)
}

fun Context.prefs(): SharedPreferences {
    return getSharedPreferences("APP_PREFS", MODE_PRIVATE)
}
internal fun View.gone() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}
internal fun View.visible() {
    visibility = View.VISIBLE
}

fun View.hidden() {
    visibility = View.INVISIBLE
}

fun View.showWithAnim() {
    animate().alpha(1f).setDuration(20).start()
}

fun View.hideWithAnim() {
    animate().alpha(0f).setDuration(50).start()
//    visibility = View.VISIBLE
}

fun View.fadeOut(duration: Long, endAction: (() -> Unit)? = null) {
    animate().alpha(0f).setDuration(duration).withEndAction {
        isVisible = false
        endAction?.invoke()
    }.start()
}

fun View.fadeIn(duration: Long) {
    alpha = 0f
    isVisible = true
    animate().alpha(1f).setDuration(duration).start()
}

//fun Number.dpToPx(): Int {
//    val metric = Resources.getSystem().displayMetrics
//    return (this.toFloat() * (metric.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
//}
fun Number.dpToPx(): Int {
    return (this.toFloat() * Resources.getSystem().displayMetrics.density).roundToInt()
}

fun Number.dpToPxF(): Float {
    return this.toFloat() * Resources.getSystem().displayMetrics.density
}

fun runTryCatch(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun AlertDialog.setupDialog(activity: Activity) {
    window!!.setBackgroundDrawableResource(R.color.transparent)
    window!!.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    )
    window!!.decorView.systemUiVisibility = activity.window.decorView.systemUiVisibility

    setOnShowListener {
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        val wm =
            activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.updateViewLayout(window!!.decorView, window!!.attributes)
    }
//    window?.setDimAmount(0.8f)
    window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
}

fun Context.showKeyboard(view: View?) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
//    val inputMethodManager =
//        checkNotNull(getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?)
//    inputMethodManager.showSoftInput(view, 0)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        checkNotNull(getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?)
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun logTime(msg: String) {
    log("$msg: ${System.currentTimeMillis().convertToTime()}")
}

fun Long.convertToTime(): String {
    val date = Date(this)
    val format = SimpleDateFormat("mm:ss.SSS", Locale.getDefault())
    return format.format(date)
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}

fun View.getMarginTopAndStartFromRoot(root: View): Pair<Int, Int> {
    val viewLocation = IntArray(2)
    getLocationOnScreen(viewLocation)

    val rootLocation = IntArray(2)
    root.getLocationOnScreen(rootLocation)

    val marginTop = viewLocation[1] - rootLocation[1]
    val marginStart = viewLocation[0] - rootLocation[0]
    return Pair(marginTop, marginStart)
}

fun getAppIcon(context: Context, packageName: String): Drawable? {
    return try {
        val packageManager = context.packageManager
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        packageManager.getApplicationIcon(applicationInfo)
    } catch (e: PackageManager.NameNotFoundException) {
        // Handle the case where the package is not found
        null
    }
}

inline fun <reified T : Activity> Context.addActivity() {
    startActivity(Intent(this, T::class.java))
}

fun View.setOnClickBounceListener(
    scaleBounce: Float = 0.95f,
    duration: Long = 300,
    onClickListener: () -> Unit
) {
    setOnClickListener {
        it.scaleX = scaleBounce
        it.scaleY = scaleBounce
        it.animate().scaleX(1f).scaleY(1f).setDuration(duration).interpolator = BounceInterpolator()
        onClickListener()
    }
//    this.enableAfter(200)
}

 fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}
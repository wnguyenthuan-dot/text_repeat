import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

fun Activity.showSnackBar(msg: String, duration: Int = 2000) {
    val view = window.decorView.findViewById<View>(R.id.content)
    view?.let {
        val snackBar = Snackbar.make(it, msg, duration)
//            .setTextColor(getColorById(R.color.text_selected))
        val snackView = snackBar.view
//        snackView.setBackgroundColor(getColorById(R.color.color_app))
        snackBar.show()
    }
}


fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun TextView.clear() {
    this.text = ""
}

fun TextView.setFont(pathFont: String) {
    val face = Typeface.createFromAsset(
        this.context.assets,
        pathFont
    )
    this.typeface = face
}

fun RecyclerView.setLinearLayoutManager(
    context: Context,
    adapter: RecyclerView.Adapter<*>,
    orientation: Int = RecyclerView.VERTICAL
) {
    val manager = LinearLayoutManager(context)
    manager.orientation = orientation
    this.layoutManager = manager
    this.adapter = adapter
}

fun Context.openActivity(pClass: Class<out Activity>, isFinish: Boolean = false) {
    openActivity(pClass, null)
    if (isFinish) {
        (this as Activity).finish()
    }
}

fun Context.openActivity(pClass: Class<out Activity>, bundle: Bundle?) {
    val intent = Intent(this, pClass)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.showWithAnimation() {
    this.visibility = View.VISIBLE
    //this.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.slide_in_bottom))
}

fun View.hideWithAnimation() {
    this.visibility = View.INVISIBLE
    //this.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.slide_out_bottom))
}

fun View.disableView() {
    this.isClickable = false
    this.postDelayed({ this.isClickable = true }, 500)
}

class SafeClickListener(val onSafeClickListener: (View) -> Unit) : View.OnClickListener {
    override fun onClick(v: View) {
        v.disableView()
        onSafeClickListener(v)
    }
}

fun View.setOnSafeClick(onSafeClickListener: (View) -> Unit) {
    val safeClick = SafeClickListener {
        onSafeClickListener(it)
    }
    setOnClickListener(safeClick)
}

fun Context.showDialogRate(onRate: (rateNum: Int) -> Unit, onClose: (() -> Unit)? = null) {
//    RateAppDialog(this).also { dialog ->
//        dialog.show(onClickSubmit = { starNum ->
//            onRate.invoke(starNum)
//        }, onClickClose = { onClose?.invoke() })
//    }
}

fun Context.showDialogGiveOpinion(
    onSubmit: (opinion: String) -> Unit,
    onClose: (() -> Unit)? = null
) {
//    GiveOpinionDialog(this).also { dialog ->
//        dialog.show(onClickSubmit = {
//            onSubmit.invoke(it)
//        }, onClickCloseOrLater = { onClose?.invoke() })
//    }
}

fun Context.sendEmail(toEmail: String, content: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    val data = ("mailto:"
            + toEmail
            + "?subject=Opinion for Intense Vibrator" + "&body=" + content).toUri()
    intent.data = data
    try {
        this.startActivity(intent)
    } catch (ex: Exception) {
        Toast.makeText(
            this,
            "Not have email app to send email!",
            Toast.LENGTH_SHORT
        ).show()
        ex.printStackTrace()
    }
}

var isAvailableClick = true
fun handleAvailableClick() {
    Handler(Looper.getMainLooper()).postDelayed({
        isAvailableClick = true
    }, 200)
}

fun View.clickSafe(action: () -> Unit) {
    this.setOnClickListener {
        if (isAvailableClick) {
            isAvailableClick = false
            handleAvailableClick()
            action.invoke()
        }
    }
}
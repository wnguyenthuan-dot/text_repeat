package com.example.nt.app.textrepeat.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nt.app.textrepeat.R
import com.example.nt.app.textrepeat.ui.main.MainActivity

/** Local, ad-free three-step introduction. */
class IntroActivity : AppCompatActivity() {
    private var page = 0
    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var content: TextView
    private lateinit var next: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER; setPadding(40, 40, 40, 40) }
        image = ImageView(this).apply { layoutParams = LinearLayout.LayoutParams(-1, 0, 1f); scaleType = ImageView.ScaleType.CENTER_INSIDE }
        title = TextView(this).apply { textSize = 26f; gravity = Gravity.CENTER }
        content = TextView(this).apply { textSize = 18f; gravity = Gravity.CENTER; setPadding(0, 16, 0, 32) }
        next = Button(this).apply { setOnClickListener { advance() } }
        root.addView(image); root.addView(title); root.addView(content); root.addView(next)
        setContentView(root); showPage()
    }

    private fun advance() { if (page++ == 2) { startActivity(Intent(this, MainActivity::class.java)); finish() } else showPage() }
    private fun showPage() {
        val images = intArrayOf(R.drawable.im_intro1, R.drawable.im_intro2, R.drawable.im_intro3)
        val titles = intArrayOf(R.string.intro1, R.string.intro2, R.string.intro3)
        val contents = intArrayOf(R.string.intro1_content, R.string.intro2_content, R.string.intro3_content)
        image.setImageResource(images[page]); title.setText(titles[page]); content.setText(contents[page])
        next.text = if (page == 2) getString(R.string.start) else getString(R.string.next)
    }
}

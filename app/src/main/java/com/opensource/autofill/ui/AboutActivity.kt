package com.opensource.autofill.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import com.opensource.autofill.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val FIREFLY_APP = "https://github.com/emansih/FireflyMobile"
        val FIREFLY_SERVER = "https://github.com/firefly-iii/firefly-iii"
        val PERSONAL_GITHUB= "https://github.com/luifermoron"

        makeTextLink(findViewById(R.id.autofill_description), "More.", true, R.color.color_one, action = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(FIREFLY_APP))) })
        makeTextLink(findViewById(R.id.firefly_description), "More.", true, R.color.color_one, action = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(FIREFLY_SERVER))) })
        makeTextLink(findViewById(R.id.author_description), "More.", true, R.color.color_one, action = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PERSONAL_GITHUB))) })
    }


    private fun makeTextLink(textView: TextView, str: String, underlined: Boolean, color: Int?, action: (() -> Unit)? = null) {
        val spannableString = SpannableString(textView.text)
        val textColor = color ?: textView.currentTextColor
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                action?.invoke()
            }
            override fun updateDrawState(drawState: TextPaint) {
                super.updateDrawState(drawState)
                drawState.isUnderlineText = underlined
                drawState.color = textColor
            }
        }
        val index = spannableString.indexOf(str)
        spannableString.setSpan(clickableSpan, index, index + str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT
    }
}
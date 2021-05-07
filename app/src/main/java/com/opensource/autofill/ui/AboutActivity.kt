package com.opensource.autofill.ui
/*
  ~ Copyright (c)  2021 Luis Morón
  ~
  ~ This program is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ This program is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
import android.content.Intent
import android.content.res.Configuration
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
        supportActionBar?.hide()

        val FIREFLY_APP = "https://github.com/emansih/FireflyMobile"
        val OCR_FIREFLY_APP = "https://github.com/luifermoron/ocrFireflyMobile"
        val FIREFLY_SERVER = "https://github.com/firefly-iii/firefly-iii"
        val PERSONAL_GITHUB= "https://github.com/luifermoron"
        val mode = resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        val color = if (mode == Configuration.UI_MODE_NIGHT_YES) R.color.white else R.color.color_one

<<<<<<< HEAD
        makeTextLink(findViewById(R.id.autofill_description), "Firefly App", true, R.color.color_one, action = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(FIREFLY_APP))) })
        makeTextLink(findViewById(R.id.autofill_description), "More.", true, R.color.color_one, action = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(OCR_FIREFLY_APP))) })
        makeTextLink(findViewById(R.id.firefly_description), "More.", true, R.color.color_one, action = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(FIREFLY_SERVER))) })
        makeTextLink(findViewById(R.id.author_description), "More.", true, R.color.color_one, action = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PERSONAL_GITHUB))) })
=======
        makeTextLink(findViewById(R.id.autofill_description), "More.", true, color, action = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(FIREFLY_APP))) })
        makeTextLink(findViewById(R.id.firefly_description), "More.", true, color, action = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(FIREFLY_SERVER))) })
        makeTextLink(findViewById(R.id.author_description), "More.", true, color, action = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PERSONAL_GITHUB))) })
>>>>>>> develop
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
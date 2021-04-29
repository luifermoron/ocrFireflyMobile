package com.opensource.autofill

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
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.opensource.autofill.model.ocr.OCRTag
import com.opensource.autofill.ui.configuration.ConfigurationViewModel


class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val tagViewModel: ConfigurationViewModel by viewModels()
        tagViewModel.getAllOCRTags().observe(this, Observer<List<OCRTag>> { tags ->
            MainActivity.open(SplashScreenActivity@ this, !tags.isEmpty(), getImageUri())
            SplashScreenActivity@ this.finishAffinity()
        })
    }

    private fun getImageUri() : String? {
        val intent = intent
        val action = intent.action
        val type = intent.type
        if (Intent.ACTION_SEND == action && type != null) {
            if (type.startsWith("image/")) {
                return intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM).toString()
            }
        }
        return null
    }

}
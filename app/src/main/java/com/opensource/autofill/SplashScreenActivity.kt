package com.opensource.autofill


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
        supportActionBar?.hide()

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
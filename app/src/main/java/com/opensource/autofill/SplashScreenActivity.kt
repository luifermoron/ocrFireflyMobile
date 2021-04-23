package com.opensource.autofill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.opensource.autofill.ui.ViewModelFactory
import com.opensource.autofill.ui.configuration.ConfigurationViewModel
import androidx.lifecycle.observe
import com.opensource.autofill.model.ocr.OCRTag

class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val tagViewModel: ConfigurationViewModel by viewModels()
        tagViewModel.getAllOCRTags().observe(this, Observer<List<OCRTag>>{ tags ->
           MainActivity.open(SplashScreenActivity@this, !tags.isEmpty())
        })
    }
}
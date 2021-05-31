package com.opensource.autofill.ui.home
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
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.google.mlkit.vision.demo.kotlin.textdetector.TextRecognitionProcessor
import com.opensource.autofill.R
import com.opensource.autofill.databinding.FragmentHomeBinding
import com.opensource.autofill.ocr.OCRResult
import com.opensource.autofill.ocr.TagParser
import com.opensource.autofill.ui.AboutActivity
import com.opensource.autofill.ui.configuration.ConfigurationViewModel
import com.opensource.autofill.ui.configuration.getViewModelFactory
import com.opensource.autofill.ui.mlkit.BitmapUtils
import com.opensource.autofill.ui.mlkit.VisionImageProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*


class HomeFragment : Fragment(), View.OnClickListener, OCRResult {

    private var hasAppFireflyInstalled: Boolean = false
    private var imageUri: Uri? = null
    private var imageProcessor: VisionImageProcessor? = null

    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private val binding get() = fragmentHomeBinding!!

    private val tagViewModel by activityViewModels<ConfigurationViewModel> { getViewModelFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        view.findViewById<Button>(R.id.select_button).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.about_page).setOnClickListener(this)
        return view
    }

    private fun createImageProcessor() {
        try {
            imageProcessor = TextRecognitionProcessor(requireActivity(), this)
        } catch (e: Exception) {
        }
    }

    private fun launchImageUri() {
        val intentImageUri = requireActivity()?.intent.getStringExtra("imageUri")
        if (intentImageUri != null) {
            imageUri = Uri.parse(intentImageUri)
            Log.d(TAG, imageUri.toString())
            processImage()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createImageProcessor()
        launchImageUri()
    }

    override fun onResume() {
        super.onResume()
        val fireflyIntent = Intent()
        fireflyIntent.action = "firefly.hisname.PREFILL_TRANSACTION"
        hasAppFireflyInstalled = isInstalled(fireflyIntent)
        if (hasAppFireflyInstalled) {
            binding.textHome.setText(R.string.home_app_installed_description)
            binding.selectButton.setText(R.string.select_image)
        } else {
            binding.textHome.setText(R.string.home_app_not_installed_description)
            binding.selectButton.setText(R.string.download_app)
        }
    }

    private fun isInstalled(intent: Intent): Boolean {
        val list = requireActivity().packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list.size > 0
    }

    override fun onClick(v: View?) {
        v?.let {
            val id: Int = it.id
            when (id) {
                R.id.select_button -> {
                    if (hasAppFireflyInstalled)
                        chooseImageFromGallery()
                    else
                        downloadFirefly()
                }
                R.id.about_page -> {
                    startActivity(Intent(requireActivity(), AboutActivity::class.java))
                }
            }
        }
    }

    fun downloadFirefly() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/emansih/FireflyMobile")))
    }

    fun chooseImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(
                Intent.createChooser(intent, "Select an Image"),
                REQUEST_CHOOSE_IMAGE
        )
    }

    private fun processImage() {
        Log.d(TAG, "Try reload and detect image")
        try {
            if (imageUri == null) {
                Toast.makeText(requireActivity().applicationContext, "Not image found", Toast.LENGTH_LONG).show()
                Log.d(TAG, "imageUri == null")
                return
            }

            val imageBitmap = BitmapUtils.getBitmapFromContentUri(
                    requireActivity().contentResolver,
                    imageUri
            )
            if (imageBitmap == null) {
                Toast.makeText(requireActivity().applicationContext, "Could not process image", Toast.LENGTH_LONG).show()
                Log.d(TAG, "imageBitmap == null")
                return
            }

            if (imageProcessor != null) {
                Log.d(TAG, "imageProcessor != null")
                imageProcessor!!.processBitmap(imageBitmap)
                binding.animationView.playAnimation()
            } else {
                Toast.makeText(requireActivity().applicationContext, "Something weird happened! Contact me", Toast.LENGTH_LONG).show()
                Log.e(
                        TAG,
                        "Null imageProcessor, please check adb logs for imageProcessor creation error"
                )
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error retrieving saved image")
            imageUri = null
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data!!.data
            Log.d(TAG, imageUri.toString())
            processImage()
        }
    }

    override fun showOCRResult(text: String?) {
        text?.let { it
            tagViewModel.getAllOCRTags().observe(viewLifecycleOwner) { tags ->
                val tagParser = TagParser()
                val description: String = tagParser.findTextOn(tagViewModel.descriptionSringList(tags), TagParser.buildGetExactText(it))
                val amount: String = tagParser.findTextOn(tagViewModel.amountStringList(tags), TagParser.buildGetExactText(it)) //TagParser.buildGetSimilarText(it, "\\d*\\.?\\d+")
                Log.d(TAG, "heyy")
                Log.d(TAG, description)
                Log.d(TAG, amount)

                if (description == "" && amount == "") {
                    Toast.makeText(requireActivity().applicationContext, "Could not find anything", Toast.LENGTH_LONG).show()
                    GlobalScope.launch {
                        delay(1000L)
                        launch(Dispatchers.Main) {
                            binding.animationView.pauseAnimation()
                            binding.animationView.progress = 0.0f
                        }
                    }
                } else {
                    GlobalScope.launch {
                        delay(1000L)
                        launch(Dispatchers.Main) {
                            binding.animationView.pauseAnimation()
                            binding.animationView.progress = 0.0f
                            openFireflyApp(description, amount)
                        }
                    }
                }
            }
        }
    }
    private fun openFireflyApp(description: String, amount: String) {
        val fireflyIntent = Intent()
        fireflyIntent.action = "firefly.hisname.PREFILL_TRANSACTION"
        fireflyIntent.apply {
            putExtra("description", description)
            putExtra("amount", amount)
            putExtra("transactionType", "withdrawal")
        }
        startActivity(fireflyIntent)
    }

    companion object {
        private const val TAG = "HomeFragment"

        private const val REQUEST_CHOOSE_IMAGE = 1
    }
}
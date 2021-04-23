package com.opensource.autofill.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.mlkit.vision.demo.kotlin.textdetector.TextRecognitionProcessor
import com.opensource.autofill.R
import com.opensource.autofill.ocr.OCRResult
import com.opensource.autofill.ocr.TagParser
import com.opensource.autofill.ui.configuration.ConfigurationViewModel
import com.opensource.autofill.ui.configuration.getViewModelFactory
import com.opensource.autofill.ui.mlkit.BitmapUtils
import com.opensource.autofill.ui.mlkit.VisionImageProcessor
import java.io.IOException
import java.util.*


class HomeFragment : Fragment(), View.OnClickListener, OCRResult {

    private var imageUri: Uri? = null
    private var imageProcessor: VisionImageProcessor? = null

    private val tagViewModel by activityViewModels<ConfigurationViewModel> { getViewModelFactory() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        root.findViewById<Button>(R.id.select_button).setOnClickListener(this)
        return root
    }

    private fun createImageProcessor() {
        try {
            imageProcessor = TextRecognitionProcessor(requireActivity(), this)
        } catch (e: Exception) {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createImageProcessor()
    }

    override fun onClick(v: View?) {
        v?.let {
            val id: Int = it.id

            when (id) {
                R.id.select_button -> {
                    chooseImageFromGallery()
                }
            }
        }
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
    private fun tryReloadAndDetectInImage() {
        Log.d(TAG, "Try reload and detect image")
        try {
            if (imageUri == null) {
                Log.d(TAG, "imageUri == null")
                return
            }

            val imageBitmap = BitmapUtils.getBitmapFromContentUri(
                    requireActivity().contentResolver,
                    imageUri
            )
            if (imageBitmap == null) {
                Log.d(TAG, "imageBitmap == null")
                return
            }

            if (imageProcessor != null) {
                Log.d(TAG, "imageProcessor != null")
                imageProcessor!!.processBitmap(imageBitmap)
            } else {
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
            Log.d("HomeFragment", imageUri.toString())
            tryReloadAndDetectInImage()
        }
    }


    companion object {
        private const val TAG = "HomeFragment"

        private const val SIZE_SCREEN = "w:screen" // Match screen width
        private const val SIZE_1024_768 = "w:1024" // ~1024*768 in a normal ratio
        private const val SIZE_640_480 = "w:640" // ~640*480 in a normal ratio

        private const val REQUEST_CHOOSE_IMAGE = 1
    }

    override fun showOCRResult(text: String?) {
        text?.let { it
            tagViewModel.getAllOCRTags().observe(viewLifecycleOwner) { tags ->
                val tagParser = TagParser()
                val description: String = tagParser.findTextOn(tagViewModel.descriptionSringList(tags), TagParser.buildGetExactText(it))
                val amount: String = tagParser.findTextOn(tagViewModel.amountStringList(tags), TagParser.buildGetExactText(it)) //TagParser.buildGetSimilarText(it, "\\d*\\.?\\d+")
                Log.d("HomeFragment", "heyy")
                Log.d("HomeFragment", description)
                Log.d("HomeFragment", amount)
                openOtherApp(description, amount)
            }
        }
    }

    fun getShareIntent(activity: Activity, type: String?): Intent? {
        var found = false
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"

        // gets the list of intents that can be loaded.
        val resInfo = activity.packageManager.queryIntentActivities(share, 0)
        println("resinfo: $resInfo")
        if (!resInfo.isEmpty()) {
            for (info in resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type!!) ||
                        info.activityInfo.name.toLowerCase().contains(type)) {
                    share.setPackage(info.activityInfo.packageName)
                    found = true
                    break
                }
            }
            return if (!found) null else share
        }
        return null
    }

    private fun openOtherApp(description: String, amount: String) {

        val targetedShareIntents: ArrayList<Intent?> = ArrayList()
        val fireflyIntent: Intent? = getShareIntent(requireActivity(), "xyz.hisname.fireflyiii")

        if (fireflyIntent != null) targetedShareIntents.add(fireflyIntent)

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra("description", description)
            putExtra("amount", amount)
            putExtra(Intent.EXTRA_CHOOSER_TARGETS, arrayOf<Intent>());
            putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray());
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}
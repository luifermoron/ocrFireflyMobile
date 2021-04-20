package com.opensource.autofill.ui.home

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.demo.kotlin.textdetector.TextRecognitionProcessor
import com.opensource.autofill.R
import com.opensource.autofill.ui.mlkit.VisionImageProcessor
import java.io.IOException

import com.opensource.autofill.ui.mlkit.BitmapUtils

class HomeFragment : Fragment(), View.OnClickListener {

    private var imageUri: Uri? = null
    private lateinit var homeViewModel: HomeViewModel
    private var imageProcessor: VisionImageProcessor? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        root.findViewById<Button>(R.id.select_button).setOnClickListener(this)
        return root
    }

    private fun createImageProcessor() {
        try {
            imageProcessor = TextRecognitionProcessor(requireActivity())
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

        startActivityForResult(Intent.createChooser(intent, "Select an Image"), REQUEST_CHOOSE_IMAGE)
    }
    private fun tryReloadAndDetectInImage() {
        Log.d(TAG, "Try reload and detect image")
        try {
            if (imageUri == null) {
                Log.d(TAG, "imageUri == null")
                return
            }

            val imageBitmap = BitmapUtils.getBitmapFromContentUri(requireActivity().contentResolver, imageUri)
            if (imageBitmap == null) {
                Log.d(TAG, "imageBitmap == null")
                return
            }

            if (imageProcessor != null) {
                Log.d(TAG, "imageProcessor != null")
                imageProcessor!!.processBitmap(imageBitmap)
            } else {
                Log.e(TAG, "Null imageProcessor, please check adb logs for imageProcessor creation error")
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
}
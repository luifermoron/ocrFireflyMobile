/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.vision.demo.kotlin.textdetector

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.opensource.autofill.ocr.OCRResult
import com.opensource.autofill.ui.mlkit.VisionProcessorBase
import java.lang.Exception

/** Processor for the text detector demo.  */
class TextRecognitionProcessor(context: Context, _ocrResult: OCRResult) : VisionProcessorBase<Text>(
  context
) {

  val ocrResult: OCRResult = _ocrResult
  private val textRecognizer: TextRecognizer = TextRecognition.getClient()

  override fun stop() {
    super.stop()
    textRecognizer.close()
  }

  override fun detectInImage(image: InputImage): Task<Text> {
    return textRecognizer.process(image)
  }

  override fun onSuccess(text: Text) {
    logExtrasForTesting(text)
  }
  private fun logExtrasForTesting(text: Text?) {
    if (text != null) {
      val SPACE = "   "
      val builder = StringBuilder()
      for (i in text.textBlocks.indices) {
        val lines = text.textBlocks[i].lines
        for (j in lines.indices) {
          val elements = lines[j].elements
          for (k in elements.indices) {
            val element = elements[k]
            builder.append(SPACE.plus(element.text))
          }
        }
      }

      val longText: String = builder.toString()
      //val cleanWhiteSpaceText = longText.trim().replace("\\s+".toRegex(), " ")

      Log.v(MANUAL_TESTING_LOG, "Final result is:")
      Log.v(MANUAL_TESTING_LOG, longText)

      ocrResult.showOCRResult(longText)
    }
  }

  override fun onFailure(e: Exception) {
    TODO("Not yet implemented")
  }

}

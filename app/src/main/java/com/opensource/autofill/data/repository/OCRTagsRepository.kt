package com.opensource.autofill.data.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.opensource.autofill.data.dao.OCRTagDao
import com.opensource.autofill.model.ocr.OCRTag

@Suppress("RedundantSuspendModifier")
@WorkerThread
class OCRTagsRepository(private val ocrTagDao: OCRTagDao) {
    fun allOCRTags(): LiveData<MutableList<OCRTag>> {
        return ocrTagDao.getAllOCRTags()
    }

    suspend fun insertAmountTag(valueTag: String, max_word_quantity: Int = AMOUNT_MAX_WORD_QUANTITY) {
        ocrTagDao.insert(OCRTag(OCRTag.AMOUNT_FIELD, valueTag, max_word_quantity))
    }

    suspend fun insertDescriptionTag(valueTag: String, max_word_quantity: Int = DESCRIPTION_MAX_WORD_QUANTITY) {
        ocrTagDao.insert(OCRTag(OCRTag.DESCRIPTION_FIELD, valueTag, max_word_quantity))
    }

    suspend fun deleteTagByValue(valueTag: String) {
        ocrTagDao.deleteTagByValue(valueTag)
    }

    fun filterDescriptionTags(tags: MutableList<OCRTag>) : MutableList<OCRTag> {
        return tags.filter { it.field_name.equals(OCRTag.DESCRIPTION_FIELD) } as MutableList<OCRTag>
    }

    fun filterAmountTags(tags: MutableList<OCRTag>) : MutableList<OCRTag> {
        return tags.filter { it.field_name.equals(OCRTag.AMOUNT_FIELD) } as MutableList<OCRTag>
    }

    companion object {
        @JvmStatic val AMOUNT_MAX_WORD_QUANTITY = 1
        @JvmStatic val DESCRIPTION_MAX_WORD_QUANTITY = 5
    }
}
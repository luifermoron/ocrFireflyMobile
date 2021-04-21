package com.opensource.autofill.ui.configuration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.opensource.autofill.data.AutofillDatabase
import com.opensource.autofill.data.repository.OCRTagsRepository
import com.opensource.autofill.model.ocr.OCRTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfigurationViewModel(application: Application) : AndroidViewModel(application){
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val ocrTagsRepository = OCRTagsRepository(
        AutofillDatabase.buildDatabase(application).ocrTagDataDao()
    )

    fun getAllOCRTags(): LiveData<MutableList<OCRTag>> {
        return ocrTagsRepository.allOCRTags()
    }

    fun isLoading() {
        isLoading.postValue(true)
    }

    fun loaded() {
        isLoading.postValue(false)
    }

    fun insertAmountTag(value: String) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            ocrTagsRepository.insertAmountTag(value)
            isLoading.postValue(false)
        }
    }

    fun insertDescriptionTag(value: String) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            ocrTagsRepository.insertDescriptionTag(value)
            isLoading.postValue(false)
        }
    }

    suspend fun deleteTagByValue(tagValue: String) {
        return ocrTagsRepository.deleteTagByValue(tagValue)
    }

    fun filterDescriptionTags(tags: MutableList<OCRTag>) : MutableList<OCRTag> {
        return ocrTagsRepository.filterDescriptionTags(tags)
    }

    fun filterAmountTags(tags: MutableList<OCRTag>) : MutableList<OCRTag> {
        return ocrTagsRepository.filterAmountTags(tags)
    }

    fun descriptionSringList(tags: MutableList<OCRTag>) : List<String> {
        return filterDescriptionTags(tags).map { it.value_tag }
    }

    fun amountStringList(tags: MutableList<OCRTag>) : List<String> {
        return filterAmountTags(tags).map { it.value_tag }
    }
}
package com.opensource.autofill.ui.configuration
/*
  ~ Copyright (c)  2018 - 2021 Daniel Quah
  ~ Copyright (c)  2021 ASDF Dev Pte. Ltd.
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

    fun insertAmountTag(value: String, wordQuantities: Int) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            ocrTagsRepository.insertAmountTag(value, wordQuantities)
            isLoading.postValue(false)
        }
    }

    fun insertDescriptionTag(value: String, wordQuantities: Int) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            ocrTagsRepository.insertDescriptionTag(value, wordQuantities)
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

    fun descriptionSringList(tags: MutableList<OCRTag>) : List<OCRTag> {
        return filterDescriptionTags(tags)
    }

    fun amountStringList(tags: MutableList<OCRTag>) : List<OCRTag> {
        return filterAmountTags(tags)
    }
}
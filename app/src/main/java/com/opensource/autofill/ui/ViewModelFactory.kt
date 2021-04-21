package com.opensource.autofill.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.opensource.autofill.ui.configuration.ConfigurationViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ConfigurationViewModel::class.java) ->
                    ConfigurationViewModel(application)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
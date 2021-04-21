package com.opensource.autofill.ui.configuration

import androidx.fragment.app.Fragment
import com.opensource.autofill.ui.ViewModelFactory

fun Fragment.getViewModelFactory(): ViewModelFactory {
    return ViewModelFactory(requireActivity().application)
}

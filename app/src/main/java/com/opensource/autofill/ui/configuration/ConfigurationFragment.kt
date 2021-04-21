package com.opensource.autofill.ui.configuration

import androidx.fragment.app.activityViewModels


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.opensource.autofill.R
import com.opensource.autofill.databinding.FragmentConfigurationBinding
import com.opensource.autofill.model.ocr.OCRTag
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConfigurationFragment : Fragment() {

    private val tagViewModel by activityViewModels<ConfigurationViewModel> { getViewModelFactory() }
    private var fragmentListTagsBinding: FragmentConfigurationBinding? = null
    private val binding get() = fragmentListTagsBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentListTagsBinding = FragmentConfigurationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.allDescriptionTags.setChipSpacing(16)
        binding.allAmountTags.setChipSpacing(16)
        setResponse()
        displayView()
    }

    private fun setResponse(){

        binding.descriptionEdittext.setOnEditorActionListener(TextView.OnEditorActionListener
        {  v: TextView, actionId: Int,  event: KeyEvent? ->
            var handled : Boolean = false
            if (wasEnterPressed(actionId, event)) {
                val description: String = popText(binding.descriptionEdittext)
                tagViewModel.insertDescriptionTag(description)

                handled = true
            }
            handled
        })

        binding.amountEdittext.setOnEditorActionListener(TextView.OnEditorActionListener
        {  v: TextView, actionId: Int,  event: KeyEvent? ->
            var handled : Boolean = false
            if (wasEnterPressed(actionId, event)) {
                val amount: String = popText(binding.amountEdittext)
                tagViewModel.insertAmountTag(amount)

                handled = true
            }
            handled
        })
    }

    private fun popText(textView: EditText) : String {
        val textContent: String = textView.text.toString()
        textView.setText("")
        return textContent
    }

    private fun wasEnterPressed(actionId: Int,  event: KeyEvent?) : Boolean {
        Log.d("OCRFRAGMENT", actionId.toString());

        return (actionId == EditorInfo.IME_ACTION_DONE
                || event?.action == KeyEvent.ACTION_DOWN
                || event?.action == KeyEvent.KEYCODE_ENTER)
    }

    private fun displayView() {
        tagViewModel.getAllOCRTags().observe(viewLifecycleOwner) { tags ->
            tagViewModel.loaded()

            binding.allDescriptionTags.removeAllViewsInLayout()
            binding.allAmountTags.removeAllViewsInLayout()
            if(tags.isEmpty()){

            } else {
                replaceListOn(tagViewModel.filterDescriptionTags(tags), binding.allDescriptionTags)
                replaceListOn(tagViewModel.filterAmountTags(tags), binding.allAmountTags)
            }
        }

    }

    private fun replaceListOn(tags: MutableList<OCRTag>, chipGroup: ChipGroup){
        tags.forEach { tagsData ->
            val chipTags = Chip(requireContext(), null, R.attr.chipStyle)
            chipTags.apply {
                text = tagsData.value_tag
                isCloseIconVisible = true
                setOnCloseIconClickListener { close ->
                    val tagName = (close as TextView).text.toString()
                    deleteTag(tagName, chipTags)
                }
            }
            chipGroup.addView(chipTags)
        }
    }

    private fun deleteTag(tagName: String, chip: Chip){
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.delete_tag_title, tagName))
            .setMessage(resources.getString(R.string.delete_tag_message, tagName))
            .setPositiveButton(R.string.delete_permanently){ _, _ ->
                GlobalScope.launch {
                    tagViewModel.deleteTagByValue(tagName)
                }
            }
            .setNegativeButton("No"){ _, _ ->
                //toastInfo("Tag not deleted")
            }
            .show()
    }

    override fun onAttach(context: Context){
        super.onAttach(context)
        //requireActivity().findViewById<Toolbar>(R.id.activity_toolbar).title = resources.getString(R.string.tags)
    }
}
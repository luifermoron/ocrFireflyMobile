package com.opensource.autofill.ocr;

import android.util.Log;

import com.opensource.autofill.model.ocr.OCRTag;
import com.opensource.autofill.ocr.texthandler.BaseGetText;
import com.opensource.autofill.ocr.texthandler.GetExactText;
import com.opensource.autofill.ocr.texthandler.GetSimilarText;

import java.util.List;

import static com.opensource.autofill.ocr.texthandler.BaseGetText.DEFAULT_VALUE;


public class TagParser {
    private static final String TAG = "TagParser";

    public static BaseGetText buildGetExactText(String rawText) {
        return new GetExactText(rawText);
    }

    public static BaseGetText buildGetSimilarText(String rawText, String regexValue) {
        return new GetSimilarText(rawText).setValueRegex(regexValue);
    }

    public String findTextOn(List<OCRTag> tags, BaseGetText baseGetText) {

        for(OCRTag tag: tags) {
            String text = baseGetText.getText(tag);
            Log.d(TAG, "Text:" + text);
            Log.d(TAG, "Tag:" + tag);
            if(!text.equals(DEFAULT_VALUE))
                return text;
        }

        return DEFAULT_VALUE;
    }
}

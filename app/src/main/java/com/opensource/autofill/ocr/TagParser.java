package com.opensource.autofill.ocr;
/*
  ~ Copyright (c)  2021 Luis Morón
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

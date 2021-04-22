package com.opensource.autofill.ocr.texthandler;

import android.util.Log;

import com.opensource.autofill.model.ocr.OCRTag;

public class GetExactText extends BaseGetText {

    private static final String TAG = "GetExactText";
    private final String NEW_LINE = System.getProperty("line.separator");

    public GetExactText(String rawText) {
        super(rawText);
    }

    private boolean containsNewLine(String text) {
        return text.contains(NEW_LINE);
    }

    // Candidate to be deleted
    private String deleteLeadingNewLine(String text) {
        int index = 0;
        while (index < text.length() && (containsNewLine(text.substring(index, index + 1)) || Character.isWhitespace(text.substring(index, index + 1).toCharArray()[0])) ) {
            index++;
        }
        if (index >= text.length() || index == 0) return text;

        return text.substring(index);
    }

    private int calculateEnd(String formatedText, int maxWordQuantity) {
        int wordQuantity = 0;
        int end = 0;

        while (end < formatedText.length() &&  wordQuantity < maxWordQuantity) {
            int firstSpaceIndex = formatedText.substring(end).indexOf(" ") + end;

            if ((end + 1) == firstSpaceIndex)
                wordQuantity = formatedText.substring(0, end).trim().split(" ").length;
            end++;
        }

        return end;
    }

    @Override
    public String getText(OCRTag tag) {

        if (rawText.isEmpty()) return DEFAULT_VALUE;

        String valueTag  = tag.getValue_tag();
        int maxWordQuantity = tag.getMax_word_quantity();
        int begining = rawText.indexOf(valueTag);

        if (begining == -1 || (begining + valueTag.length()) >= rawText.length()) return DEFAULT_VALUE;
        begining += valueTag.length();

        String formatedText = rawText.substring(begining).trim();
        Log.d(TAG, "FormatedText:" + formatedText);

        int end = calculateEnd(formatedText, maxWordQuantity);

        Log.d(TAG, "End:" + end);
        Log.d(TAG, "Result:" + formatedText.substring(0, end).trim());

        return formatedText.substring(0, end).trim();
    }
}

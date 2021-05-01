package com.opensource.autofill.ocr.texthandler;

import com.opensource.autofill.model.ocr.OCRTag;

public abstract class BaseGetText {
    public static final String DEFAULT_VALUE = "";

    protected String rawText;

    private void cleanRawText() {
        this.rawText = rawText.trim().replace("\\s+", " ");
    }

    public BaseGetText(String rawText) {
        this.rawText = rawText;
        cleanRawText();
    }

    public String getRawText() {
        return rawText;
    }

    public abstract String getText(OCRTag tag);
}

package com.opensource.autofill.ocr.texthandler;

import com.opensource.autofill.model.ocr.OCRTag;

public abstract class BaseGetText {
    public static final String DEFAULT_VALUE = "";

    protected String rawText;

    public BaseGetText(String rawText) {
        this.rawText = rawText;
    }

    public abstract String getText(OCRTag tag);
}

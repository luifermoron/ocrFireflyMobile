package com.opensource.autofill.ocr.texthandler;
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

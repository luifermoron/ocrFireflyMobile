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

import java.util.Arrays;
import java.util.List;

import info.debatty.java.stringsimilarity.JaroWinkler;

public class GetSimilarText extends BaseGetText {

    private static final String TAG = "GetSimilarText";
    private static final double ACCEPTANCE_VALUE = 0.94;

    private String valueRegex;

    public GetSimilarText buildGetSimilarText(String rawText) {
        return new GetSimilarText(rawText);
    }

    public GetSimilarText(String rawText) {
        super(rawText);
    }

    public GetSimilarText setValueRegex(String valueRegex) {
        this.valueRegex = valueRegex;
        return this;
    }

    private String findRegex(List<String> subRawTextList, String valueRegex) {

        for (int i = 0; i < subRawTextList.size(); i++) {
            String singleText = subRawTextList.get(i).trim();
            if (singleText.matches(valueRegex))
                return singleText;
        }

        return DEFAULT_VALUE;
    }

    private String getAcceptanceValue(List<String> rawTextList, int startPosition, String valueRegex) {
        if (valueRegex != null)
            return findRegex(rawTextList.subList(startPosition, rawTextList.size()), valueRegex);
        else if( startPosition + 1 < rawTextList.size())
            return rawTextList.get(startPosition + 1);
        return DEFAULT_VALUE;
    }

    @Override
    public String getText(OCRTag tag) {
        String valueTag  = tag.getValue_tag();

        List<String> rawTextList = Arrays.asList(rawText.split("\n\n"));
        if (rawTextList.isEmpty()) return DEFAULT_VALUE;

        JaroWinkler jw = new JaroWinkler();
        for (int i= 0; i < rawTextList.size(); i++) {
            String singleText = rawTextList.get(i);
            double similarity = jw.similarity(valueTag, singleText);

            if (similarity > ACCEPTANCE_VALUE) {
                String value = getAcceptanceValue(rawTextList, i, valueRegex);
                if (!value.equals(DEFAULT_VALUE))
                    return value;
            }
        }

        return DEFAULT_VALUE;
    }
}

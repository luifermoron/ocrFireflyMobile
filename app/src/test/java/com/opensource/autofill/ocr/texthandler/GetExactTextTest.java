package com.opensource.autofill.ocr.texthandler;

import com.opensource.autofill.ocr.TagParser;

import org.junit.Test;

import static org.junit.Assert.*;

public class GetExactTextTest {
    String multipleSpaces = "    Comprobante   Electrónico   Transferencia   interbancaria   Número   de   comprobante:   46546456456   echa   de   la   transacción:   29/04/2021   Hora   de   la   transacción:   16:28:28   Se   debito   de   su   caja   de   ahorro:   3564564   La   suma   de   bs:   100   Nombre   del   originante:   XD   LUIS     Nombre   del   abonado:   Francisca Pelaez   Banco   destino:   BANCO   xd   Se   acreditó   a   la   cuenta:   f232     Comprobante:   342342432423   Glosa:   Test test";

    @Test
    public void deleteMultipleSpaces_ReturnsFalse() {
        BaseGetText baseGetText = TagParser.buildGetExactText(multipleSpaces);
        String rawText = baseGetText.getRawText();
        assertFalse(rawText.matches("\\s+"));
    }

}
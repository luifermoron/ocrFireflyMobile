package com.opensource.autofill.ui.mlkit;

import android.graphics.Bitmap;

public interface VisionImageProcessor {

    void processBitmap(Bitmap bitmap);

    void stop();
}

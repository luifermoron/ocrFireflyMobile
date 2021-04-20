package com.opensource.autofill.ui.mlkit;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.camera.core.ImageProxy;

import com.google.mlkit.common.MlKitException;

import java.nio.ByteBuffer;


public interface VisionImageProcessor {

    void processBitmap(Bitmap bitmap);

    void stop();
}

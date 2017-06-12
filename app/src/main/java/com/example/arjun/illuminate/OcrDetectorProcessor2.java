/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.arjun.illuminate;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;

import com.example.arjun.illuminate.camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import static com.example.arjun.illuminate.HomeActivity.tts;


/**
 * A very simple Processor which gets detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class OcrDetectorProcessor2 implements Detector.Processor<TextBlock> {

    private static final long DETECTION_DELAY = 2000;
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    long LastDetectionTime = 0;


    OcrDetectorProcessor2(GraphicOverlay<OcrGraphic> ocrGraphicOverlay) {
        mGraphicOverlay = ocrGraphicOverlay;
    }

    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        if(System.currentTimeMillis() - LastDetectionTime > DETECTION_DELAY) {

            mGraphicOverlay.clear();
            SparseArray<TextBlock> items = detections.getDetectedItems();
            for (int i = 0; i < items.size(); ++i) {
                TextBlock item = items.valueAt(i);
                if (item != null && item.getValue() != null) {
                    Log.d("OcrDetectorProcessor", "Text detected! " + item.getValue());
//                    tts.speak(item.getValue(), TextToSpeech.QUEUE_ADD, null);
                }
                OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
//            mGraphicOverlay.add(graphic);
            }
            LastDetectionTime = System.currentTimeMillis();
        }
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {
        mGraphicOverlay.clear();
    }
}

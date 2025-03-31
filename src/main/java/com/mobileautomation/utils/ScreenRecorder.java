package com.mobileautomation.utils;

import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class ScreenRecorder {
    private ScreenRecorder screenRecorder;
    private final String outputDir;
    private final String testName;

    public ScreenRecorder(String outputDir, String testName) {
        this.outputDir = outputDir;
        this.testName = testName;
    }

    public void startRecording() throws IOException, AWTException {
        File outputFile = new File(outputDir, testName + "_" + System.currentTimeMillis() + ".mov");
        
        Rectangle captureSize = new Rectangle(0, 0, 1920, 1080);
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        screenRecorder = new ScreenRecorder(gc, captureSize,
                new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, MIME_QUICKTIME),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_QUICKTIME_JPEG,
                        CompressorNameKey, ENCODING_QUICKTIME_JPEG, DepthKey, 24, FrameRateKey,
                        Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                null, outputFile);

        screenRecorder.start();
    }

    public void stopRecording() throws IOException {
        if (screenRecorder != null) {
            screenRecorder.stop();
            screenRecorder.save();
        }
    }
} 
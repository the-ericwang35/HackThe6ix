package com.example.android.hackthe6ixcopy;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jerry on 2017-08-26.
 */

public class MedianCalculator {
    private static final String TAG = "OpenCVMedianCalculator";
    public static List<Mat> stabilize(List<Mat> images) {
        return new ArrayList<>();
    }

    public static void process(List<File> imageFiles) {
        List<Mat> images = new ArrayList<>();
        Mat tmp;
        for (File file : imageFiles) {
            try {
                tmp = Imgcodecs.imread(file.getPath());
                images.add(tmp);
            }
            catch(UnsatisfiedLinkError e) {
                e.printStackTrace();
            }
        }
        Mat med = median(images);
        String outputName = "result.jpg";
        Imgcodecs.imwrite(outputName, med);
    }

    public static Mat median(List<Mat> images) {
        Mat tmp = null;
        // We will sorting pixels where the first mat will get the lowest pixels and the last one, the highest
        for(int i = 0; i < images.size(); i++) {
            for(int j = i + 1; j < images.size(); j++) {
                images.get(i).copyTo(tmp);
                Core.min(images.get(i), images.get(j), images.get(i));
                Core.max(images.get(j), tmp, images.get(j));
            }
        }
        // We get the median
        Mat result = images.get(images.size() / 2);
        return result;
    }

    public static void main(String[] args) {
        List<File> files = Arrays.asList(new File(System.getProperty("user.dir") + "/input").listFiles());
        process(files);
    }
}

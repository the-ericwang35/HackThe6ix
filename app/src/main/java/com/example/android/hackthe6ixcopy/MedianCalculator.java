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

    public static void process(List<File> imageFiles, File output) {
        List<Mat> images = new ArrayList<>();
        Mat tmp;
        for (File file : imageFiles) {
            try {
                tmp = Imgcodecs.imread(file.getPath());
                images.add(tmp);
                Log.d(TAG, file.getPath());
            }
            catch(UnsatisfiedLinkError e) {
                e.printStackTrace();
            }
        }
        Mat med = median(images);
        Log.d(TAG, Boolean.toString(Imgcodecs.imwrite(output.toString(), med)));
    }

    public static Mat median(List<Mat> images) {
        Mat[] imgArray = new Mat[images.size()];
        imgArray = images.toArray(imgArray);
        Mat tmp = imgArray[0];
        // We will sorting pixels where the first mat will get the lowest pixels and the last one, the highest
        for(int i = 0; i < images.size(); i++) {
            for(int j = i + 1; j < images.size(); j++) {
                imgArray[i].copyTo(tmp);
                Core.min(imgArray[i], imgArray[j], imgArray[i]);
                Core.max(imgArray[j], tmp, imgArray[j]);
            }
        }
        // We get the median
        Mat result = images.get(images.size() / 2);
        return result;
    }

    public static void main(String[] args) {
        List<File> files = Arrays.asList(new File(System.getProperty("user.dir") + "/input").listFiles());
        //process(files);
    }
}

package com.example.android.hackthe6ixcopy;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jerry on 2017-08-26.
 */

public class ThingRemover {
    private static final String TAG = "OpenCVMedianCalculator";
    public static List<Mat> stabilize(List<Mat> images) {
        // orig = cv2.imread(file_paths[1])
        // orig = cv2.resize(orig, None, fx=0.14, fy=0.14)
        List<Mat> results = new ArrayList<>();

        Mat orig = images.get(0);
        Mat orig_resized = new Mat();
        Imgproc.resize(orig, orig_resized, orig_resized.size(), 0.15, 0.15, Imgproc.INTER_CUBIC);
        Mat orig_gray = new Mat();
        Imgproc.cvtColor(orig_resized, orig_gray, Imgproc.COLOR_BGR2GRAY);
        results.add(orig_resized);

        int warp_mode = Video.MOTION_EUCLIDEAN;
        Mat tmp = new Mat();
        Mat tmp_gray = new Mat();
        Mat tmp_aligned = new Mat();
        for (Mat image : images) {
            Imgproc.resize(image, tmp, tmp.size(), 0.15, 0.15, Imgproc.INTER_CUBIC);
            Imgproc.cvtColor(tmp, tmp_gray, Imgproc.COLOR_BGR2GRAY);

            Mat warp_matrix;
            if ( warp_mode == Video.MOTION_HOMOGRAPHY )
                warp_matrix = Mat.eye(3, 3, CvType.CV_32F);
            else
                warp_matrix = Mat.eye(2, 3, CvType.CV_32F);

            int number_of_iterations = 300;

            double termination_eps = 1e-10;

            TermCriteria criteria = new TermCriteria(TermCriteria.COUNT+TermCriteria.EPS, number_of_iterations, termination_eps);
            Video.findTransformECC(orig_gray, tmp_gray, warp_matrix, warp_mode, criteria, new Mat());
            if (warp_mode != Video.MOTION_HOMOGRAPHY)
                Imgproc.warpAffine(tmp, tmp_aligned, warp_matrix, orig_resized.size(), Imgproc.INTER_LINEAR + Imgproc.WARP_INVERSE_MAP);
            else
                // Use warpPerspective for Homography
                Imgproc.warpPerspective(tmp, tmp_aligned, warp_matrix, orig_resized.size(), Imgproc.INTER_LINEAR + Imgproc.WARP_INVERSE_MAP);
            results.add(tmp_aligned);
        }

        return results;
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

        Mat med = median(stabilize(images));
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

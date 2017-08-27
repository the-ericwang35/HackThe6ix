package com.example.android.hackthe6ixcopy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;
import java.util.Arrays;

public class ImageProcessingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processing);
    }

    @Override
    protected void onResume(){
        super.onResume();
        new ProcessImagesTask().execute(0); //0 is a placeholder
    }

    class ProcessImagesTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer...params){
            File folder = new File(Environment.getExternalStorageDirectory() + "/pics/");
            File output = new File(Environment.getExternalStorageDirectory() + "/pics/median.jpg");
            ThingRemover.process(Arrays.asList(folder.listFiles()), output);
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            File imgFile = new File(Environment.getExternalStorageDirectory() + "/pics/median.jpg");
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView medianImage = (ImageView) findViewById(R.id.median_image);
                medianImage.setImageBitmap(bitmap);
            }



        }
    }
}

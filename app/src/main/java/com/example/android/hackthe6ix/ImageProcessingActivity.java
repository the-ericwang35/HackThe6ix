package com.example.android.hackthe6ix;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
        new ProcessImagesTask().execute();
    }

    void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    class ProcessImagesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void...params){
            File folder = new File(Environment.getExternalStorageDirectory() + "/pics/");
            File output = new File(Environment.getExternalStorageDirectory() + "/pics/median.jpg");
            ThingRemover.process(Arrays.asList(folder.listFiles()), output);
            String result = Environment.getExternalStorageDirectory() + "/pics/median.jpg";
            addImageToGallery(result, ImageProcessingActivity.this);
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
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_indicator);
                progressBar.setVisibility(View.INVISIBLE);
                medianImage.setVisibility(View.VISIBLE);
                medianImage.setImageBitmap(bitmap);
            }
        }


    }
}

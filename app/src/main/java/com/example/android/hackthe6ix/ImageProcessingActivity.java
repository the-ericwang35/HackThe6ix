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
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import java.io.File;
import java.util.Arrays;

public class ImageProcessingActivity extends AppCompatActivity {

    private ImageView mMedianImage;
    private ProgressBar mProgressBar;
    private Button mSaveButton;
    private AppCompatButton mDeleteButton;

    private static final String OUTPUT_PATH = Environment.getExternalStorageDirectory()
            + "/pics/median.jpg";
    private static final String INPUT_PATH = Environment.getExternalStorageDirectory()
            + "/pics/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processing);
        mMedianImage = (ImageView) findViewById(R.id.median_image);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        mSaveButton = (Button) findViewById(R.id.save_action);
        mDeleteButton = (AppCompatButton) findViewById(R.id.delete_action);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImageToGallery(OUTPUT_PATH, getParent());
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
            File folder = new File(INPUT_PATH);
            File output = new File(OUTPUT_PATH);
            ThingRemover.process(Arrays.asList(folder.listFiles()), output);
            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            File imgFile = new File(OUTPUT_PATH);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mProgressBar.setVisibility(View.INVISIBLE);
                mMedianImage.setVisibility(View.VISIBLE);
                mSaveButton.setVisibility(View.VISIBLE);
                mDeleteButton.setVisibility(View.VISIBLE);
                mMedianImage.setImageBitmap(bitmap);
            }
        }


    }
}

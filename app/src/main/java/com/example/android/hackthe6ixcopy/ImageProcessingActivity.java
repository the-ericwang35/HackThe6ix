package com.example.android.hackthe6ixcopy;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.File;
import java.util.Arrays;

public class ImageProcessingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        File folder = new File(Environment.getExternalStorageDirectory() + "/pics/");
        File output = new File(Environment.getExternalStorageDirectory() + "/pics/median.jpg");
        ThingRemover.process(Arrays.asList(folder.listFiles()), output);
    }

}

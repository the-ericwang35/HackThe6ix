package com.example.android.hackthe6ixcopy;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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
        
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("done processing");
    }

}

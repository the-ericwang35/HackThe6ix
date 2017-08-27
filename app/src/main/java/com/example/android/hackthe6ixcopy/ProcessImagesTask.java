package com.example.android.hackthe6ixcopy;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.util.Arrays;

/**
 * Created by jerry on 2017-08-27.
 */

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
    }
}

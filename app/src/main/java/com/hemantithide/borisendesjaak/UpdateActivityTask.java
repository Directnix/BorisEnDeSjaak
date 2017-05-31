package com.hemantithide.borisendesjaak;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Daniel on 31/05/2017.
 */

public class UpdateActivityTask extends AsyncTask<Object, Void, Void> {

    private final TextView frameCounter;
    private final int frameCount;

    public UpdateActivityTask(TextView frameCounter, int frameCount) {
        this.frameCounter = frameCounter;
        this.frameCount = frameCount;
    }

    @Override
    protected Void doInBackground(Object... params) {
        return null;
    }

    protected void onProgressUpdate() {
        frameCounter.setText(frameCount);

        Log.e("Updated thru onProgress", frameCount + "");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
        frameCounter.setText(frameCount);

        Log.e("Updated thru onPost", frameCount + "");
    }

    protected void onPostExecute(Long result) {
    }
}

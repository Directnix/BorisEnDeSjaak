package com.hemantithide.borisendesjaak;

import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by Daniel on 30/05/2017.
 */

public class GameThread extends Thread {

    private GameSurfaceView surfaceView;

    public GameThread(GameSurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }

    @Override
    public void run() {

        // update canvas
        Canvas canvas = surfaceView.getHolder().lockCanvas();
        if(canvas != null) {
            synchronized (surfaceView.getHolder()) {
                surfaceView.updateCanvas(canvas);
            }
            surfaceView.getHolder().unlockCanvasAndPost(canvas);
        }

        // sleep
        try { sleep(1000/60); } catch (InterruptedException e) {
            Log.e("InterruptedException", e.getLocalizedMessage());
        }
    }
}

package com.hemantithide.borisendesjaak;

import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by Daniel on 30/05/2017.
 */

public class GameThread extends Thread {

    private GameSurfaceView surfaceView;
    private boolean isRunning;

    // fps
    private long lastLoopTime;

    public GameThread(GameSurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        this.isRunning = false;

        lastLoopTime = System.currentTimeMillis();
    }

    public void running(boolean running) {
        this.isRunning = running;
    }

    @Override
    public void run() {

        while(true) {
            // update canvas
            Canvas canvas = surfaceView.getHolder().lockCanvas();

            if (canvas != null) {
                synchronized (surfaceView.getHolder()) {
                    surfaceView.updateCanvas(canvas);
                }
                surfaceView.getHolder().unlockCanvasAndPost(canvas);

            }

            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();;

            // sleep
            try {
                sleep(1000 / 60);

//                Log.e("FPS", delta + "");


            } catch (InterruptedException e) {
                Log.e("InterruptedException", e.getLocalizedMessage());
            }
        }
    }
}

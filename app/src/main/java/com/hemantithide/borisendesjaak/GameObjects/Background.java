package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.hemantithide.borisendesjaak.GameSurfaceView;
import com.hemantithide.borisendesjaak.R;

/**
 * Created by Daniel on 31/05/2017.
 */

public class Background {

    private GameSurfaceView game;

    private Bitmap sprite;

    private int lifespan;
    public int posY;

    public Background(GameSurfaceView game, int posY) {
        this.game = game;
        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.grassloop);

        this.posY = posY;
        lifespan = posY;

        Log.e("Created", "background");
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, 0, posY, null);
    }

    public void update() {
        lifespan += game.metrics.heightPixels / 90;

        posY = lifespan;

        // resets the background to scroll
        if(lifespan > game.metrics.heightPixels)
            lifespan = 12 - sprite.getHeight();
    }
}

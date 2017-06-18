package com.hemantithide.borisendesjaak.Visuals;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.SpriteLibrary;
import com.hemantithide.borisendesjaak.GameActivity;
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
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.BACKGROUND);
        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels, (int)(1.2 * game.metrics.heightPixels), true);

        this.posY = posY;
        lifespan = posY;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, 0, posY, null);
    }

    public void update() {
        lifespan += game.gameSpeed * GameActivity.relativeScreenSizeFactor;

        posY = lifespan;

        // resets the background to scroll
        if(lifespan > game.metrics.heightPixels)
            lifespan = (game.metrics.heightPixels / 10) - sprite.getHeight();
    }
}

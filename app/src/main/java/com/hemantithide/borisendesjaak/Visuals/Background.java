package com.hemantithide.borisendesjaak.Visuals;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
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
<<<<<<< HEAD:app/src/main/java/com/hemantithide/borisendesjaak/GameObjects/Background.java
        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.better_grass);
=======
        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.grassloop_plus);
        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels, (int)(1.2 * game.metrics.heightPixels), true);
>>>>>>> game:app/src/main/java/com/hemantithide/borisendesjaak/Visuals/Background.java

        this.posY = posY;
        lifespan = posY;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, 0, posY, null);
    }

    public void update() {
        lifespan += game.gameSpeed;

        posY = lifespan;

        // resets the background to scroll
        if(lifespan > game.metrics.heightPixels)
            lifespan = (game.metrics.heightPixels / 10) - sprite.getHeight();
    }
}

package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.GameSurfaceView;
import com.hemantithide.borisendesjaak.R;
import com.hemantithide.borisendesjaak.SpriteLibrary;

/**
 * Created by Daniel on 31/05/2017.
 */

public class Rock extends GameObject {

    int lifespan;

    public Rock(GameSurfaceView game, int ID) {
        super(game);
//        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.rock);
//        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 10, game.metrics.widthPixels / 10, true);
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.ROCK);

        lifespan = (int)(-0.1 * game.metrics.heightPixels);
        horizLaneID = game.primaryRocks.get(ID);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, posX + 16, posY, null);
    }

    public void update() {
        posX = game.laneXValues.get(horizLaneID);

        lifespan += game.gameSpeed;

        posY = lifespan;

        if(game.player.collisionTimer == 0 && Math.abs(posY - game.player.posY) < sprite.getHeight() && Math.abs(posX - game.player.posX) < sprite.getWidth()) {
            game.player.collision(this);
        }

        if(lifespan > game.metrics.heightPixels)
            destroy();
    }
}

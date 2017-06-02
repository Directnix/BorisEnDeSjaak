package com.hemantithide.borisendesjaak.GameObjects.Collectables;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.GameSurfaceView;
import com.hemantithide.borisendesjaak.R;
import com.hemantithide.borisendesjaak.SpriteLibrary;

/**
 * Created by Daniel on 01/06/2017.
 */

public class Kinker extends Collectable {

    public Kinker(GameSurfaceView game) {
        super(game, (int)(Math.random() * 5));
//        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.kinker);
//        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 8, game.metrics.widthPixels / 10, true);
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.KINKER);

        lifespan = -2 * sprite.getHeight();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, posX + (sprite.getWidth() / 4), posY, null);
    }

    @Override
    public void update() {

        posX = game.laneXValues.get(horizLaneID);

        lifespan += game.gameSpeed * 0.2;

        posY = lifespan;

        if(Math.abs(posY - game.player.posY) < sprite.getHeight() && Math.abs(posX - game.player.posX) < sprite.getWidth()) {
            game.activity.playSound(GameActivity.Sound.POWERUP);
            game.player.collect(this);
            destroy();
        }

        if(lifespan > game.metrics.heightPixels) {
            destroy();
            game.kinker = null;
        }
    }
}

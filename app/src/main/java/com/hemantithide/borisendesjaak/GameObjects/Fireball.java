package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.GameSurfaceView;
import com.hemantithide.borisendesjaak.R;

/**
 * Created by Daniel on 01/06/2017.
 */

public class Fireball extends GameObject {

    private int lifespan;

    public Fireball(GameSurfaceView game, int ID) {
        super(game);
        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.fireball);

        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 10, game.metrics.widthPixels / 5, true);

        lifespan = -2 * sprite.getHeight();
        horizLaneID = game.primaryRocks.get(ID);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, posX + 16, posY, null);
    }

    @Override
    public void update() {
        posX = game.laneXValues.get(horizLaneID);

        lifespan += game.gameSpeed;

        posY = game.metrics.heightPixels - lifespan;

        if(game.player.collisionTimer == 0 && Math.abs(posY - game.player.posY) < sprite.getHeight() && Math.abs(posX - game.player.posX) < sprite.getWidth()) {
            game.activity.playSound(GameActivity.Sound.ROCK_HIT);
            game.player.collisionTimer = 120;

            if(game.player.health > 0) {
                game.player.health--;
                game.player.healthBar.update(game.player.health);
            }
        }

        if(lifespan > (game.metrics.heightPixels + sprite.getHeight()))
            game.gameObjects.remove(this);
    }
}

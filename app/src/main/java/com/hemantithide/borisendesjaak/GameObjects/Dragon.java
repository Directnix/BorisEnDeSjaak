package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.hemantithide.borisendesjaak.GameSurfaceView;
import com.hemantithide.borisendesjaak.R;

/**
 * Created by Daniel on 01/06/2017.
 */

public class Dragon extends GameObject {

    int lifespan;

    int targetLane;
    int targetX;
    int targetY;

    private int fireballCooldown;

    public Dragon(GameSurfaceView game) {
        super(game);
        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.boris);

        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 2, game.metrics.widthPixels / 2, true);

        horizLaneID = 2;

        posX = game.laneXValues.get(horizLaneID);
        posY = game.metrics.heightPixels;

        targetLane = 2;

        targetY = posY - 200;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, posX - (sprite.getWidth() / 4), posY, null);
    }

    @Override
    public void update() {

        if(Math.abs(posX - targetX) < (16 * game.speedMultiplier))
            posX = targetX;
        if(posX < targetX)
            posX += 16 * game.speedMultiplier;
        else if(posX > targetX)
            posX -= 16 * game.speedMultiplier;

        if(Math.abs(posY - targetY) < (6 * game.speedMultiplier))
            posY = targetY;
        if(posY < targetY)
            posY += 6 * game.speedMultiplier;
        else if(posY > targetY)
            posY -= 4 * game.speedMultiplier;

        spawnFireball();

        if(fireballCooldown > 0) {
            fireballCooldown--;
        }
    }

    private void spawnFireball() {

        if(posX == targetX && fireballCooldown == 0) {

            Log.e("Dragon Lane", targetLane + "");

            new Fireball(game, targetLane);
            targetLane = (int)(Math.random() * 5);
            targetX = game.laneXValues.get(targetLane);
            fireballCooldown = 120;

        }
    }
}

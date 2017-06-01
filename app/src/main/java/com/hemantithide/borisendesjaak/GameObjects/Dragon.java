package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.GameSurfaceView;
import com.hemantithide.borisendesjaak.R;

import static com.hemantithide.borisendesjaak.GameObjects.Dragon.State.ABSENT;
import static com.hemantithide.borisendesjaak.GameObjects.Dragon.State.PRESENT;

/**
 * Created by Daniel on 01/06/2017.
 */

public class Dragon extends GameObject {


    public enum State {PRESENT, ABSENT}
    public State state = ABSENT;

    private boolean initFinished;

    int lifespan;

    int targetLane;
    int targetX;
    int targetY;

    private int fireballCooldown;

    public Dragon(GameSurfaceView game) {
        super(game);
        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.draak);

        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 2, game.metrics.widthPixels / 2, true);

        setState(ABSENT);

        targetLane = 2;
        targetX = game.laneXValues.get(targetLane);

        posX = game.laneXValues.get(targetLane);
        posY = game.metrics.heightPixels;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, posX - (sprite.getWidth() / 3), posY, null);
    }

    @Override
    public void update() {

        if (Math.abs(posX - targetX) < (16 * game.speedMultiplier))
            posX = targetX;
        if (posX < targetX)
            posX += 16 * game.speedMultiplier;
        else if (posX > targetX)
            posX -= 16 * game.speedMultiplier;

        if (Math.abs(posY - targetY) < (6 * game.speedMultiplier))
            posY = targetY;
        if (posY < targetY)
            posY += 6 * game.speedMultiplier;
        else if (posY > targetY)
            posY -= 4 * game.speedMultiplier;

        if(state == PRESENT && !initFinished && posY == targetY) {
            game.activity.playSound(GameActivity.Sound.AYO_WHADDUP);
            initFinished = true;
            fireballCooldown = 42;
        }

        if (state == PRESENT && posY == targetY) {
            if (posX == targetX && fireballCooldown == 0) {
                spawnFireball();
            }
        }

        if (fireballCooldown > 0) {
            fireballCooldown--;
        }
    }

    private void spawnFireball() {
        new Fireball(game, targetLane);
        targetLane = (int) (Math.random() * 5);
        targetX = game.laneXValues.get(targetLane);
//        fireballCooldown = (int) (60 / game.speedMultiplier);
        fireballCooldown = 42;
        game.activity.playSound(GameActivity.Sound.FIREBALL);
    }

    public void setState(State state) {

        switch (state) {
            case PRESENT:
                targetLane = 2;
                targetX = game.laneXValues.get(targetLane);
                targetY = game.metrics.heightPixels - (sprite.getHeight() / 2);
                initFinished = false;
                game.activity.playSound(GameActivity.Sound.WOOSH);
                break;
            case ABSENT:
                targetY = game.metrics.heightPixels + sprite.getHeight();
                break;
        }

        this.state = state;
    }
}

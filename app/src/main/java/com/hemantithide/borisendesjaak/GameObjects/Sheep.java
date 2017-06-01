package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.GameSurfaceView;
import com.hemantithide.borisendesjaak.R;
import com.hemantithide.borisendesjaak.Visuals.HealthBar;

/**
 * Created by Daniel on 30/05/2017.
 */

public class Sheep extends GameObject {


    private int playerID;

    private boolean alive = true;
    int health = 3;

    private int targetX;
    private int targetY;

    HealthBar healthBar;

    int collisionTimer;
    boolean blinkInvisible;

    public Sheep(GameSurfaceView game, int playerID) {
        super(game);
        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.sheep_placeholder);
        sprite = Bitmap.createScaledBitmap(sprite, (game.metrics.widthPixels / 300) * 27, (game.metrics.widthPixels / 300) * 48, false);

        this.playerID = playerID;

        healthBar = new HealthBar(this);

        horizLaneID = 2;
        vertiLaneID = 3;

        posX = game.laneXValues.get(horizLaneID);
        posY = game.laneYValues.get(vertiLaneID);

        targetX = posX;
        targetY = posY;
    }

    public void moveLeft() {

        horizLaneID--;

        if(horizLaneID < 0)
            horizLaneID = 0;
        else
            game.activity.playSound(GameActivity.Sound.SWIPE);

        targetX = game.laneXValues.get(horizLaneID);
    }

    public void moveRight() {

        horizLaneID++;

        if(horizLaneID > 4)
            horizLaneID = 4;
        else
            game.activity.playSound(GameActivity.Sound.SWIPE);

        targetX = game.laneXValues.get(horizLaneID);
    }

    public void moveDown() {

        vertiLaneID++;

        if(vertiLaneID > 5)
            vertiLaneID = 5;
        else
            game.activity.playSound(GameActivity.Sound.SWIPE);

        targetY = game.laneYValues.get(vertiLaneID);
    }

    public void moveUp() {

        vertiLaneID--;

        if(vertiLaneID < 1)
            vertiLaneID = 1;
        else
            game.activity.playSound(GameActivity.Sound.SWIPE);

        targetY = game.laneYValues.get(vertiLaneID);
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public void draw(Canvas canvas) {

        healthBar.draw(canvas);

        if(collisionTimer > 0 && game.frameCount % 30 == 0)
            blinkInvisible = !blinkInvisible;

        if(collisionTimer == 0 || !blinkInvisible) {
            canvas.drawBitmap(sprite, posX + 32, posY, null);
        }
    }

    @Override
    public void update() {

        if(Math.abs(posX - targetX) < (16 * game.speedMultiplier))
            posX = targetX;
        if(posX < targetX)
            posX += 16 * game.speedMultiplier;
        else if(posX > targetX)
            posX -= 16 * game.speedMultiplier;

        if(Math.abs(posY - targetY) < (12 * game.speedMultiplier))
            posY = targetY;
        if(posY < targetY)
            posY += 12 * game.speedMultiplier;
        else if(posY > targetY)
            posY -= 8 * game.speedMultiplier;

        if(collisionTimer > 0)
            collisionTimer--;

//        posX = game.getLaneXValues().get(horizLaneID);
    }
}

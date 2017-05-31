package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

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

    HealthBar healthBar;

    int collisionTimer;

    public Sheep(GameSurfaceView game, int playerID) {
        super(game);
        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.sheep_placeholder);
        sprite = Bitmap.createScaledBitmap(sprite, (game.metrics.widthPixels / 300) * 27, (game.metrics.widthPixels / 300) * 48, false);

        this.playerID = playerID;

        healthBar = new HealthBar(this);

        posY = (int)(game.metrics.heightPixels * 0.6);
    }

    public void moveLeft() {

        laneID--;

        if(laneID < 0)
            laneID = 0;

        targetX = game.getLanePositionValues().get(laneID);
    }

    public void moveRight() {

        laneID++;

        if(laneID > 4)
            laneID = 4;

        targetX = game.getLanePositionValues().get(laneID);
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, posX + 32, posY, null);

        healthBar.draw(canvas);

        if(collisionTimer > 0) {

        }
    }

    @Override
    public void update() {

        if(Math.abs(posX - targetX) < 20)
            posX = targetX;
        if(posX < targetX)
            posX += 20;
        else if(posX > targetX)
            posX -= 20;

        if(collisionTimer > 0)
            collisionTimer--;

//        posX = game.getLanePositionValues().get(laneID);
    }
}

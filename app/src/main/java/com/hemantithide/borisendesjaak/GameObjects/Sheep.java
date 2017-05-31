package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.hemantithide.borisendesjaak.GameSurfaceView;
import com.hemantithide.borisendesjaak.R;

/**
 * Created by Daniel on 30/05/2017.
 */

public class Sheep extends GameObject {


    private int playerID;
    private boolean alive = true;

    public Sheep(GameSurfaceView game, int playerID) {
        super(game);
        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.sheep_placeholder);

        this.playerID = playerID;

        posY = 540;
    }

    public void moveLeft() {

        laneID--;

        if(laneID < 0)
            laneID = 0;

        game.snapToLane(laneID);
    }

    public void moveRight() {

        laneID++;

        if(laneID > 4)
            laneID = 4;

        game.snapToLane(laneID);
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, posX + 50, posY, null);
    }

    @Override
    public void update() {
        posX = game.getLanePositionValues().get(laneID);
    }
}

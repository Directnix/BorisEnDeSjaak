package com.hemantithide.borisendesjaak;

import android.graphics.drawable.Drawable;

/**
 * Created by Daniel on 30/05/2017.
 */

public class Sheep {

    private GameActivity game;

    private int playerID;
    private int sprite = R.drawable.sheep_placeholder;

    private boolean alive = true;

    private int laneID;

    public Sheep(GameActivity game, int playerID) {
        this.game = game;
        this.playerID = playerID;
    }

    public int getSprite() {
        return sprite;
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
}

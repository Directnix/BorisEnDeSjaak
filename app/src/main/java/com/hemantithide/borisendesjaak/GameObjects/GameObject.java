package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;

/**
 * Created by Daniel on 31/05/2017.
 */

public abstract class GameObject {

    public GameSurfaceView game;

    protected Bitmap sprite;

    protected int horizLaneID;
    int vertiLaneID;

    public int posX;
    public int posY;

    public GameObject(GameSurfaceView game) {
        this.game = game;
        game.gameObjects.add(this);
    }

    public abstract void draw(Canvas canvas);

    public abstract void update();

    public void destroy() {
        game.gameObjects.remove(this);
    }
}

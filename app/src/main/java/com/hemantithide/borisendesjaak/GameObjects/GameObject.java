package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.GameSurfaceView;

/**
 * Created by Daniel on 31/05/2017.
 */

public abstract class GameObject {

    public GameSurfaceView game;

    Bitmap sprite;

    int laneID;
    public int posX;
    public int posY;

    public GameObject(GameSurfaceView game) {
        this.game = game;
        game.gameObjects.add(this);
    }

    public abstract void draw(Canvas canvas);

    public abstract void update();
}
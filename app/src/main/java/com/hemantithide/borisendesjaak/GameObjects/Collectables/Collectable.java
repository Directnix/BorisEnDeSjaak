package com.hemantithide.borisendesjaak.GameObjects.Collectables;

import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.GameObjects.GameObject;
import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;

/**
 * Created by Daniel on 01/06/2017.
 */

public abstract class Collectable extends GameObject {

    int lifespan;

    public Collectable(GameSurfaceView game, int ID) {
        super(game);
        horizLaneID = ID;

        posY = (int)(game.metrics.heightPixels * -0.1);
    }

    @Override
    public abstract void draw(Canvas canvas);

    @Override
    public abstract void update();
}

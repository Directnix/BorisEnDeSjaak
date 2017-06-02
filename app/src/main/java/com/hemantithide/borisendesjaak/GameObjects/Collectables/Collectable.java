package com.hemantithide.borisendesjaak.GameObjects.Collectables;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.GameObjects.GameObject;
import com.hemantithide.borisendesjaak.GameSurfaceView;
import com.hemantithide.borisendesjaak.R;

/**
 * Created by Daniel on 01/06/2017.
 */

public abstract class Collectable extends GameObject {

    int lifespan;

    public Collectable(GameSurfaceView game, int ID) {
        super(game);
        horizLaneID = game.primaryRocks.get(ID);
    }

    @Override
    public abstract void draw(Canvas canvas);

    @Override
    public abstract void update();
}

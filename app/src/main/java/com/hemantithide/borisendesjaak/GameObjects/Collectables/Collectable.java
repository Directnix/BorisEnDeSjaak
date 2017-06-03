package com.hemantithide.borisendesjaak.GameObjects.Collectables;

import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.GameObjects.GameObject;
import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;

/**
 * Created by Daniel on 01/06/2017.
 */

public abstract class Collectable extends GameObject {

    GameActivity.Sound sound;
    int lifespan;

    public Collectable(GameSurfaceView game, int ID) {
        super(game);
        horizLaneID = ID;

        posY = (int)(game.metrics.heightPixels * -0.1);
    }

    @Override
    public void update() {
        posX = game.laneXValues.get(horizLaneID);

        lifespan += game.gameSpeed;

        posY = lifespan;

        if(Math.abs(posY - game.player.posY) < sprite.getHeight() && Math.abs(posX - game.player.posX) < sprite.getWidth()) {
            game.activity.playSound(sound);
            game.player.collect(this);
            destroy();
        }

        if(lifespan > game.metrics.heightPixels)
            destroy();
    }
}

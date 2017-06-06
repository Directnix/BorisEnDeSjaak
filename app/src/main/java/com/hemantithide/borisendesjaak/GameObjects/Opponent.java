package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.SpriteLibrary;

/**
 * Created by Nick van Endhoven, 2119719 on 06-Jun-17.
 */

public class Opponent extends GameObject {
    public Opponent(GameSurfaceView game) {
        super(game);
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.PLAYER);

        posX = game.laneXValues.get(horizLaneID);
        posY = game.laneYValues.get(vertiLaneID);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, posX + (int)(sprite.getWidth() / 1.25), posY, null);
    }

    @Override
    public void update() {

    }
}

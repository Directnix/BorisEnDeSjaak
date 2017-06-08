package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.Engine.GameConstants;
import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.SpriteLibrary;

/**
 * Created by Nick van Endhoven, 2119719 on 06-Jun-17.
 */

public class Opponent extends GameObject {

    public int targetX;
    public int targetY;

    public Opponent(GameSurfaceView game) {
        super(game);
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.PLAYER);

        posX = game.laneXValues.get(horizLaneID);
        posY = game.laneYValues.get(vertiLaneID);

        targetX = posX;
        targetY = posY;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, posX + (int)(sprite.getWidth() / 1.25), posY, null);
    }

    @Override
    public void update() {
        if(Math.abs(posX - targetX) < (GameConstants.SWIPE_SPEED_HORIZONTAL * game.speedMultiplier))
            posX = targetX;
        if(posX < targetX)
            posX += GameConstants.SWIPE_SPEED_HORIZONTAL * game.speedMultiplier;
        else if(posX > targetX)
            posX -= GameConstants.SWIPE_SPEED_HORIZONTAL * game.speedMultiplier;

        if(Math.abs(posY - targetY) < (GameConstants.SWIPE_SPEED_VERTICAL * game.speedMultiplier))
            posY = targetY;
        if(posY < targetY)
            posY += GameConstants.SWIPE_SPEED_VERTICAL * game.speedMultiplier;
        else if(posY > targetY)
            posY -= GameConstants.SWIPE_SPEED_VERTICAL * game.speedMultiplier;
    }
}

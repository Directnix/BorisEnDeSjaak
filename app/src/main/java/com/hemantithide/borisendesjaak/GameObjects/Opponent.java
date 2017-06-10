package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

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

        Bitmap cropped1 = Bitmap.createBitmap(sprite, (sprite.getWidth()/4) * 0, 0, sprite.getWidth()/4, sprite.getHeight());
        Bitmap cropped2 = Bitmap.createBitmap(sprite, (sprite.getWidth()/4) * 1, 0, sprite.getWidth()/4, sprite.getHeight());
        Bitmap cropped3 = Bitmap.createBitmap(sprite, (sprite.getWidth()/4) * 2, 0, sprite.getWidth()/4, sprite.getHeight());
        Bitmap cropped4 = Bitmap.createBitmap(sprite, (sprite.getWidth()/4) * 3, 0, sprite.getWidth()/4, sprite.getHeight());
        spritesheet.add(cropped1);
        spritesheet.add(cropped2);
        spritesheet.add(cropped3);
        spritesheet.add(cropped4);


        horizLaneID = 2;
        vertiLaneID = 3;

        posX = game.laneXValues.get(horizLaneID);
        posY = game.laneYValues.get(vertiLaneID);

        targetX = posX;
        targetY = posY;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint alphaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        alphaPaint.setAlpha(63);

        if (game.updateCounter % GameConstants.ANIMATION_SPEED == 0) {
            animIndex++;

            if (animIndex >= spritesheet.size())
                animIndex = 0;
        }

        canvas.drawBitmap(spritesheet.get(animIndex), posX - (spritesheet.get(animIndex).getWidth() / 2), posY, alphaPaint);
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

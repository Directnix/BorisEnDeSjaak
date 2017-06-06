package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.SpriteLibrary;

/**
 * Created by Daniel on 31/05/2017.
 */

public class Rock extends GameObject {

    int lifespan;

    public Rock(GameSurfaceView game, int ID) {
        super(game);
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.ROCK);
        drawPriority = 5;

        lifespan = (int)(-0.1 * game.metrics.heightPixels);
        horizLaneID = ID;

        posY = (int)(game.metrics.heightPixels * -0.1);
    }

    @Override
    public void draw(Canvas canvas) {

//        if(lifespan < game.metrics.heightPixels * 0.65) {
            canvas.drawBitmap(sprite, posX + (sprite.getWidth() / 2), posY, null);
//        } else {
//            Paint alphaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            alphaPaint.setAlpha(127);
//            canvas.drawBitmap(sprite, posX + (sprite.getWidth() / 2), posY, alphaPaint);
//        }
    }

    public void update() {
        posX = game.laneXValues.get(horizLaneID);

        lifespan += game.gameSpeed;

        posY = lifespan;

        if(game.player.collisionTimer == 0 && Math.abs(posY - game.player.posY) < sprite.getHeight() && Math.abs(posX - game.player.posX) < sprite.getWidth()) {
            game.player.collision(this);
        }

        if(lifespan > game.metrics.heightPixels)
            destroy();
    }
}

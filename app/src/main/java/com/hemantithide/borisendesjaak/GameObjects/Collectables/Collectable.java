package com.hemantithide.borisendesjaak.GameObjects.Collectables;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.hemantithide.borisendesjaak.Engine.GameConstants;
import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.GameObjects.GameObject;
import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;

/**
 * Created by Daniel on 01/06/2017.
 */

public abstract class Collectable extends GameObject {

    public GameActivity.Sound sound;
    int lifespan;

    boolean isGhost;

    public Collectable(GameSurfaceView game, int ID, boolean ghost) {
        super(game);
        horizLaneID = ID;
        drawPriority = 4;

        isGhost = ghost;

        posY = (int)(game.metrics.heightPixels * -0.1);
    }

    @Override
    public void draw(Canvas canvas) {

        if(!isGhost) {
            canvas.drawBitmap(sprite, posX - (sprite.getWidth() / 2), posY, null);
        } else {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setAlpha(63);

            canvas.drawBitmap(sprite, posX - (sprite.getWidth() / 2), posY, paint);
        }
    }

    @Override
    public void update() {
        posX = game.laneXValues.get(horizLaneID);

        lifespan += game.gameSpeed * GameActivity.relativeScreenSizeFactor;

        posY = lifespan;

//        if(game.opponent != null) {
//            if (Math.abs(posY - game.opponent.posY) < sprite.getHeight() && Math.abs(posX - game.opponent.posX) < sprite.getWidth()) {
//                destroy();
//            }
//        }

        if(!isGhost) {
            if (Math.abs(posY - game.player.posY) < sprite.getHeight() && Math.abs(posX - game.player.posX) < sprite.getWidth()) {
                game.player.collect(this);
            }
        }

        if(lifespan > game.metrics.heightPixels)
            destroy();
    }
}

package com.hemantithide.borisendesjaak.GameObjects.Collectables;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.SpriteLibrary;

/**
 * Created by Daniel on 01/06/2017.
 */

public class Apple extends Collectable {

    public Apple(GameSurfaceView game, int ID, boolean ghost) {
        super(game, ID, ghost);

        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.APPLE);
        sound = GameActivity.Sound.KINKER;

        lifespan = -2 * sprite.getHeight();
    }

//    @Override
//    public void draw(Canvas canvas) {
//
//        if (game.player.health < 3) {
//            canvas.drawBitmap(sprite, posX + (sprite.getWidth() / 2), posY, null);
//        } else {
//            Paint alphaPaint = new Paint();
//            alphaPaint.setAlpha(63);
//            canvas.drawBitmap(sprite, posX + (sprite.getWidth() / 2), posY, alphaPaint);
//        }
//    }
}

package com.hemantithide.borisendesjaak.GameObjects.Collectables;

import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.SpriteLibrary;

/**
 * Created by Daniel on 01/06/2017.
 */

public class Kinker extends Collectable {

    public Kinker(GameSurfaceView game, int ID) {
        super(game, ID);

        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.KINKER);
        sound = GameActivity.Sound.KINKER_2;

        lifespan = -2 * sprite.getHeight();
    }

//    @Override
//    public void draw(Canvas canvas) {
//        canvas.drawBitmap(sprite, posX + (sprite.getWidth() / 4), posY, null);
//    }
}

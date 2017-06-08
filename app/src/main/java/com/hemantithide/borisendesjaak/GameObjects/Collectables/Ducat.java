package com.hemantithide.borisendesjaak.GameObjects.Collectables;

import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.SpriteLibrary;
import com.hemantithide.borisendesjaak.GameActivity;

/**
 * Created by Daniel on 03/06/2017.
 */

public class Ducat extends Collectable {

    public Ducat(GameSurfaceView game, int ID) {
        super(game, ID);

        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT);
        sound = GameActivity.Sound.POWERUP;

        lifespan = -2 * sprite.getHeight();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, posX + (sprite.getWidth() / 3), posY, null);
    }
}

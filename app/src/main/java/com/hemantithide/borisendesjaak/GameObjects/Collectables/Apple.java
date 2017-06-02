package com.hemantithide.borisendesjaak.GameObjects.Collectables;

import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.SpriteLibrary;

/**
 * Created by Daniel on 01/06/2017.
 */

public class Apple extends Collectable {

    public Apple(GameSurfaceView game, int ID) {
        super(game, ID);

//        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.apple);
//        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels, game.metrics.widthPixels, true);
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.APPLE);

        lifespan = (int)(-0.1 * game.metrics.heightPixels);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, posX + (sprite.getWidth() / 2), posY, null);
    }

    @Override
    public void update() {

        posX = game.laneXValues.get(horizLaneID);

        lifespan += game.gameSpeed;

        posY = lifespan;

        if(Math.abs(posY - game.player.posY) < sprite.getHeight() && Math.abs(posX - game.player.posX) < sprite.getWidth()) {
            game.activity.playSound(GameActivity.Sound.KINKER);
            game.player.collect(this);
            destroy();
        }

        if(lifespan > game.metrics.heightPixels)
            destroy();

    }
}

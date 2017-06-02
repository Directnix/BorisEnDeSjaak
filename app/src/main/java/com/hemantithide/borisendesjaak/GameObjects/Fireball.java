package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.GameSurfaceView;
import com.hemantithide.borisendesjaak.R;
import com.hemantithide.borisendesjaak.SpriteLibrary;

import java.util.LinkedList;

/**
 * Created by Daniel on 01/06/2017.
 */

public class Fireball extends GameObject {

    private int lifespan;

    public Fireball(GameSurfaceView game, int ID) {
        super(game);
//        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.fireball);
//        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 10, game.metrics.widthPixels / 5, true);
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.FIREBALL);

        lifespan = sprite.getHeight();
        horizLaneID = ID;

        posX = game.laneXValues.get(horizLaneID);

        Log.e("Fireball Lane", ID + "");
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, posX + 32, posY, null);
    }

    @Override
    public void update() {

        lifespan += 10;

        posY = game.metrics.heightPixels - lifespan;

        if(game.player.collisionTimer == 0 && Math.abs(posY - game.player.posY) < sprite.getHeight() && Math.abs(posX - game.player.posX) < sprite.getWidth()) {
            game.activity.playSound(GameActivity.Sound.ROCK_HIT);
            game.player.collision(this);
        }

        LinkedList<GameObject> toCheck = new LinkedList<>(game.gameObjects);
        for(GameObject g : toCheck) {
            if(g instanceof Rock)
                if(Math.abs(posY - g.posY) < sprite.getHeight() && Math.abs(posX - g.posX) < sprite.getWidth()
                        && g.posY < game.metrics.heightPixels * 0.65) {
                    game.activity.playSound(GameActivity.Sound.FIRE_ON_ROCK);
                    destroy();
                }
        }

        if(lifespan > (game.metrics.heightPixels + sprite.getHeight()))
            destroy();
    }
}

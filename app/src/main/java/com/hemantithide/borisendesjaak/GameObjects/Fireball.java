package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.SpriteLibrary;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Daniel on 01/06/2017.
 */

public class Fireball extends GameObject {

    private int lifespan;
    private double speedMultiplier;

    public Fireball(GameSurfaceView game, int ID, double speedMultiplier) {
        super(game);
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.FIREBALL);

        Bitmap subsprite;
        for(int i = 0; i < 4; i++) {
            subsprite = Bitmap.createBitmap(sprite, (sprite.getWidth() / 4) * i, 0, sprite.getWidth() / 4, sprite.getHeight());
            spritesheet.add(subsprite);
        }

        drawPriority = 2;

        lifespan = sprite.getHeight();
        horizLaneID = ID;

        posX = game.laneXValues.get(horizLaneID);
        posY = game.metrics.heightPixels - lifespan;

        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public void update() {

        lifespan += 10 * speedMultiplier;

        posY = game.metrics.heightPixels - lifespan;

        if (game.player.collisionTimer == 0 && Math.abs(posY - game.player.posY) < spritesheet.get(animIndex).getHeight() && Math.abs(posX - game.player.posX) < spritesheet.get(animIndex).getWidth()) {
            game.player.collision(this);
        }

        LinkedList<GameObject> toCheck = new LinkedList<>(game.gameObjects);
        for (GameObject g : toCheck) {
            if (g instanceof Rock)
                if (Math.abs(posY - g.posY) < spritesheet.get(animIndex).getHeight() && Math.abs(posX - g.posX) < spritesheet.get(animIndex).getWidth()
                        && g.posY < game.metrics.heightPixels * 0.65) {
                    game.activity.playSound(GameActivity.Sound.FIRE_ON_ROCK);
                    destroy();
                }
        }

        if (lifespan > (game.metrics.heightPixels + sprite.getHeight()))
            destroy();
    }
}

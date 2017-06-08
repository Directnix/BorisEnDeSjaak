package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Kinker;

import java.util.ArrayList;

/**
 * Created by Daniel on 31/05/2017.
 */

public abstract class GameObject {

    public GameSurfaceView game;

    public Bitmap sprite;
    ArrayList<Bitmap> spritesheet = new ArrayList<>();
    public int drawPriority;
    private int animIndex;
    private int animSpeed = 5;

    public int horizLaneID;
    public int vertiLaneID;

    public int posX;
    public int posY;

    public GameObject(GameSurfaceView game) {
        this.game = game;
        game.gameObjects.add(this);
    }

    public void draw(Canvas canvas) {
        if(!spritesheet.isEmpty()) {
            if (game.updateCounter % animSpeed == 0) {
                animIndex++;

                if (animIndex >= spritesheet.size())
                    animIndex = 0;
            }

            if (spritesheet.size() > 0) {
                canvas.drawBitmap(spritesheet.get(animIndex), posX - (spritesheet.get(animIndex).getWidth() / 2), posY, null);
            }
        } else {
            canvas.drawBitmap(sprite, posX - (sprite.getWidth()/2), posY, null);
        }
    }

    public abstract void update();

    public void destroy() {
        game.gameObjects.remove(this);

        if(this instanceof Kinker) {
            game.kinker = null;
        }
    }
}

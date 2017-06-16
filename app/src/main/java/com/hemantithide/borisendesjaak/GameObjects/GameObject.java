package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.hemantithide.borisendesjaak.Engine.GameConstants;
import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Kinker;
import com.hemantithide.borisendesjaak.Network.Client;
import com.hemantithide.borisendesjaak.Network.Server;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Daniel on 31/05/2017.
 */

public abstract class GameObject {

    public GameSurfaceView game;

    public Bitmap sprite;
    ArrayList<Bitmap> spritesheet = new ArrayList<>();
    public int drawPriority;
    int animIndex;

    public int horizLaneID;
    public int vertiLaneID;

    public int posX;
    public int posY;

    private static int objectIDcount;
    public int objectID;

    public boolean grabbedByDragon;

    public GameObject(GameSurfaceView game) {
        this.game = game;
        game.gameObjects.add(this);

        objectID = objectIDcount;
        objectIDcount++;
    }

    public void draw(Canvas canvas) {
        if (!spritesheet.isEmpty()) {
            if (game.updateCounter % GameConstants.ANIMATION_SPEED == 0) {
                animIndex++;

                if (animIndex >= spritesheet.size())
                    animIndex = 0;
            }

            if (spritesheet.size() > 0) {
                canvas.drawBitmap(spritesheet.get(animIndex), posX - (spritesheet.get(animIndex).getWidth() / 2), posY, null);
            }
        } else {
            canvas.drawBitmap(sprite, posX - (sprite.getWidth() / 2), posY, null);
        }
    }

    public abstract void update();

    public void destroy() {
        if (GameActivity.IS_MULTIPLAYER) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        if (GameActivity.IS_SERVER) {
                            Server.out.writeUTF("destroy_" + getClass().toString() + "_" + objectID);
                        } else if (GameActivity.IS_CLIENT) {
                            Client.out.writeUTF("destroy_" + getClass().toString() + "_" + objectID);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        game.gameObjects.remove(this);

        if (this instanceof Kinker) {
            game.kinker = null;
        }
    }

    public void destroyExternally() {
        game.gameObjects.remove(this);

        if (this instanceof Kinker) {
            game.kinker = null;
        }
    }
}

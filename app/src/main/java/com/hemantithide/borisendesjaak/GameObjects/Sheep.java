package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.hemantithide.borisendesjaak.Engine.GameConstants;
import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Apple;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Collectable;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Ducat;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Kinker;
import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.SpriteLibrary;
import com.hemantithide.borisendesjaak.Network.Client;
import com.hemantithide.borisendesjaak.Network.Server;
import com.hemantithide.borisendesjaak.Visuals.HealthBar;

import java.io.IOException;

/**
 * Created by Daniel on 30/05/2017.
 */

public class Sheep extends GameObject {


    private int playerID;

    private boolean alive = true;
    public int health = GameConstants.SHEEP_HEALTH;

    public int targetX;
    public int targetY;

    HealthBar healthBar;

    int collisionTimer;
    boolean blinkInvisible;

    private int powerupCounter;

    public int appleCounter;
    public int requiredApples = GameConstants.SHEEP_REQUIRED_APPLES;

    public int ducatCounter;

    public boolean grabbedByDragon;

    public int ducatsCollected;
    public int applesCollected;
    public int kinkersCollected;

    public Sheep(GameSurfaceView game, int playerID) {
        super(game);
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.PLAYER);
        Bitmap cropped1 = Bitmap.createBitmap(sprite, (sprite.getWidth() / 4) * 0, 0, sprite.getWidth() / 4, sprite.getHeight());
        Bitmap cropped2 = Bitmap.createBitmap(sprite, (sprite.getWidth() / 4) * 1, 0, sprite.getWidth() / 4, sprite.getHeight());
        Bitmap cropped3 = Bitmap.createBitmap(sprite, (sprite.getWidth() / 4) * 2, 0, sprite.getWidth() / 4, sprite.getHeight());
        Bitmap cropped4 = Bitmap.createBitmap(sprite, (sprite.getWidth() / 4) * 3, 0, sprite.getWidth() / 4, sprite.getHeight());
        spritesheet.add(cropped1);
        spritesheet.add(cropped2);
        spritesheet.add(cropped3);
        spritesheet.add(cropped4);

        drawPriority = 3;

        this.playerID = playerID;

        healthBar = new HealthBar(this);

        horizLaneID = 2;
        vertiLaneID = 3;

        posX = game.laneXValues.get(horizLaneID);
        posY = game.laneYValues.get(vertiLaneID);

        targetX = posX;
        targetY = posY;
    }

    public void moveLeft() {

        horizLaneID--;

        if (horizLaneID < 0)
            horizLaneID = 0;
        else
            game.activity.playSound(GameActivity.Sound.SWIPE);

        targetX = game.laneXValues.get(horizLaneID);
    }

    public void moveRight() {

        horizLaneID++;

        if (horizLaneID > 4)
            horizLaneID = 4;
        else
            game.activity.playSound(GameActivity.Sound.SWIPE);

        targetX = game.laneXValues.get(horizLaneID);
    }

    public void moveDown() {

        vertiLaneID++;

        if (vertiLaneID > 5)
            vertiLaneID = 5;
        else
            game.activity.playSound(GameActivity.Sound.SWIPE);

        targetY = game.laneYValues.get(vertiLaneID);
    }

    public void moveUp() {

        vertiLaneID--;

        if (vertiLaneID < 1)
            vertiLaneID = 1;
        else
            game.activity.playSound(GameActivity.Sound.SWIPE);

        targetY = game.laneYValues.get(vertiLaneID);
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public void draw(Canvas canvas) {

        if (health > 0)
            healthBar.draw(canvas);

        if (collisionTimer > 0 && collisionTimer % (GameConstants.SHEEP_COLLISION_TIMER / 10) == 0)
            blinkInvisible = !blinkInvisible;

        if (collisionTimer == 0 || !blinkInvisible) {
            super.draw(canvas);
        }

        if (powerupCounter > 0) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.GREEN);
            paint.setAlpha(55 + (int) (powerupCounter * 0.28));

//            canvas.drawBitmap(sprite, posX + 32, posY, paint);
//            canvas.drawBitmap(sprite, posX + (int)(sprite.getWidth() / 1.25), posY + 100, paint);
//            canvas.drawCircle(posX + (int)(sprite.getWidth() / 1.25) + (sprite.getWidth() / 2), posY  + (sprite.getHeight() / 2), sprite.getWidth(), paint);
            canvas.drawCircle(posX, posY + (spritesheet.get(animIndex).getHeight() / 2), spritesheet.get(animIndex).getWidth(), paint);
        }
    }

    @Override
    public void update() {

        if (Math.abs(posX - targetX) < (GameConstants.SWIPE_SPEED_HORIZONTAL * game.speedMultiplier))
            posX = targetX;
        if (posX < targetX)
            posX += GameConstants.SWIPE_SPEED_HORIZONTAL * game.speedMultiplier;
        else if (posX > targetX)
            posX -= GameConstants.SWIPE_SPEED_HORIZONTAL * game.speedMultiplier;

        if (Math.abs(posY - targetY) < (GameConstants.SWIPE_SPEED_VERTICAL * game.speedMultiplier))
            posY = targetY;
        if (posY < targetY)
            posY += GameConstants.SWIPE_SPEED_VERTICAL * game.speedMultiplier;
        else if (posY > targetY)
            posY -= GameConstants.SWIPE_SPEED_VERTICAL * game.speedMultiplier;

        if (collisionTimer > 0)
            collisionTimer--;

        if (powerupCounter > 0) {
            powerupCounter--;
        }
    }

    public void collision(GameObject source) {
        if (health > 0) {
            if (powerupCounter == 0) {
                game.activity.playSound(GameActivity.Sound.ROCK_HIT);
                collisionTimer = GameConstants.SHEEP_COLLISION_TIMER;
                blinkInvisible = true;

                if (health > 1) {
                    health--;
                    healthBar.update(game.player.health);
                } else {
                    health--;
                    healthBar.update(game.player.health);
                    collisionTimer = 0;
                    game.activateState(GameSurfaceView.GameState.END_GAME);
                    alive = false;

                    if (GameActivity.IS_MULTIPLAYER) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (GameActivity.IS_CLIENT) {
                                        Client.out.writeUTF("end_game");
                                    } else if (GameActivity.IS_SERVER) {
                                        Server.out.writeUTF("end_game");
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            } else {
                game.activity.playSound(GameActivity.Sound.FIRE_ON_ROCK);
                powerupCounter = 0;
            }

            source.destroy();
        }
    }

    public void collect(Collectable c) {

        if (!game.activeStates.contains(GameSurfaceView.GameState.END_GAME)) {
            if (c instanceof Kinker) {
                powerupCounter = GameConstants.SHEEP_KINKER_TIMER;
                game.activity.playSound(c.sound);
                c.destroy();
                kinkersCollected++;
            } else if (c instanceof Apple) {

                if (health < 3) {
                    appleCounter++;
                    game.activity.playSound(c.sound);
                    c.destroy();
                    applesCollected++;

                    if (appleCounter == requiredApples) {
                        game.activity.playSound(GameActivity.Sound.POWERUP_LOOP);
                        health++;
                        healthBar.update(health);
                        appleCounter = 0;
                        requiredApples += GameConstants.SHEEP_REQUIRED_APPLES_INCREASE;
                    }
                }
            } else if (c instanceof Ducat) {
                ducatCounter++;
                game.activity.playSound(c.sound);
                c.destroy();
                ducatsCollected++;

                if (GameActivity.IS_MULTIPLAYER) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (GameActivity.IS_SERVER) {
                                    Server.out.writeUTF("ducat");
                                } else if (GameActivity.IS_CLIENT) {
                                    Client.out.writeUTF("ducat");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        }
    }

    public String getUsername() {
        return game.activity.username;
    }
}

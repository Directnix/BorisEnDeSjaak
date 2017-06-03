package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Apple;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Collectable;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Ducat;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Kinker;
import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.SpriteLibrary;
import com.hemantithide.borisendesjaak.Visuals.HealthBar;

/**
 * Created by Daniel on 30/05/2017.
 */

public class Sheep extends GameObject {


    private int playerID;

    private boolean alive = true;
    int health = 3;

    private int targetX;
    private int targetY;

    HealthBar healthBar;

    int collisionTimer;
    boolean blinkInvisible;

    private int powerupCounter;

    public int appleCounter;
    public int requiredApples = 10;

    public int ducatCounter;

    public boolean grabbedByDragon;


    public Sheep(GameSurfaceView game, int playerID) {
        super(game);
//        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.sheep_placeholder);
//        sprite = Bitmap.createScaledBitmap(sprite, (game.metrics.widthPixels / 300) * 27, (game.metrics.widthPixels / 300) * 48, false);
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.PLAYER);

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

        if(horizLaneID < 0)
            horizLaneID = 0;
        else
            game.activity.playSound(GameActivity.Sound.SWIPE);

        targetX = game.laneXValues.get(horizLaneID);
    }

    public void moveRight() {

        horizLaneID++;

        if(horizLaneID > 4)
            horizLaneID = 4;
        else
            game.activity.playSound(GameActivity.Sound.SWIPE);

        targetX = game.laneXValues.get(horizLaneID);
    }

    public void moveDown() {

        vertiLaneID++;

        if(vertiLaneID > 5)
            vertiLaneID = 5;
        else
            game.activity.playSound(GameActivity.Sound.SWIPE);

        targetY = game.laneYValues.get(vertiLaneID);
    }

    public void moveUp() {

        vertiLaneID--;

        if(vertiLaneID < 1)
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

        if(health > 0)
            healthBar.draw(canvas);

        if(collisionTimer > 0 && collisionTimer % 12 == 0)
            blinkInvisible = !blinkInvisible;

        if(collisionTimer == 0 || !blinkInvisible) {
            canvas.drawBitmap(sprite, posX + 32, posY, null);
        }

        if(powerupCounter > 0) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.GREEN);
            paint.setAlpha(55 + (int)(powerupCounter * 0.28));

//            canvas.drawBitmap(sprite, posX + 32, posY, paint);
            canvas.drawCircle(posX + (sprite.getWidth()), posY + (sprite.getHeight() / 2), sprite.getHeight(), paint);
        }
    }

    @Override
    public void update() {

        if(Math.abs(posX - targetX) < (20 * game.speedMultiplier))
            posX = targetX;
        if(posX < targetX)
            posX += 20 * game.speedMultiplier;
        else if(posX > targetX)
            posX -= 20 * game.speedMultiplier;

        if(Math.abs(posY - targetY) < (15 * game.speedMultiplier))
            posY = targetY;
        if(posY < targetY)
            posY += 15 * game.speedMultiplier;
        else if(posY > targetY)
            posY -= 12 * game.speedMultiplier;

        if(collisionTimer > 0)
            collisionTimer--;

        if(powerupCounter > 0) {
            powerupCounter--;
        }
    }

    public void collision(GameObject source) {
        if(health > 0) {
            if (powerupCounter == 0) {
                game.activity.playSound(GameActivity.Sound.ROCK_HIT);
                collisionTimer = 120;
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
                }
            } else {
                game.activity.playSound(GameActivity.Sound.FIRE_ON_ROCK);
                powerupCounter = 0;
            }

            source.destroy();
        }
    }

    public void collect(Collectable c) {

        if(!game.activeStates.contains(GameSurfaceView.GameState.END_GAME)) {
            if (c instanceof Kinker) {
                powerupCounter = 350;
            } else if (c instanceof Apple) {
                appleCounter++;

                if (appleCounter == requiredApples && health < 3) {
                    game.activity.playSound(GameActivity.Sound.POWERUP_LOOP);
                    health++;
                    healthBar.update(health);
                    appleCounter = 0;
                    requiredApples += 5;
                } else if (appleCounter == requiredApples && health == 3) {
                    appleCounter = requiredApples - 1;
                }
            } else if (c instanceof Ducat) {
                ducatCounter++;
            }
        }
    }

    public String getUsername() {
        return game.activity.username;
    }
}

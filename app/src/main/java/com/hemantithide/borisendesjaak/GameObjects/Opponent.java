package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.hemantithide.borisendesjaak.Engine.GameConstants;
import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.SpriteLibrary;
import com.hemantithide.borisendesjaak.Visuals.HealthBar;

/**
 * Created by Nick van Endhoven, 2119719 on 06-Jun-17.
 */

public class Opponent extends GameObject {

    public int targetX;
    public int targetY;

    public int health = GameConstants.SHEEP_HEALTH;
    private HealthBar healthBar;

    private int collisionTimer;
    private boolean blinkInvisible;
    private double powerupCounter;

    public Opponent(GameSurfaceView game) {
        super(game);
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.PLAYER);

        Bitmap cropped1 = Bitmap.createBitmap(sprite, (sprite.getWidth()/4) * 0, 0, sprite.getWidth()/4, sprite.getHeight());
        Bitmap cropped2 = Bitmap.createBitmap(sprite, (sprite.getWidth()/4) * 1, 0, sprite.getWidth()/4, sprite.getHeight());
        Bitmap cropped3 = Bitmap.createBitmap(sprite, (sprite.getWidth()/4) * 2, 0, sprite.getWidth()/4, sprite.getHeight());
        Bitmap cropped4 = Bitmap.createBitmap(sprite, (sprite.getWidth()/4) * 3, 0, sprite.getWidth()/4, sprite.getHeight());
        spritesheet.add(cropped1);
        spritesheet.add(cropped2);
        spritesheet.add(cropped3);
        spritesheet.add(cropped4);

        healthBar = new HealthBar(this);

        horizLaneID = 2;
        vertiLaneID = 3;

        posX = game.laneXValues.get(horizLaneID);
        posY = game.laneYValues.get(vertiLaneID);

        targetX = posX;
        targetY = posY;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint alphaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        alphaPaint.setAlpha(63);

        if (game.updateCounter % GameConstants.ANIMATION_SPEED == 0) {
            animIndex++;

            if (animIndex >= spritesheet.size())
                animIndex = 0;
        }

        if (health > 0)
            healthBar.draw(canvas, alphaPaint);

        if (collisionTimer > 0 && collisionTimer % (GameConstants.SHEEP_COLLISION_TIMER / 10) == 0)
            blinkInvisible = !blinkInvisible;

        if (collisionTimer == 0 || !blinkInvisible) {
            canvas.drawBitmap(spritesheet.get(animIndex), posX - (spritesheet.get(animIndex).getWidth() / 2), posY, alphaPaint);
        }

        if (powerupCounter > 0) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.GREEN);
            paint.setAlpha(55 + (int) (powerupCounter * 0.28));
            canvas.drawCircle(posX, posY + (spritesheet.get(animIndex).getHeight() / 2), spritesheet.get(animIndex).getWidth(), paint);
        }
    }

    @Override
    public void update() {

        if(powerupCounter > 0)
            powerupCounter--;

        if(Math.abs(posX - targetX) < (GameConstants.SWIPE_SPEED_HORIZONTAL * game.speedMultiplier))
            posX = targetX;
        if(posX < targetX)
            posX += GameConstants.SWIPE_SPEED_HORIZONTAL * game.speedMultiplier;
        else if(posX > targetX)
            posX -= GameConstants.SWIPE_SPEED_HORIZONTAL * game.speedMultiplier;

        if(Math.abs(posY - targetY) < (GameConstants.SWIPE_SPEED_VERTICAL * game.speedMultiplier))
            posY = targetY;
        if(posY < targetY)
            posY += GameConstants.SWIPE_SPEED_VERTICAL * game.speedMultiplier;
        else if(posY > targetY)
            posY -= GameConstants.SWIPE_SPEED_VERTICAL * game.speedMultiplier;
    }

    public void takeDamage() {
        if (health > 1) {
            health--;
            healthBar.update(health);

            collisionTimer = GameConstants.SHEEP_COLLISION_TIMER;
            blinkInvisible = true;
        }
    }

    public void setPowerupCounter() {
        powerupCounter = GameConstants.SHEEP_KINKER_TIMER;
    }
}

package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.hemantithide.borisendesjaak.Engine.GameConstants;
import com.hemantithide.borisendesjaak.Engine.GameNotificationManager;
import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.Engine.GameSurfaceView;
import com.hemantithide.borisendesjaak.Engine.SpriteLibrary;
import com.hemantithide.borisendesjaak.GameObjects.Collectables.Kinker;
import com.hemantithide.borisendesjaak.MainActivity;
import com.hemantithide.borisendesjaak.User;

import java.util.ArrayList;

import static com.hemantithide.borisendesjaak.GameObjects.Dragon.State.ABSENT;
import static com.hemantithide.borisendesjaak.GameObjects.Dragon.State.PRESENT;

/**
 * Created by Daniel on 01/06/2017.
 */

public class Dragon extends GameObject {

    private boolean fireBallAnim = false;
    private ArrayList<Bitmap> spritesheetFire = new ArrayList<>();

    public int visitCounter;

    public void increaseVisitCounter() { visitCounter++; }

    public enum State {PRESENT, ABSENT}

    public State state = ABSENT;

    private boolean initFinished;

    int lifespan;

    int targetLaneX;
    int targetLaneY;
    int targetX;
    int targetY;

    private int fireballCooldown;

    private int firewaveCharge;

    private Bitmap warningSign;
    private boolean warningBlink;

    public boolean flyingOut = false;

    public Dragon(GameSurfaceView game) {
        super(game);
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DRAGON);
//        Bitmap cropped1 = Bitmap.createBitmap(sprite, (sprite.getWidth() / 4) * 0, 0, sprite.getWidth() / 4, sprite.getHeight());
//        Bitmap cropped2 = Bitmap.createBitmap(sprite, (sprite.getWidth() / 4) * 1, 0, sprite.getWidth() / 4, sprite.getHeight());
//        Bitmap cropped3 = Bitmap.createBitmap(sprite, (sprite.getWidth() / 4) * 2, 0, sprite.getWidth() / 4, sprite.getHeight());
//        Bitmap cropped4 = Bitmap.createBitmap(sprite, (sprite.getWidth() / 4) * 3, 0, sprite.getWidth() / 4, sprite.getHeight());
//        spritesheet.add(cropped1);
//        spritesheet.add(cropped2);
//        spritesheet.add(cropped3);
//        spritesheet.add(cropped4);

//        Bitmap spriteFire = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.SHOOTFIRE);
//        Bitmap fCropped1 = Bitmap.createBitmap(spriteFire, (spriteFire.getWidth() / 2) * 0, 0, spriteFire.getWidth() / 2, spriteFire.getHeight());
//        Bitmap fCropped2 = Bitmap.createBitmap(spriteFire, (spriteFire.getWidth() / 2) * 1, 0, spriteFire.getWidth() / 2, spriteFire.getHeight());
//        spritesheetFire.add(fCropped1);
//        spritesheetFire.add(fCropped2);

        warningSign = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.WARNING);

        drawPriority = 1;

        setState(ABSENT);

        targetLaneX = 2;
        targetX = game.laneXValues.get(targetLaneX);

        posX = game.laneXValues.get(targetLaneX);
        posY = game.metrics.heightPixels;
    }

    @Override
    public void draw(Canvas canvas) {
//        if (fireBallAnim) {
//            if (game.updateCounter % 5 == 0) {
//                animIndex++;
//
//                if (animIndex >= spritesheetFire.size()) {
//                    animIndex = 0;
//                }
//            }
//
//            fireBallAnim = false;
//        } else {
        if(MainActivity.user.attraction == User.Attraction.BORIS)
            canvas.drawBitmap(sprite, posX - (int) (sprite.getWidth() / 1.725), posY, null);
        else
            canvas.drawBitmap(sprite, posX - (sprite.getWidth() / 2), posY, null);

        if(state == PRESENT && !initFinished && !warningBlink) {
            canvas.drawBitmap(warningSign, game.laneXValues.get(targetLaneX) - (warningSign.getWidth() / 2), game.metrics.heightPixels * 0.75f, null);
        }

        if (firewaveCharge > 0 && !warningBlink) {
            for (int i = 0; i < 5; i++) {
                canvas.drawBitmap(warningSign, game.laneXValues.get(i) - (warningSign.getWidth() / 2), game.metrics.heightPixels * 0.75f, null);
            }
//        }
        }
    }

    @Override
    public void update() {

//        if (!flyingOut)
//            speedX = game.speedMultiplier;

        if (Math.abs(posX - targetX) < 16)
            posX = targetX;
        if (posX < targetX)
            posX += 16;
        else if (posX > targetX)
            posX -= 16;

        if (Math.abs(posY - targetY) < 6)
            posY = targetY;
        if (posY < targetY)
            posY += 6;
        else if (posY > targetY)
            posY -= 4;

        if(posY > targetY && !initFinished && game.updateCounter % 25 == 0)
            warningBlink = !warningBlink;

        if (state == PRESENT && !initFinished && posY == targetY) {
            game.activity.playSound(GameActivity.Sound.AYO_WHADDUP);
            initFinished = true;
            fireballCooldown = GameConstants.DRAGON_FIREBALL_INTERVAL;

//            GameNotificationManager.showNotification(GameNotificationManager.Notification.FIREBALL, true);
        }

        if (state == PRESENT && posY == targetY && posX == targetX && fireballCooldown == 0 && !game.activeStates.contains(GameSurfaceView.GameState.END_GAME)) {
            if(visitCounter > 2 && game.dragonPresentTimer < 140) {
                chargeFirewave();

                if (firewaveCharge > 0 && firewaveCharge % (GameConstants.DRAGON_FIREWAVE_CHARGE_TIMER / 7) == 0)
                    warningBlink = !warningBlink;
            } else
                spawnFireball();
            fireBallAnim = true;
        }

        if (fireballCooldown > 0) {
            fireballCooldown--;
        }

        if (flyingOut && posX == targetX && game.loser != null) {
            posY -= game.metrics.heightPixels / 60;

            if (Math.abs(posY - game.loser.posY) < game.loser.sprite.getHeight()) {
                if (!game.loser.grabbedByDragon)
                    game.activity.playSound(GameActivity.Sound.SHEEP_SCREECH);

                game.loser.grabbedByDragon = true;
                game.loser.posY = posY;

                if (game.loser.grabbedByDragon && posY <= targetY) {

                    game.activateState(GameSurfaceView.GameState.REWARDS);
                    game.calculateRewards();

                    game.loser.destroy();
                    destroy();
                }
            }
        }
    }

    private void chargeFirewave() {
        if (firewaveCharge == 0) {
            game.activity.playSound(GameActivity.Sound.BORIS_CHARGE);
            firewaveCharge++;

            warningBlink = false;

            targetLaneX = 2;
            targetX = game.laneXValues.get(targetLaneX);

            GameNotificationManager.showNotification(GameNotificationManager.Notification.FIREWAVE, true);
        } else if(firewaveCharge < GameConstants.DRAGON_FIREWAVE_CHARGE_TIMER) {
            firewaveCharge++;
            game.dragonPresentTimer++;
        } else {
            if(game.kinker == null)
                game.kinker = new Kinker(game, game.seed.kinkerSeq.get(game.spawnWaveCount), false);
            new Kinker(game, game.seed.rockSeqB.get(game.spawnWaveCount), false);

            new Fireball(game, 0, 1);
            new Fireball(game, 1, 1);
            new Fireball(game, 2, 1);
            new Fireball(game, 3, 1);
            new Fireball(game, 4, 1);
            fireballCooldown = GameConstants.DRAGON_FIREBALL_INTERVAL;
            game.activity.playSound(GameActivity.Sound.FIREBALL);

            firewaveCharge = 0;
            game.dragonPresentTimer = 0;
        }
    }

    private void spawnFireball() {

        new Fireball(game, targetLaneX, game.speedMultiplier);
        targetLaneX = game.seed.fireballSeq.get(game.spawnWaveCount);
        targetX = game.laneXValues.get(targetLaneX);
        fireballCooldown = GameConstants.DRAGON_FIREBALL_INTERVAL;
        game.activity.playSound(GameActivity.Sound.FIREBALL);
    }

    public void setState(State state) {

        switch (state) {
            case PRESENT:
                initFinished = false;
                game.activity.playSound(GameActivity.Sound.WOOSH);
                targetY = game.metrics.heightPixels - (int)(sprite.getHeight() / 2.75);
                targetLaneX = 2;
                targetX = game.laneXValues.get(targetLaneX);
                break;
            case ABSENT:
                targetY = game.metrics.heightPixels + sprite.getHeight();
                break;
        }

        this.state = state;
    }

    public void flyOut(Sheep sheep) {
        flyingOut = true;
        targetX = sheep.posX;
        targetY = -sprite.getHeight();
    }
}

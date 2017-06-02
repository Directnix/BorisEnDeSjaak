package com.hemantithide.borisendesjaak.GameObjects;

import android.graphics.Canvas;
import android.util.Log;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.GameSurfaceView;
import com.hemantithide.borisendesjaak.SpriteLibrary;

import static com.hemantithide.borisendesjaak.GameObjects.Dragon.State.ABSENT;
import static com.hemantithide.borisendesjaak.GameObjects.Dragon.State.PRESENT;

/**
 * Created by Daniel on 01/06/2017.
 */

public class Dragon extends GameObject {

    public enum State {PRESENT, ABSENT}
    public State state = ABSENT;

    public enum Orientation {SIDEWARDS, UPWARDS}
    public Orientation orientation = Orientation.SIDEWARDS;

    private boolean initFinished;

    int lifespan;

    int targetLaneX;
    int targetLaneY;
    int targetX;
    int targetY;

    private int fireballCooldown;

    public boolean flyingOut = false;

    public Dragon(GameSurfaceView game) {
        super(game);
//        sprite = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.draak);
//        sprite = Bitmap.createScaledBitmap(sprite, game.metrics.widthPixels / 2, game.metrics.widthPixels / 2, true);
        sprite = SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DRAGON);

        Log.e("Sprite", sprite + "");

        setState(ABSENT);

        targetLaneX = 2;
        targetX = game.laneXValues.get(targetLaneX);

        posX = game.laneXValues.get(targetLaneX);
        posY = game.metrics.heightPixels;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, posX - (sprite.getWidth() / 3), posY, null);
    }

    @Override
    public void update() {

        double speedX = 1;
        if (!flyingOut)
            speedX = game.speedMultiplier;

        if (Math.abs(posX - targetX) < (16 * speedX))
            posX = targetX;
        if (posX < targetX)
            posX += 16 * speedX;
        else if (posX > targetX)
            posX -= 16 * speedX;

        if (Math.abs(posY - targetY) < (6 * game.speedMultiplier))
            posY = targetY;
        if (posY < targetY)
            posY += 6 * game.speedMultiplier;
        else if (posY > targetY)
            posY -= 4 * game.speedMultiplier;

        if (state == PRESENT && !initFinished && posY == targetY) {
            game.activity.playSound(GameActivity.Sound.AYO_WHADDUP);
            initFinished = true;
            fireballCooldown = 42;
        }

        if (state == PRESENT && posY == targetY && posX == targetX && fireballCooldown == 0 && !game.activeStates.contains(GameSurfaceView.GameState.END_GAME)) {
            spawnFireball();
        }

        if (fireballCooldown > 0) {
            fireballCooldown--;
        }

        if (flyingOut && posX == targetX) {
            posY -= game.metrics.heightPixels / 60;

            if(Math.abs(posY - game.player.posY) < game.player.sprite.getHeight()) {
                if(!game.player.grabbedByDragon)
                    game.activity.playSound(GameActivity.Sound.SHEEP_SCREECH);

                game.player.grabbedByDragon = true;
                game.player.posY = posY;

                if(game.player.grabbedByDragon && posY == targetY) {
                    game.activateState(GameSurfaceView.GameState.LOSE_WINDOW);
                    game.player.destroy();
                    destroy();
                }
            }
        }
    }

    private void spawnFireball() {
        new Fireball(game, targetLaneX);
        targetLaneX = (int) (Math.random() * 5);
        targetX = game.laneXValues.get(targetLaneX);
        fireballCooldown = 42;
        game.activity.playSound(GameActivity.Sound.FIREBALL);
    }

    public void setState(State state) {

        switch (state) {
            case PRESENT:
                switch(orientation) {
                    case SIDEWARDS:
                        targetLaneX = 2;
                        targetX = game.laneXValues.get(targetLaneX);
                        targetY = game.metrics.heightPixels - (sprite.getHeight() / 2);

                        break;
                    case UPWARDS:
                        targetLaneX = 2;
                        targetX = game.laneXValues.get(targetLaneX);
                        targetY = game.metrics.heightPixels - (sprite.getHeight() / 2);
                        break;
                }

                initFinished = false;
                game.activity.playSound(GameActivity.Sound.WOOSH);
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

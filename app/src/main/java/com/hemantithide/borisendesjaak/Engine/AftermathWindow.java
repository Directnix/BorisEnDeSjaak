package com.hemantithide.borisendesjaak.Engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.hemantithide.borisendesjaak.GameActivity;
import com.hemantithide.borisendesjaak.R;

/**
 * Created by Daniel on 02/06/2017.
 */

public class AftermathWindow {

    private GameSurfaceView game;

    private int lifetime;

    private boolean showRewards;
    private boolean doneShowing;

    private int nextActionTimer;
    private int actionCounter;

    private int distanceDucats;
    private int collectedDucats;
    private int victoryDucats;
    private int multiplierDucats;
    public int totalDucats;

    public AftermathWindow(GameSurfaceView game) {
        this.game = game;
    }


    public void draw(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);

        Typeface tf = Typeface.createFromAsset(game.getContext().getAssets(), "RobotoCondensed-BoldItalic.ttf");
        paint.setTypeface(tf);

        paint.setTextAlign(Paint.Align.CENTER);

        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 4) - ((paint.descent() + paint.ascent()) / 2));

        paint.setTextSize(0.056f * lifetime);
        canvas.drawText("De winnaar is...", xPos, yPos - (canvas.getHeight() / 16), paint);

        paint.setTextSize(0.072f * lifetime);
        canvas.drawText(game.player.getUsername() + "!", xPos, yPos, paint);


        if (showRewards) {
            paint.setTextSize(48);

            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(game.getResources().getString(R.string.game_end_distance) + ":   ", xPos, (int) (yPos + (canvas.getHeight() * 0.2)), paint);
            canvas.drawText(game.getResources().getString(R.string.game_end_collected) + ":   ", xPos, (int) (yPos + (canvas.getHeight() * 0.25)), paint);
            canvas.drawText(game.getResources().getString(R.string.game_end_win_bonus) + ":   ", xPos, (int) (yPos + (canvas.getHeight() * 0.3)), paint);

            if(game.isMultiplierActive()) {
                canvas.drawText(game.getResources().getString(R.string.game_end_multiplier) + ":   ", xPos, (int) (yPos + (canvas.getHeight() * 0.35)), paint);
                canvas.drawText(game.getResources().getString(R.string.game_end_total) + ":   ", xPos, (int) (yPos + (canvas.getHeight() * 0.45)), paint);
            } else {
                canvas.drawText(game.getResources().getString(R.string.game_end_total) + ":   ", xPos, (int) (yPos + (canvas.getHeight() * 0.4)), paint);
            }

            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("     +" + distanceDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.2)), paint);
            canvas.drawText("     +" + collectedDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.25)), paint);
            canvas.drawText("     +" + victoryDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.3)), paint);

            if(game.isMultiplierActive()) {
                canvas.drawText("     +" + multiplierDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.35)), paint);
                canvas.drawText("     +" + totalDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.45)), paint);
            } else {
                canvas.drawText("     +" + totalDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.4)), paint);
            }

//            xPos -= (canvas.getWidth() / 25);

            canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT_ICON), xPos, (int) (yPos + (canvas.getHeight() * 0.175)), null);
            canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT_ICON), xPos, (int) (yPos + (canvas.getHeight() * 0.225)), null);
            canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT_ICON), xPos, (int) (yPos + (canvas.getHeight() * 0.275)), null);

            if(game.isMultiplierActive()) {
                canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT_ICON), xPos, (int) (yPos + (canvas.getHeight() * 0.325)), null);
                canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT_ICON), xPos, (int) (yPos + (canvas.getHeight() * 0.425)), null);
            } else {
                canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT_ICON), xPos, (int) (yPos + (canvas.getHeight() * 0.375)), null);
            }

        }
    }

    public void update() {
        if(lifetime < 1000)
            lifetime += 10;

        if(nextActionTimer > 0)
            nextActionTimer--;

        actionCounter++;

        if(showRewards && nextActionTimer == 0) {
            if(distanceDucats > 0) {
                game.activity.playSound(GameActivity.Sound.DUCAT);
                distanceDucats--;
                totalDucats++;

                if(distanceDucats == 0)
                    nextActionTimer = 10;

            } else if(collectedDucats > 0) {
                game.activity.playSound(GameActivity.Sound.DUCAT);
                collectedDucats--;
                totalDucats++;

                if(collectedDucats == 0)
                    nextActionTimer = 10;

            } else if(victoryDucats > 0) {
                game.activity.playSound(GameActivity.Sound.DUCAT);
                victoryDucats--;
                totalDucats++;

                if(victoryDucats == 0)
                    nextActionTimer = 10;

            } else if(multiplierDucats > 0) {
                game.activity.playSound(GameActivity.Sound.DUCAT);
                multiplierDucats--;
                totalDucats++;

            } else if(!doneShowing) {
                doneShowing = true;
            }
        }

        if(doneShowing) {
            game.activity.showAftermathFrame();
        }
    }

    void showRewards() {
        distanceDucats = game.frameCount / 100;
        if(distanceDucats < 0) distanceDucats = 0;
        
        collectedDucats = game.player.ducatCounter;
        victoryDucats = (int)((distanceDucats + collectedDucats) * 0.2);
        if(game.isMultiplierActive()) multiplierDucats = (int)((distanceDucats + collectedDucats + victoryDucats) * 0.5);

        showRewards = true;

        nextActionTimer = 50;
    }
}

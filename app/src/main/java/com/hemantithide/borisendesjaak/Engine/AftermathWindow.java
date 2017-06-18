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

    private String sjaakName;

    private int lifetime;

    private boolean showRewards;
    private boolean doneShowing;

    private int nextActionTimer;
    private int actionCounter;

    private int distanceDucats;
    private int collectedDucats;
    private int multiplayerDucats;
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

        if(sjaakName == null && game.loser != null) {
            if (game.loser.equals(game.player))
                sjaakName = game.player.getUsername();
            else if (game.loser.equals(game.opponent))
                sjaakName = game.activity.opponentName;
        } else {
            sjaakName = game.player.getUsername();
        }

        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 4) - ((paint.descent() + paint.ascent()) / 2));

        paint.setTextSize(0.072f * lifetime);
        canvas.drawText(sjaakName, xPos, yPos - (canvas.getHeight() / 16), paint);

        paint.setTextSize(0.056f * lifetime);
        canvas.drawText("is de Sjaak!", xPos, yPos, paint);
//


        if (showRewards) {
            paint.setTextSize(48);

            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(game.getResources().getString(R.string.game_end_distance) + ":   ", xPos, (int) (yPos + (canvas.getHeight() * 0.2)), paint);
            canvas.drawText(game.getResources().getString(R.string.game_end_collected) + ":   ", xPos, (int) (yPos + (canvas.getHeight() * 0.25)), paint);

            if(GameActivity.IS_MULTIPLAYER) {
                canvas.drawText(game.getResources().getString(R.string.game_end_multiplier) + ":   ", xPos, (int) (yPos + (canvas.getHeight() * 0.3)), paint);
                canvas.drawText(game.getResources().getString(R.string.game_end_total) + ":   ", xPos, (int) (yPos + (canvas.getHeight() * 0.4)), paint);
            } else {
                canvas.drawText(game.getResources().getString(R.string.game_end_total) + ":   ", xPos, (int) (yPos + (canvas.getHeight() * 0.35)), paint);
            }

            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("     +" + distanceDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.2)), paint);
            canvas.drawText("     +" + collectedDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.25)), paint);

            if(GameActivity.IS_MULTIPLAYER) {
                canvas.drawText("     +" + multiplierDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.3)), paint);
                canvas.drawText("     +" + totalDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.4)), paint);
            } else {
                canvas.drawText("     +" + totalDucats, xPos, (int) (yPos + (canvas.getHeight() * 0.35)), paint);
            }

//            xPos -= (canvas.getWidth() / 25);

            canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT_ICON), xPos, (int) (yPos + (canvas.getHeight() * 0.175)), null);
            canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT_ICON), xPos, (int) (yPos + (canvas.getHeight() * 0.225)), null);

            if(GameActivity.IS_MULTIPLAYER) {
                canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT_ICON), xPos, (int) (yPos + (canvas.getHeight() * 0.275)), null);
                canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT_ICON), xPos, (int) (yPos + (canvas.getHeight() * 0.375)), null);
            } else {
                canvas.drawBitmap(SpriteLibrary.bitmaps.get(SpriteLibrary.Sprite.DUCAT_ICON), xPos, (int) (yPos + (canvas.getHeight() * 0.325)), null);
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
//                game.activity.playSound(GameActivity.Sound.DUCAT);
                distanceDucats--;
                totalDucats++;

                if(distanceDucats == 0)
                    nextActionTimer = 10;

            } else if(collectedDucats > 0) {
//                game.activity.playSound(GameActivity.Sound.DUCAT);
                collectedDucats--;
                totalDucats++;

                if(collectedDucats == 0)
                    nextActionTimer = 10;

            } else if(multiplayerDucats > 0) {
//                game.activity.playSound(GameActivity.Sound.DUCAT);
                multiplayerDucats--;
                totalDucats++;

                if(multiplayerDucats == 0)
                    nextActionTimer = 10;

            } else if(multiplierDucats > 0) {
//                game.activity.playSound(GameActivity.Sound.DUCAT);
                multiplierDucats--;
                totalDucats++;

            } else if(!doneShowing) {
                doneShowing = true;
            }
        }

        if(doneShowing && game.activity.activeFrame != GameActivity.ActiveFrame.AFTERMATH) {
            game.activity.showAftermathFrame();
        }
    }

    void showRewards() {
        distanceDucats = game.updateCounter / 100;
        if(distanceDucats < 0) distanceDucats = 0;

        collectedDucats = game.player.ducatCounter;
        if(GameActivity.IS_MULTIPLAYER) multiplayerDucats = (int)((distanceDucats + collectedDucats) * 0.25);
//        if(game.isMultiplierActive()) multiplierDucats = (int)((distanceDucats + collectedDucats + multiplayerDucats) * 0.5);

        showRewards = true;

        nextActionTimer = 50;
    }
}

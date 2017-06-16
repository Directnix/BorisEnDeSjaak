package com.hemantithide.borisendesjaak;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Daniel on 03/06/2017.
 */

public class User implements Serializable {

    String username;
    int age;

    public enum Gender { MALE, FEMALE, OTHER }
    Gender gender;

    int ducats;
    int multipliers;

    // account statistics
    int gamesPlayed;
    int gamesWon;
    int ducatsCollected;
    int applesCollected;
    int kinkersCollected;
    int distanceTravelled;
    int ducatsEarned;

    // account records
    int longestDistance;
    int mostDucats;

    public User(int age, Gender gender) {
        this.age = age;
        this.gender = gender;
    }

    public void setAgeAndGender(int age, Gender gender) {
        this.age = age;
        this.gender = gender;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addToDucats(int amount) {
        ducats += amount;
    }

    public void subtractFromDucats(int amount) {
        ducats -= amount;
    }

    public void consumeMultiplier() {
        multipliers--;
    }

    void save(Context context) {
        String filename = "user_data";
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static User load(Context context) {
        String filename = "user_data";
        FileInputStream fis = null;

        User user = null;

        try {
            fis = context.openFileInput(filename);
            ObjectInputStream is = new ObjectInputStream(fis);
            user = (User)is.readObject();
            is.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return user;
    }
}

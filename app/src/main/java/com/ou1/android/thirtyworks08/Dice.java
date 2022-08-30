package com.ou1.android.thirtyworks08;
import java.util.Random;

public class Dice {
    private final String DICEID;
    private int mCurrentFaceUp = 1;
    private boolean mIsHeld;
    private boolean mIsActive;

    public Dice(String id) {
        DICEID = id;
        mCurrentFaceUp = 1;
        mIsHeld = false;

    }

    public String getDiceId() {
        return DICEID;
    }

    public void reset() {
        mCurrentFaceUp = 1;
        mIsHeld = false;
    }

    public void setCurrentFaceUp(int newFaceUp) {
        mCurrentFaceUp = newFaceUp;
    }

    public int getCurrentFaceUp() {
        return mCurrentFaceUp;
    }

    public void setHeld(boolean held) {
        mIsHeld = held;
    }



    public boolean isHeld() {
        return mIsHeld;
    }

    public void setActive(boolean active) {
        mIsActive = active;
    }

    public boolean isActive() {
        return mIsActive;
    }


    public void roll() {
        Random rng = new Random();
        int randomNumber = rng.nextInt(6) + 1;
        setCurrentFaceUp(randomNumber);

    }
}

package com.ou1.android.thirtyworks08;

import java.util.ArrayList;

public class ThirtyGame {

    private Dice[] mDice;

    private int mScoreChoice = 0;

    private final int MAX_ROUNDS = 10;
    private final int MAX_ROLLS = 3;

    private int mCurrentRoundNo;
    private int mNextRollNo;


    private ArrayList<Integer> roundScores = new ArrayList<>();

    public ThirtyGame() {
        mCurrentRoundNo = 0;
        mNextRollNo = 0;
        launchDice();
    }

    private void launchDice() {
        mDice = new Dice[6];
        String diceIds[] = {"1","2","3","4","5","6"};
        for (int i = 0; i < diceIds.length; i++) {
            addDice(i, diceIds[i]);
        }
    }

    public void addDice(int index, String diceId) {
        mDice[index] = new Dice(diceId);
    }

    public void nextRound() {
        if (mCurrentRoundNo <= MAX_ROUNDS) {

            if (roundScores.size() < mCurrentRoundNo) {
                roundScores.add(getScore());
            }
            resetDice();
            mCurrentRoundNo++;
            mNextRollNo = 1;
            mScoreChoice = 0;
        }
    }

    public void resetDice() {
        for (int i = 0; i < mDice.length; i++) {
            mDice[i].reset();
        }
    }

    public boolean roundOver() {
        return mNextRollNo > MAX_ROLLS;
    }

    public boolean hasScoreChoice() {
        return (mScoreChoice != 0);
    }

    public void setScoreChoice(int choice){
        mScoreChoice = choice;
    }




    public void activateDice(int index, boolean state) {
        mDice[index].setActive(state);
    }

    public void holdDice(int index, boolean state) {
        mDice[index].setHeld(state);
    }

    public Dice getDiceAtIndex(int index) {
        return mDice[index];
    }

    public void rollAllDice() {
        for (int i = 0; i < mDice.length; i++) {
            if (!mDice[i].isHeld()) {
                //rollDice(mDiceViews[i], mDice[i]);
                mDice[i].roll();
                //round over
            }
        }
        mNextRollNo++;

    }
    public int getCurrentRoundNo() {
        return mCurrentRoundNo;
    }




    /*-----------------SCORING-------------  */
    private ArrayList<Dice> getValidDice() {
        ArrayList<Dice> validDice = new ArrayList<>();

        for (int i = 0; i < mDice.length; i++) {
            if (mDice[i].getCurrentFaceUp() < mScoreChoice) {
                validDice.add(mDice[i]);
            }
        }
        return validDice;
    }


    private ArrayList<DiceCombo> getMatchingDice() {

        ArrayList<DiceCombo> diceThatMatchScore = new ArrayList<>();
        for (int i = 0; i < mDice.length; i++) {
            if (mDice[i].getCurrentFaceUp() == mScoreChoice) {
                DiceCombo thisCombo = new DiceCombo(mDice[i]);
                diceThatMatchScore.add(thisCombo);
            }
        }
        return diceThatMatchScore;
    }

    public int getScore() {
        ArrayList<Dice> validDice = getValidDice();
        ArrayList<DiceCombo> validCombos = getMatchingDice();
        if (validDice.size() > 1) {
            for (int i = 0; i < validDice.size(); i++) {
                DiceCombo thisCombo = new DiceCombo(validDice.get(i));
                //     DiceCombo thisCombo = new DiceCombo();
                ArrayList<Dice> comboOptions = new ArrayList(validDice);
                Dice firstIsRemoved = comboOptions.remove(i);
                comboDFS(thisCombo, comboOptions, validCombos);
            }
        }
        return validCombos.size();
    }

    public boolean compareCombos(DiceCombo combo, ArrayList<DiceCombo> comboList)
    {
        int found = 0;
        for (int i = 0; i < comboList.size(); i++ ) {
            if (combo.compareCombo(comboList.get(i))) {
                found++;
            }
        }
        if (found > 0) {return true;}
        return false;
    }

    private void comboDFS(DiceCombo diceCombo, ArrayList<Dice> options,
                          ArrayList<DiceCombo> validCombos) {
        boolean allChecked = false;
        int checked = 0;
        int toCheck = options.size();
        while (!allChecked) {

            for (int i = 0; i < options.size(); i++) {
                DiceCombo newCombo = diceCombo.copy();
                if ((newCombo.getComboSum() + options.get(i).getCurrentFaceUp()) <= mScoreChoice) {
                    newCombo.addOne(options.get(i));
                    if (newCombo.getComboSum() == mScoreChoice) {
                        if (validCombos.size() > 0) {
                            if (!compareCombos(newCombo, validCombos)) {
                                validCombos.add(newCombo);
                            }
                        } else {
                            validCombos.add(newCombo);
                        }
                    } else {
                        ArrayList<Dice> newOptions = new ArrayList(options);
                        Dice firstRemoved = newOptions.remove(i);
                        comboDFS(newCombo, newOptions, validCombos);
                    }
                }
                checked++;
                if (checked == toCheck) {
                    allChecked = true;
                }
            }
        }
    }



}

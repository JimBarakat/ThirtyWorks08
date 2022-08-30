package com.ou1.android.thirtyworks08;
import java.util.ArrayList;

public class DiceCombo {

    private int comboSize = 0;
    private int comboSum = 0;
    private ArrayList<Dice> combo = new ArrayList<>();


    public DiceCombo(Dice first) {
        combo.add(first);
        comboSize = 1;
        comboSum += first.getCurrentFaceUp();
    }

    public int getComboSum() {
        return comboSum;
    }

    public void addOne(Dice next) {
        combo.add(next);
        comboSize++;
        comboSum += next.getCurrentFaceUp();
    }

    public void removeOne(int index) {
        // if (index < 0) {
        //   index = 0;
        //  }

        //if (index )
        comboSum -= combo.get(index).getCurrentFaceUp();
        combo.remove(index);
        comboSize--;

    }

    public Dice getDiceInCombo(int index) {
        return combo.get(index);
    }

    public DiceCombo copy() {
        DiceCombo copy = new DiceCombo(combo.get(0));
        for (int i = 1; i < combo.size(); i++) {
            copy.addOne(combo.get(i));
        }
        return copy;
    }
/*
    public void printCombo() {
        for (int i = 0; i < comboSize; i++) {
            int index = i + 1;
            System.out.print("Dice id at index: " + index + " is ");
            System.out.println(combo.get(i).getDiceId());
        }
    }
*/

    public boolean compareCombo(DiceCombo combo2) {
        if (comboSize == combo2.getSize()) {

            int potentialMatches = comboSize;
            int matches = 0;
            ArrayList<Integer> matchedIndex = new ArrayList<>();
            for (int i = 0; i < comboSize; i++) {
                for (int j = 0; j < combo2.getSize(); j++) {
                    if (combo.get(i).getDiceId().equals(combo2.getDiceInCombo(j).getDiceId())) {
                        if (!matchedIndex.contains(i)) {
                            matches++;
                            matchedIndex.add(i);
                        }
                    }
                }
            }
            if (potentialMatches == matches) {
                return true;
            }
        }
        return false;
    }

    public int getSize() {
        return comboSize;
    }

}
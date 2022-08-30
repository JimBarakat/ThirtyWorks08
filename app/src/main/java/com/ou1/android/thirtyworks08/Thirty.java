package com.ou1.android.thirtyworks08;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class Thirty extends AppCompatActivity {

    private final String SCORE_PROMPT = "Select a score value to start the game";
    private final String HOLD_PROMPT = "Select any dice you want to hold";
    private final String ROLL_PROMPT = "Roll the dice!";
    private final String ROUND_OVER = "Round over!";

    private final String SCORE_SELECT = "SCORE_SELECT";

    private ThirtyGame mThirtyGame;

    private TextView[] mRoundScores;

    private ImageView[] mDiceViews;


    private Spinner mScoreSelector;
    private Button mRollDiceButton;
    private Button mReadyButton;

    private boolean mScoreIsSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirty);
        mThirtyGame = new ThirtyGame();

        initializeDice();

        initRollButton();
        initReadyButton();

        initScoreSelector();
        initRoundScores();

    }

    private void initializeDice() {
        launchDiceViews();
        setDiceViewListeners();
    }

    private void initRoundScores() {
        mRoundScores = new TextView[10];
        mRoundScores[0] = findViewById(R.id.round_1);
        mRoundScores[1] = findViewById(R.id.round_2);
        mRoundScores[2] = findViewById(R.id.round_3);
        mRoundScores[3] = findViewById(R.id.round_4);
        mRoundScores[4] = findViewById(R.id.round_5);
        mRoundScores[5] = findViewById(R.id.round_6);
        mRoundScores[6] = findViewById(R.id.round_7);
        mRoundScores[7] = findViewById(R.id.round_8);
        mRoundScores[8] = findViewById(R.id.round_9);
        mRoundScores[9] = findViewById(R.id.round_10);
    }

    /**
     * Initializes the dice imageviews
     */
    private void launchDiceViews() {
        mDiceViews = new ImageView[6];
        mDiceViews[0] = findViewById(R.id.image_view_dice1);
        mDiceViews[1] = findViewById(R.id.image_view_dice2);
        mDiceViews[2] = findViewById(R.id.image_view_dice3);
        mDiceViews[3] = findViewById(R.id.image_view_dice4);
        mDiceViews[4] = findViewById(R.id.image_view_dice5);
        mDiceViews[5] = findViewById(R.id.image_view_dice6);
    }


    private void setDiceViewListeners() {
        for (int i = 0; i < mDiceViews.length; i++) {
            setHoldListener(mDiceViews[i]);
            mDiceViews[i].setActivated(false);
        }
    }

    private void setHoldListener(ImageView v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holdDice((ImageView) view);
            }
        });
    }

    private void holdDice(ImageView dice) {
        int diceIndex = getIndexFromDiceViewId(dice);
        mThirtyGame.holdDice(diceIndex, true);
        dice.setActivated(false);
        updateDiceView(dice, diceIndex);
    }

    private int getIndexFromDiceViewId(ImageView dice) {
        String diceId = dice.getResources().getResourceName(dice.getId());
        return Integer.parseInt(diceId.substring(diceId.length() - 1)) - 1;
    }


    private void enableHoldListeners() {
        for (int i = 0; i < mDiceViews.length; i++) {
            mDiceViews[i].setEnabled(true);
        }
    }




    private void setDiceRollable() {
        for (int i = 0; i < mDiceViews.length; i++) {
            mDiceViews[i].setImageResource(R.drawable.white1);
            mThirtyGame.activateDice(i, true);
        }
    }


    private void initScoreSelector() {
        mScoreSelector = (Spinner) findViewById(R.id.spinner);
        initSelector();
        initSelectorListener();
        mScoreSelector.setEnabled(true);
    }

    private void initSelector() {
        ArrayAdapter<CharSequence> scrAdptr = ArrayAdapter.createFromResource(this,
                R.array.str2, android.R.layout.simple_list_item_1);
        scrAdptr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mScoreSelector.setAdapter(scrAdptr);
        mScoreSelector.setSelection(0, false);
    }

    private void initSelectorListener() {
        mScoreSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // toast it
//                scoreOptionText(mScoreSelector.getSelectedItem().toString());
                // test that right val is passed
                int pos = position + 3;
                String posWrite = String.valueOf(pos);
                Toast.makeText(getBaseContext(), posWrite, Toast.LENGTH_SHORT).show();
                scoreSelected(position + 3);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void scoreSelected(int scoreVal) {
        mThirtyGame.setScoreChoice(scoreVal);
        setDiceRollable();
        mRollDiceButton.setEnabled(true);
        mScoreSelector.setEnabled(false);
    }

    private void scoreOptionText(String scoreOption) {
        Toast.makeText(getBaseContext(), getScoreText(scoreOption),
                Toast.LENGTH_SHORT).show();
    }

    private String getScoreText(String score) {
        String scoreTag = score.toLowerCase(Locale.ROOT);
        int stringId = getResources().getIdentifier(scoreTag + "_toast",
                "string",
                this.getPackageName());
        return getResources().getString(stringId);
    }

    private void initReadyButton() {
        mReadyButton = (Button) findViewById(R.id.button2);
        mReadyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRollDiceButton.setEnabled(true);
                mReadyButton.setEnabled(false);
            }
        });
        mReadyButton.setEnabled(false);
    }

    private void initRollButton() {
        mRollDiceButton = (Button) findViewById(R.id.button);
        mRollDiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mThirtyGame.hasScoreChoice()) {
                    if (!(mThirtyGame.roundOver())) {
                        Toast.makeText(getBaseContext(), ROLL_PROMPT, Toast.LENGTH_SHORT).show();
                        rollDice();
                    } else {
                        Toast.makeText(getBaseContext(), SCORE_PROMPT, Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        mRollDiceButton.setEnabled(false);
    }

    private void rollDice() {

        mThirtyGame.rollAllDice();
        for (int i = 0; i < mDiceViews.length; i++) {
            updateDiceView(mDiceViews[i], i);
        }

        if (mThirtyGame.roundOver()) {
            Toast.makeText(getBaseContext(), ROUND_OVER, Toast.LENGTH_SHORT).show();
            String score = String.valueOf(mThirtyGame.getScore());
            String scoreMesg = "Score for round: " + mThirtyGame.getCurrentRoundNo() + " is " + score;
            Toast.makeText(getBaseContext(), scoreMesg, Toast.LENGTH_LONG).show();

        }
        else {
            mRollDiceButton.setEnabled(false);
            //activateDiceViews();
            mReadyButton.setEnabled(true);
        }
    }



    @SuppressLint("ResourceAsColor")
    private void addScoreToScoreBoard(int boardIndex, int score) {
        String scoreThisRound = String.valueOf(score);
        mRoundScores[boardIndex].setText(scoreThisRound);
        mRoundScores[boardIndex].setTextColor(android.R.color.holo_blue_light);
    }


    private void updateDiceView(ImageView diceView, int i) {
        switch (mThirtyGame.getDiceAtIndex(i).getCurrentFaceUp()) {
            case 1:
                if (mThirtyGame.getDiceAtIndex(i).isHeld()) {
                    diceView.setImageResource(R.drawable.red1);
                } else {
                    diceView.setImageResource(R.drawable.white1);
                }
                break;
            case 2:
                if (mThirtyGame.getDiceAtIndex(i).isHeld()) {
                    diceView.setImageResource(R.drawable.red2);
                } else {
                    diceView.setImageResource(R.drawable.white2);
                }
                break;
            case 3:
                if (mThirtyGame.getDiceAtIndex(i).isHeld()) {
                    diceView.setImageResource(R.drawable.red3);
                } else {
                    diceView.setImageResource(R.drawable.white3);
                }
                break;
            case 4:
                if (mThirtyGame.getDiceAtIndex(i).isHeld()) {
                    diceView.setImageResource(R.drawable.red4);
                } else {
                    diceView.setImageResource(R.drawable.white4);
                }
                break;
            case 5:
                if (mThirtyGame.getDiceAtIndex(i).isHeld()) {
                    diceView.setImageResource(R.drawable.red5);
                } else {
                    diceView.setImageResource(R.drawable.white5);
                }
                break;
            case 6:
                if (mThirtyGame.getDiceAtIndex(i).isHeld()) {
                    diceView.setImageResource(R.drawable.red6);
                } else {
                    diceView.setImageResource(R.drawable.white6);
                }
                break;
        }
    }


}
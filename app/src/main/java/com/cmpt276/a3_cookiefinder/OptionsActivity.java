package com.cmpt276.a3_cookiefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.a3_cookiefinder.game_activity.GameActivity;
import com.cmpt276.a3_cookiefinder.model.model.controller.Options;

public class OptionsActivity extends AppCompatActivity {
    private int selectedColSize;
    private int selectedRowSize;
    private int selectedMaxScore;

    private  SharedPreferences highScoreSharedPref;
    private  String sharedPrefScoreKey;
    private String sharedPrefBestTurnKey;
    private String sharedPrefTotalTurnkey;
    public final String sharedPrefSavedRowKey = SharedPrefKey.SHARED_PREF_SAVED_ROW_KEY;
    public final String sharedPrefSavedColKey = SharedPrefKey.SHARED_PREF_SAVED_COL_KEY;
    public final String sharedPrefSavedScoreKey = SharedPrefKey.SHARED_PREF_SAVED_SCORE_KEY;


    private Options options;


    public static Intent makeLaunchIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        highScoreSharedPref = this.getSharedPreferences(SharedPrefKey.SHARED_PREF_HIGH_SCORE_TAG, MODE_PRIVATE);
        options = Options.getInstance();

        makeBoardSizeOptions();
        makeCookieNumOptions();

        updateStatBoard();
    }

    private void updateSharedPrefKeys() {
        sharedPrefBestTurnKey = SharedPrefKey.makeSharedPrefTurnKey(selectedRowSize, selectedColSize, selectedMaxScore);
        sharedPrefScoreKey = SharedPrefKey.makeSharedPrefScorekey(selectedRowSize, selectedColSize, selectedMaxScore);
        sharedPrefTotalTurnkey = SharedPrefKey.makeSharedPrefTotalTurnKey(selectedRowSize,selectedColSize, selectedMaxScore);
    }

    private void setupResetButton() {
        Button resetButton = findViewById(R.id.buttonReset);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSharedPref();
                updateStatBoard();
                Toast.makeText(OptionsActivity.this, "High Score Reset!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearSharedPref() {
        SharedPreferences.Editor editor = highScoreSharedPref.edit();
        editor.remove(sharedPrefScoreKey);
        editor.apply();
        editor.remove(sharedPrefBestTurnKey);
        editor.apply();
        editor.remove(sharedPrefTotalTurnkey);
        editor.apply();
    }

    private void updateStatBoard() {
        updateSharedPrefKeys();

        String boardConfigString = selectedRowSize + "x" + selectedColSize;
        int bestTurn = highScoreSharedPref.getInt(sharedPrefBestTurnKey, (selectedColSize*selectedRowSize)+selectedMaxScore);
        int totalTurnForConfig = highScoreSharedPref.getInt(sharedPrefTotalTurnkey, 0);

        TextView textView = findViewById(R.id.textViewConfigStat);
        textView.setText(boardConfigString);

        textView = findViewById(R.id.textViewMaxScore);
        textView.setText(""+selectedMaxScore);

        textView = findViewById(R.id.textViewStatNumGame);
        textView.setText(""+totalTurnForConfig);

        textView = findViewById(R.id.textViewStatBestTurn);
        textView.setText(""+bestTurn);

        setupResetButton();
    }

    private void makeCookieNumOptions() {
        final RadioGroup radioGroup = findViewById(R.id.radioGroupCookiesNum);
        int[] scoreNums = getResources().getIntArray(R.array.cookie_num);
        ColorStateList white = ContextCompat.getColorStateList(this, R.color.utg_white_text);

        boolean setAsDefault = false;
        for (final int scoreNum : scoreNums) {
            final RadioButton radioButton = new RadioButton(this);
            radioButton.setText(""+scoreNum);
            radioButton.setTextColor(white);
            radioButton.setButtonTintList(white);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OptionsActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                    options.setSelectedScoreNum(scoreNum);
                    //saveCookieNum(scoreNum);
                    selectedMaxScore = scoreNum;
                    updateStatBoard();
                    saveMaxScore(scoreNum);
                }
            });

            radioGroup.addView(radioButton);

            if (scoreNum == highScoreSharedPref.getInt(sharedPrefSavedScoreKey,options.getSelectedScoreNum())) {
                radioButton.setChecked(true);
                this.selectedRowSize = options.getSelectedRowNum();
                this.selectedColSize = options.getSelectedColNum();
                this.selectedMaxScore = options.getSelectedScoreNum();
                setAsDefault = true;
            }
        }
    }

    private void saveMaxScore(int score) {
        SharedPreferences.Editor editor = highScoreSharedPref.edit();
        editor.putInt(sharedPrefSavedScoreKey, score).apply();
    }

    private void makeBoardSizeOptions() {
        final RadioGroup radioGroup = findViewById(R.id.radioGroupBoardSize);
        int[] colSizes = getResources().getIntArray(R.array.colSizes);
        int[] rowSizes = getResources().getIntArray(R.array.rowSizes);
        ColorStateList white = ContextCompat.getColorStateList(this, R.color.utg_white_text);

        if (colSizes.length != rowSizes.length) {
            Toast.makeText(OptionsActivity.this, "Resource data error.", Toast.LENGTH_LONG).show();
            return;
        }

        boolean setAsDefault = false;
        for (int i = 0; i < rowSizes.length; i++) {
            final int row = rowSizes[i];
            final int col = colSizes[i];
            RadioButton radioButton = new RadioButton(this);

            radioButton.setText("" + row + "x" + col);
            radioButton.setTextColor(white);
            radioButton.setButtonTintList(white);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OptionsActivity.this, "Board size saved!", Toast.LENGTH_SHORT).show();
                    //saveBoardSize(row, col);

                    selectedColSize = col;
                    selectedRowSize = row;

                    options.setSelectedRowNum(row);
                    options.setSelectedColNum(col);
                    updateStatBoard();
                    saveSize(row,col);
                }
            });

            radioGroup.addView(radioButton);

            if (row == highScoreSharedPref.getInt(sharedPrefSavedRowKey, options.getSelectedRowNum())
                    && col == highScoreSharedPref.getInt(sharedPrefSavedColKey, options.getSelectedColNum())) {
                radioButton.setChecked(true);
                setAsDefault = true;
            }

        }
    }

    private void saveSize(int row, int col) {
        SharedPreferences.Editor editor = highScoreSharedPref.edit();
        editor.putInt(sharedPrefSavedRowKey, row).apply();
        editor.putInt(sharedPrefSavedColKey, col).apply();
    }




}

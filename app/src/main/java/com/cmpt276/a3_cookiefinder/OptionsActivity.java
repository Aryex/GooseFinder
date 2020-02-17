package com.cmpt276.a3_cookiefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    public static final String COOKIE_NUM_SHARED_PREF_TAG = "cookie num";
    public static final String ROW_SHARED_PREF_TAG = "row";
    public static final String COL_SHARE_PREF_TAG = "col";

    private int selectedColSize;
    private int selectedRowSize;
    private int selectedMaxScore;

    private static SharedPreferences highScoreSharedPref;
    private SharedPreferences.Editor editor;
    private  String sharedPrefScoreKey;
    private String sharedPrefBestTurnKey;
    private String sharedPrefTotalTurnkey;
    private Options options;


    private static SharedPreferences highScoreSharedPrefs;
    private static SharedPreferences.Editor highScoreEditor;

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }

    public static int getScoreConfig(Context context) {
        highScoreSharedPrefs = context.getSharedPreferences("HighScore", MODE_PRIVATE);
        Options options = Options.getInstance();
        String key = options.getSelectedRowNum() + "x" + options.getSelectedColNum() + "score";
        return highScoreSharedPref.getInt(key, options.getSelectedScoreNum());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        highScoreSharedPref = this.getSharedPreferences(GameActivity.SHARED_PREF_HIGH_SCORE_TAG, MODE_PRIVATE);
        options = Options.getInstance();

        makeBoardSizeOptions();
        makeCookieNumOptions();
        updateSharedPrefKeys();
        updateStatBoard();

        //Toast.makeText(this, "TurnScore: " + highScoreSharedPref.getInt(selectedRowSize + "x" + selectedColSize + "turn", -1), Toast.LENGTH_SHORT).show();
    }

    private void updateSharedPrefKeys() {
        sharedPrefBestTurnKey = makeSharedPrefTurnKey(selectedRowSize, selectedColSize);
        sharedPrefScoreKey = makeSharedPrefScorekey(selectedRowSize, selectedColSize);
        sharedPrefTotalTurnkey = makeSharedPrefTotalTurnKey(selectedRowSize,selectedColSize);
    }

    private void setupResetButton() {
        Button resetButton = findViewById(R.id.buttonReset);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = highScoreSharedPref.edit();
                editor.remove(sharedPrefScoreKey);
                editor.apply();
                editor.remove(sharedPrefBestTurnKey);
                editor.apply();
                editor.remove(sharedPrefTotalTurnkey);
                editor.apply();

                updateStatBoard();
                Toast.makeText(OptionsActivity.this, "High Score Reset!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatBoard() {
        updateSharedPrefKeys();

        String boardConfig = selectedRowSize + "x" + selectedColSize + " with " + selectedMaxScore + "total cookies";
        int bestTurn = highScoreSharedPref.getInt(sharedPrefBestTurnKey, (selectedColSize*selectedRowSize)+selectedMaxScore);
        int totalTurnForConfig = highScoreSharedPref.getInt(sharedPrefTotalTurnkey, 0);

        TextView textView = findViewById(R.id.textViewConfigStat);
        textView.setText(boardConfig);

        textView = findViewById(R.id.textViewStatNumGame);
        textView.setText(""+totalTurnForConfig);

        textView = findViewById(R.id.textViewStatBestTurn);
        textView.setText(""+bestTurn);

        setupResetButton();
    }

    private String makeSharedPrefTotalTurnKey(int selectedRowSize, int selectedColSize) {
        return selectedRowSize + "x" + selectedColSize + "total_turn";
    }

    private String makeSharedPrefScorekey(int selectedRowSize, int selectedColSize) {
        return selectedRowSize + "x" + selectedColSize + "score";
    }

    private String makeSharedPrefTurnKey(int selectedRowSize, int selectedColSize) {
        return selectedRowSize + "x" + selectedColSize + "turn";
    }

    private void makeCookieNumOptions() {
        final RadioGroup radioGroup = findViewById(R.id.radioGroupCookiesNum);
        int[] cookieNums = getResources().getIntArray(R.array.cookie_num);

        boolean setAsDefault = false;
        for (final int cookieNum : cookieNums) {
            final RadioButton radioButton = new RadioButton(this);
            radioButton.setText(cookieNum + " cookies.");

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OptionsActivity.this, "Cookie amount saved!", Toast.LENGTH_SHORT).show();
                    options.setSelectedScoreNum(cookieNum);
                    //saveCookieNum(cookieNum);
                    selectedMaxScore = cookieNum;
                    updateStatBoard();
                }
            });

            radioGroup.addView(radioButton);

            if (cookieNum == options.getSelectedScoreNum()) {
                radioButton.setChecked(true);
                selectedRowSize = options.getSelectedRowNum();
                selectedColSize = options.getSelectedColNum();
                selectedMaxScore = options.getSelectedScoreNum();
                setAsDefault = true;
            }
        }
    }

    private void makeBoardSizeOptions() {
        final RadioGroup radioGroup = findViewById(R.id.radioGroupBoardSize);
        int[] colSizes = getResources().getIntArray(R.array.colSizes);
        int[] rowSizes = getResources().getIntArray(R.array.rowSizes);

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
                }
            });

            radioGroup.addView(radioButton);

            if (row == options.getSelectedRowNum() && col == options.getSelectedColNum()) {
                radioButton.setChecked(true);
                setAsDefault = true;
            }

        }
    }

    private void saveBoardSize(int row, int col) {
        editor.putInt(ROW_SHARED_PREF_TAG, row);
        editor.putInt(COL_SHARE_PREF_TAG, col);
        editor.apply();
    }

    private void saveCookieNum(int cookieNum) {
        editor.putInt(COOKIE_NUM_SHARED_PREF_TAG, cookieNum);
        editor.apply();
    }


}

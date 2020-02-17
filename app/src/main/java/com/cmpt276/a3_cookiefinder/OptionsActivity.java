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

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Options options;

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        sharedPreferences = this.getSharedPreferences("BoardPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        options = Options.getInstance();

        makeBoardSizeOptions();
        makeCookieNumOptions();
        setupResetButton();
    }

    private void setupResetButton() {
        Button resetButton = findViewById(R.id.buttonReset);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String sharedPrefTurnKey = makeSharedPrefTurnKey(selectedRowSize, selectedColSize);
                String sharedPrefSoreKey = makeSharedPrefScorekey(selectedRowSize, selectedColSize);
                editor.remove(sharedPrefSoreKey).apply();
                editor.remove(sharedPrefTurnKey).apply();
                Toast.makeText(OptionsActivity.this, "High Score Reset!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String makeSharedPrefScorekey(int selectedRowSize, int selectedColSize) {
        return selectedRowSize + "x" + selectedColSize + "score";
    }

    private String makeSharedPrefTurnKey(int selectedRowSize, int selectedColSize) {
        return selectedRowSize + "x" + selectedColSize + "turn";
    }

    private void loadBestScore() {

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
                    saveCookieNum(cookieNum);
                    selectedMaxScore = cookieNum;
                }
            });

            radioGroup.addView(radioButton);

            if (cookieNum == options.getSelectedScoreNum()) {
                radioButton.setChecked(true);
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
                    saveBoardSize(row, col);

                    selectedColSize = col;
                    selectedRowSize = row;

                    options.setSelectedRowNum(row);
                    options.setSelectedColNum(col);
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

    public static int getBoardRow(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BoardPref", MODE_PRIVATE);
        return sharedPreferences.getInt("row", 0);
    }

    public static int getBoardCol(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BoardPref", MODE_PRIVATE);
        return sharedPreferences.getInt("col", 0);
    }


}

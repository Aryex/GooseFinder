package com.cmpt276.a3_cookiefinder.game_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.a3_cookiefinder.R;
import com.cmpt276.a3_cookiefinder.SharedPrefKey;
import com.cmpt276.a3_cookiefinder.model.model.controller.GameController;
import com.cmpt276.a3_cookiefinder.model.model.game_obj.Point;

import java.util.ArrayList;
import java.util.Random;

import static com.cmpt276.a3_cookiefinder.SharedPrefKey.SHARED_PREF_HIGH_SCORE_TAG;

public class GameActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String sharedPrefScoreKey;
    private String sharedPrefTurnKey;
    private String sharedPrefTotalTurnkey;
    private int bestTurnAchieved;
    private int scoreConfig;

    private int maxCol = 3;
    private int maxRow = 2;
    private int maxScore;
    private int currentGameTurnCount;
    private Button[][] buttons;

    private GameController gameController;

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameController = new GameController();
        maxRow = gameController.getMAX_ROW();
        maxCol = gameController.getMAX_COL();
        maxScore = gameController.getMaxScore();
        buttons = new Button[maxRow][maxCol];

        this.sharedPreferences = this.getSharedPreferences(SHARED_PREF_HIGH_SCORE_TAG, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        makeSharedPrefKeys();

        updateTotalTurnForCurrentConfig();
        currentGameTurnCount = 0;

        updateDimensionText();
        updateHighScore();
        updateTrackerTexts();
        setupGameBoard();
    }

    private void updateDimensionText() {
        TextView textView = findViewById(R.id.textViewDimension);
        textView.setText("Dimension: " + maxRow + "x" + maxCol);
    }


    private void setupGameBoard() {
        final TableLayout gameTable = findViewById(R.id.gameTableLayout);

        for (int row = 0; row < maxRow; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            gameTable.addView(tableRow);

            for (int col = 0; col < maxCol; col++) {
                final Button button = new Button(this);
                button.setSoundEffectsEnabled(false);
                setUpButtonStyle(button);

                tableRow.addView(button);
                buttons[row][col] = button;

                button.setOnClickListener(new onButtonClick(row, col, button));
            }
        }
    }

    private void setUpButtonStyle(Button button) {
        button.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT,
                1.0f));

        button.setPadding(0, 0, 0, 0);
        button.setTextColor(getColor(R.color.utg_white_text));

        button.setTextSize(25);
    }

    private class onButtonClick implements View.OnClickListener {

        private int row;
        private int col;
        private Button button;

        public onButtonClick(int row, int col, Button button) {
            this.row = row;
            this.col = col;
            this.button = button;
        }

        @Override
        public void onClick(View v) {
            currentGameTurnCount++;
            lockButtonSize();
            Point buttonPoint = new Point(row, col);
            int answer = gameController.select(buttonPoint);

            if (answer == -1) {
                turnOnImage(button);
                startBounceAnimation(button);
                playFoundSound();
            } else if (gameController.hasCookieAt(buttonPoint)) {
                buttons[row][col].setText("" + gameController.scanCookieHint(new Point(row, col)));
                startScanAnimation(row, col);
                playScanSound();
            } else if (!gameController.hasCookieAt(buttonPoint)) {
                button.setText("" + answer);
                startScanAnimation(row, col);
                playScanSound();
            }

            updateHintAroundPoint2(buttonPoint);
            updateTrackerTexts();
            checkWinCondition();
        }

    }

    private void playScanSound() {
        final MediaPlayer scanSound;
        Log.i("playScanSound: ", "");

        Random rand = new Random();
        int num = rand.nextInt(6);
        switch (num) {
            case 0:
                scanSound = MediaPlayer.create(this, R.raw.scan3);
                break;
            case 1:
                scanSound = MediaPlayer.create(this, R.raw.scan4);
                break;
            case 2:
                scanSound = MediaPlayer.create(this, R.raw.scan7);
                break;
            case 3:
                scanSound = MediaPlayer.create(this, R.raw.scan5);
                break;
            case 4:
                scanSound = MediaPlayer.create(this, R.raw.scan6);
                break;
            default:
                scanSound = MediaPlayer.create(this, R.raw.scan);
        }
        scanSound.start();
        scanSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                scanSound.release();
            }
        });
    }

    private void playFoundSound() {
        final MediaPlayer foundSound;

        Random rand = new Random();
        int num = rand.nextInt(5);
        switch (num) {
            case 0:
                foundSound = MediaPlayer.create(this, R.raw.found1);
                break;
            case 1:
                foundSound = MediaPlayer.create(this, R.raw.found6);
                break;
            case 2:
                foundSound = MediaPlayer.create(this, R.raw.found3);
                break;
            case 3:
                foundSound = MediaPlayer.create(this, R.raw.found5);
                break;
            default:
                foundSound = MediaPlayer.create(this, R.raw.found7);
        }
        foundSound.start();
        foundSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                foundSound.release();
            }
        });
    }

    private void startBounceAnimation(Button button) {
        Animation rumble = AnimationUtils.loadAnimation(GameActivity.this, R.anim.bounce);
        rumble.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                disableAllButton();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                enableAllButtons();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        button.startAnimation(rumble);

    }

    private void enableAllButtons() {
        for(Button[] row : buttons){
            for(Button button : row){
                button.setEnabled(true);
            }
        }
    }

    private void disableAllButton() {
        for(Button[] row : buttons){
            for(Button button : row){
                button.setEnabled(false);
            }
        }
    }

    private void startScanAnimation(int row, int col) {
        for (int rowNum = 0; rowNum < maxRow; rowNum++) {
            final Button button = buttons[rowNum][col];
            Animation fadeOut = AnimationUtils.loadAnimation(GameActivity.this, R.anim.fadeout);

            fadeOut.setAnimationListener(new FadeIn(button));
            button.startAnimation(fadeOut);
        }
        for (int colNum = 0; colNum < maxCol; colNum++) {
            final Button button = buttons[row][colNum];
            Animation fadeOut = AnimationUtils.loadAnimation(GameActivity.this, R.anim.fadeout);

            fadeOut.setAnimationListener(new FadeIn(button));
            button.startAnimation(fadeOut);
        }
    }

    private class FadeIn implements Animation.AnimationListener {

        private Button button;

        public FadeIn(Button button) {
            this.button = button;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Animation fadeIn = AnimationUtils.loadAnimation(GameActivity.this, R.anim.fadein);
            button.startAnimation(fadeIn);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    private void checkWinCondition() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AlertFragment dialog = new AlertFragment();

        if (gameController.won()) {
            playGooseSound();
            revealAll();
            dialog.show(fragmentManager, "Alert Dialog");
            if (currentGameTurnCount < bestTurnAchieved) {
                editor.remove(sharedPrefScoreKey);
                editor.apply();
                editor.putInt(sharedPrefScoreKey, gameController.getMaxScore());
                editor.apply();

                editor.remove(sharedPrefTurnKey);
                editor.apply();
                editor.putInt(sharedPrefTurnKey, currentGameTurnCount);
                editor.apply();
            }
        }
    }

    private void revealAll() {
        int row = 0;
        for (Button[] buttonsRow : buttons) {
            int col = 0;
            for (Button button : buttonsRow) {
                button.setText("" + gameController.scanCookieHint(new Point(row, col)));
                col++;
            }
            row++;
        }
    }

    private void playGooseSound() {
        MediaPlayer goose;

        Random rand = new Random();
        int num = rand.nextInt(3);
        switch (num) {
            case 0:
                goose = MediaPlayer.create(this, R.raw.goose_honk);
                break;
            case 1:
                goose = MediaPlayer.create(this, R.raw.goose_honk2);
                break;
            default:
                goose = MediaPlayer.create(this, R.raw.goose_honk3);
        }
        goose.start();
    }

    private void makeSharedPrefKeys() {
        sharedPrefScoreKey = SharedPrefKey.makeSharedPrefScorekey(maxRow, maxCol, maxScore);
        sharedPrefTurnKey = SharedPrefKey.makeSharedPrefTurnKey(maxRow, maxCol, maxScore);
        sharedPrefTotalTurnkey = SharedPrefKey.makeSharedPrefTotalTurnKey(maxRow, maxCol, maxScore);
    }

    private void updateTotalTurnForCurrentConfig() {
        int totalTurn = this.sharedPreferences.getInt(sharedPrefTotalTurnkey, 0);
        totalTurn++;
        editor.putInt(sharedPrefTotalTurnkey, totalTurn).apply();
    }

    private void updateTrackerTexts() {
        TextView cookiesScore = findViewById(R.id.textViewCookieScore);
        TextView turnCount = findViewById(R.id.textViewTurnsNumber);

        cookiesScore.setText(gameController.getCountedCookies() + " / " + gameController.getMaxScore());
        turnCount.setText(String.valueOf(gameController.getTurnCounts()));
    }

    private void updateHighScore() {
        TextView textViewHighScore = findViewById(R.id.textViewHighScore);

        scoreConfig = sharedPreferences.getInt(sharedPrefScoreKey, gameController.getMaxScore());
        bestTurnAchieved = sharedPreferences.getInt(sharedPrefTurnKey, (maxRow * maxCol + maxScore));

        textViewHighScore.setText("Took " + bestTurnAchieved + " turns for " + scoreConfig + " score.");
    }

    private void updateHintAroundPoint2(Point startingPoint) {
        ArrayList<Point> pointsToBeUpdated = gameController.getPointsToUpdate(startingPoint);

        for (Point point : pointsToBeUpdated) {
            int row = point.getX();
            int col = point.getY();

            buttons[row][col].setText("" + gameController.scanCookieHint(new Point(row, col)));
        }
    }

    private void turnOnImage(Button button) {
        int newWidth = button.getWidth();
        int newHeight = button.getHeight();

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.goose);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);

        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));
    }

    private void lockButtonSize() {

        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {

                Button button = buttons[row][col];
                int width = button.getWidth();
                int height = button.getHeight();

                button.setMinWidth(width);
                button.setMaxWidth(width);

                button.setMinHeight(height);
                button.setMaxHeight(height);

            }
        }
    }

}

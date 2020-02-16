package com.cmpt276.a3_cookiefinder.game_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cmpt276.a3_cookiefinder.R;
import com.cmpt276.a3_cookiefinder.model.model.controller.GameController;
import com.cmpt276.a3_cookiefinder.model.model.game_obj.Point;

public class GameActivity extends AppCompatActivity {

    private static int MAX_COL = 3;
    private static int MAX_ROW = 2;
    private static int turnCount = 0;
    private Button[][] buttons;

    private GameController gameController;

    final static int IMMERSIVE = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
            | View.SYSTEM_UI_FLAG_IMMERSIVE;

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final View gameView = getWindow().getDecorView();
        //gameView.setSystemUiVisibility(IMMERSIVE);


        gameController = new GameController();
        MAX_ROW = gameController.getMAX_ROW();
        MAX_COL = gameController.getMAX_COL();
        buttons = new Button[MAX_ROW][MAX_COL];

        updateTrackerText();
        generateGameBoard();
        //generateBoardLayout();

    }

    private void generateGameBoard() {
        final TableLayout gameTable = findViewById(R.id.gameTableLayout);
        for (int row = 0; row < MAX_ROW; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            gameTable.addView(tableRow);

            for (int col = 0; col < MAX_COL; col++) {
                final Button button = new Button(this);

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
            lockButtonSize();
            Point buttonPoint = new Point(row, col);
            int answer = gameController.select(buttonPoint);

            if (answer == -1) {
                turnOnImage(button);
                startBounceAnimation(button);
            } else if (gameController.hasCookieAt(buttonPoint)) {
                buttons[row][col].setText("" + gameController.scanCookieHint(new Point(row, col)));
                startScanAnimation(row, col);
            } else if (!gameController.hasCookieAt(buttonPoint)) {
                button.setText("" + answer);
                startScanAnimation(row, col);
            }
            updateAroundPoint(buttonPoint);
            updateTrackerText();
            checkWinCondition();
        }


    }
    private void startBounceAnimation(Button button) {
        Animation rumble = AnimationUtils.loadAnimation(GameActivity.this, R.anim.bounce);
        button.startAnimation(rumble);
    }

    private void startScanAnimation(int row, int col) {
       for(int rowNum = 0; rowNum < MAX_ROW; rowNum ++){
           final Button button = buttons[rowNum][col];
           Animation fadeOut = AnimationUtils.loadAnimation(GameActivity.this, R.anim.fadeout);

           fadeOut.setAnimationListener(new FadeIn(button));
           button.startAnimation(fadeOut);
       }
        for(int colNum = 0; colNum < MAX_COL; colNum ++){
            final Button button = buttons[row][colNum];
            Animation fadeOut = AnimationUtils.loadAnimation(GameActivity.this, R.anim.fadeout);

            fadeOut.setAnimationListener(new FadeIn(button));
            button.startAnimation(fadeOut);
        }
    }

    private class FadeIn implements Animation.AnimationListener{
        private Button button;

        public FadeIn(Button button){
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
            dialog.show(fragmentManager, "Alert Dialog");
        }
    }

    private void updateTrackerText() {
        TextView cookiesScore = findViewById(R.id.textViewCookieScore);
        TextView turnCount = findViewById(R.id.textViewTurnsNumber);

        cookiesScore.setText(gameController.getCountedCookies() + " / " + gameController.getMaxScore());
        turnCount.setText(String.valueOf(gameController.getTurnCounts()));
        //lockButtonSize();
    }

    private void updateAroundPoint(Point startingPoint) {
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                Point point = new Point(row, col);
                //if its not the point that im at, update it.
                if (!point.equals(startingPoint) && gameController.hasVisited(point)) {
                    if (!gameController.hasCookieAt(point)) {
                        buttons[row][col].setText("" + gameController.scanCookieHint(new Point(row, col)));
                    } else {
                        if (gameController.hasHintedCookie(point)) {
                            buttons[row][col].setText(" " + gameController.scanCookieHint(new Point(row, col)));
                        }

                    }
                }
            }
        }
    }

    private void turnOnImage(Button button) {
        int newWidth = button.getWidth();
        int newHeight = button.getHeight();
        Log.i("TurnOnImage(): ", " ");
        Log.i("TurnOnImage(): ", "Button Size Max " + button.getMaxHeight() + " : " + button.getMaxWidth());
        Log.i("TurnOnImage(): ", "Button Size min " + newHeight + " : " + newWidth);

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_cookie);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        //scaledBitmap.setHeight(scaledBitmap.getHeight()+10);

        Log.i("TurnOnImage(): ", " scaleBitMap Size  " + scaledBitmap.getHeight() + " : " + scaledBitmap.getWidth());

        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));
    }

    private void lockButtonSize() {
        Log.i("LockButtonSize(): ", " ");
//        Button buttonFinal = buttons[0][0];
//        int width = buttonFinal.getWidth();
//        int height = buttonFinal.getHeight();
//        Log.i("LOCK: "," BUTTON 0:0  "+height+" : "+width);

        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {

                Button button = buttons[row][col];
                int width = button.getWidth();
                int height = button.getHeight();
//                Log.i("LOCK: "," BUTTON[i][j]  "+height+" : "+width);
                button.setMinWidth(width);
                button.setMaxWidth(width);

                button.setMinHeight(height);
                button.setMaxHeight(height);
                Log.i("LockButtonSize(): ", " LOCK_SIZE  " + height + " : " + width);
            }
        }
    }

}

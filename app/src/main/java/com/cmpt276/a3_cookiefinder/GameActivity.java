package com.cmpt276.a3_cookiefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cmpt276.a3_cookiefinder.controller.GameController;
import com.cmpt276.a3_cookiefinder.model.game_obj.CookieMap;
import com.cmpt276.a3_cookiefinder.model.game_obj.Point;

import org.w3c.dom.Text;

public class GameActivity extends AppCompatActivity {

    private static final int MAX_COL = 3;
    private static final int MAX_ROW = 2;
    private static int turnCount = 0;
    Button buttons[][] = new Button[MAX_ROW][MAX_COL];

    private GameController gameController;

    final static int IMMERSIVE = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
            | View.SYSTEM_UI_FLAG_IMMERSIVE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final View gameView = getWindow().getDecorView();
        //gameView.setSystemUiVisibility(IMMERSIVE);


        gameController = new GameController(MAX_ROW, MAX_COL, 3);

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

                tableRow.addView(button);
                buttons[row][col] = button;
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));
                //set button's content padding
                button.setPadding(0, 0, 0, 0);

                //hook button to controller
                button.setOnClickListener(new onButtonClick(row, col, button));
            }
        }
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
            Point buttonPoint = new Point(row, col);
            int answer = gameController.select(buttonPoint);

            if (answer == -1) {
                //button.setText("Cookie!");
                turnOnImage(button);

            } else if (gameController.hasCookieAt(buttonPoint)) {
                buttons[row][col].setText(" Hint: " + gameController.scanCookieHint(new Point(row, col)));

            } else if (!gameController.hasCookieAt(buttonPoint)) {
                button.setText(" Hint: " + answer);

            }
            updateAroundPoint(buttonPoint);
            updateTrackerText();
        }
    }

    private void updateTrackerText() {
        TextView cookiesScore = findViewById(R.id.textViewCookieScore);
        TextView turnCount = findViewById(R.id.textViewTurnsNumber);

        cookiesScore.setText(gameController.getCountedCookies() + " / " + gameController.getTotalCookies());
        turnCount.setText(String.valueOf(gameController.getTurnCounts()));
        lockButtonSize();
    }

    private void updateAroundPoint(Point startingPoint) {
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                Point point = new Point(row, col);
                //if its not the point that im at, update it.
                if (!point.equals(startingPoint) && gameController.hasVisited(point)) {
                    if (!gameController.hasCookieAt(point)) {
                        buttons[row][col].setText(" Hint: " + gameController.scanCookieHint(new Point(row, col)));
                    } else {
                        if (gameController.hasHintedCookie(point)) {
                            buttons[row][col].setText("  Hint: " + gameController.scanCookieHint(new Point(row, col)));
                        }

                    }
                }
            }
        }
    }

    private void turnOnImage(Button button) {
        int newWidth = button.getWidth();
        int newHeight = button.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_cookie);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));
    }

    private void lockButtonSize() {
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                Button button = buttons[row][col];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }

}

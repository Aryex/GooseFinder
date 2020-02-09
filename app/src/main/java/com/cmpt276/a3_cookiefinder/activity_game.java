package com.cmpt276.a3_cookiefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.cmpt276.a3_cookiefinder.controller.GameController;
import com.cmpt276.a3_cookiefinder.model.game_obj.CookieMap;
import com.cmpt276.a3_cookiefinder.model.game_obj.Point;

public class activity_game extends AppCompatActivity {

    private static final int MAX_COL = 3;
    private static final int MAX_ROW = 2;
    private static int turnCount = 0;
    Button buttons[][] = new Button[MAX_ROW][MAX_COL];

    private GameController gameController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        CookieMap cookieMap = new CookieMap(MAX_ROW, MAX_COL, 3);
        gameController = new GameController(cookieMap);

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
                final int rowf = row;
                final int colf = col;
                final Button button = new Button(this);
                tableRow.addView(button);
                buttons[row][col] = button;
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

                //set button's content padding
                button.setPadding(0, 0, 0, 0);
                //hook button to controller
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Point buttonPoint = new Point(rowf, colf);
                        int answer = gameController.select(buttonPoint);
                        //lockButtonSize();
                        if (answer == -1) {
                            button.setText("Cookie!");
                            //turnOnImage();
                        } else if (gameController.hasCookieAt(buttonPoint)) {
                            buttons[rowf][colf].setText(" Hint: " + gameController.select(new Point(rowf, colf)));
                        } else if (!gameController.hasCookieAt(buttonPoint)) {
                            button.setText("No Cookie! :(. \n Hint: " + answer);
                        }
                        updateAroundPoint(buttonPoint);

                    }

                    private void updateAroundPoint(Point startingPoint) {
                        for (int row = 0; row < MAX_ROW; row++) {
                            for (int col = 0; col < MAX_COL; col++) {
                                Point point = new Point(row, col);
                                //if its not the point that im at, update it.
                                if (!point.equals(startingPoint)) {
                                    if (gameController.hasVisited(point)) {
                                        if (!gameController.hasCookieVisitedAt(point)) {
                                            buttons[row][col].setText("No Cookie! :( \n Hint: " + gameController.select(new Point(row, col)));
                                            return;
                                        }else{
                                            buttons[row][col].setText(" Hint: " + gameController.select(new Point(row, col)));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    private void turnOnImage() {
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
                });
            }
        }
    }


    private void generateBoardLayout() {

        //set Image

        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                Button button = buttons[row][col];

                button.setBackgroundResource(R.drawable.game_cookie);
                /*Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_cookie);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
                Resources resource = getResources();
                button.setBackground(new BitmapDrawable(resource, scaledBitmap));*/

            }
        }

    }

}

package com.cmpt276.a3_cookiefinder.model.model.controller;

import com.cmpt276.a3_cookiefinder.model.model.game_obj.CookieMap;
import com.cmpt276.a3_cookiefinder.model.model.game_obj.Point;

import java.util.ArrayList;

/*
Game Controller manages game logic on a given board.
Main interaction will be with select(Point point):
    at a given point, it will return -1 for cookies found,
    and Int for hints count.
Game controller remembers found-cookies and as such
    hints count will not include them.
Cookie Map expect x = Row and y = Col
* */
public class GameController {
    private final CookieMap cookieMap;
    private final Options options;

    private int score = 0;
    private int turnCount = 0;
    private int maxScore;

    private int MAX_ROW;
    private int MAX_COL;
    private ArrayList<Point> revealedCookies = new ArrayList<>();
    private ArrayList<Point> hintedCookies = new ArrayList<>();
    private ArrayList<Point> visitedPoints = new ArrayList<>();
    private boolean[][] cellVisited;

    public GameController() {
        options = Options.getInstance();
        MAX_ROW = options.getSelectedRowNum();
        MAX_COL = options.getSelectedColNum();
        this.maxScore = options.getSelectedScoreNum();

        this.cookieMap = new CookieMap(MAX_ROW, MAX_COL, maxScore);
        cellVisited = new boolean[MAX_ROW][MAX_COL];

        for(boolean[] row : cellVisited){
            for (boolean col : row){
                col = false;
            }
        }
    }

    //main method for class to interact outside.
    public int select(Point point) {
        this.turnCount++;
        cellVisited[point.getX()][point.getY()] = true;
        visitedPoints.add(point.clone());

        if (hasCookieVisitedAt(point)) {
            if(!hintedCookies.contains(point)){
                hintedCookies.add(point);
            }
            return scanCookieHint(point);
        }
        if (hasCookieAt(point)) {
            revealedCookies.add(point);
            score++;
            return -1;
        } else {
            return scanCookieHint(point);
        }
    }

    public boolean hasVisited(Point point){
        return cellVisited[point.getX()][point.getY()];
    }

    public boolean hasCookieAt(Point point) {
        return cookieMap.hasCookieAt(point);
    }

    public boolean hasCookieVisitedAt(Point point) {
        return revealedCookies.contains(point);
    }

    public int scanCookieHint(Point point) {
        if (cookieMap.hasCookieAt(point)) {
            return scanSideWay(point) + scanDownWard(point);
        } else {
            return scanSideWay(point) + scanDownWard(point);
        }
    }

    private int scanDownWard(Point point) {
        int downwardCount = 0;
        int maxRow = cookieMap.getMaxRow();
        //System.out.println("Scan Downward: ");
        for (int i = 0; i < maxRow; i++) {
            Point rowPoint = new Point(i, point.getY());
            //System.out.println("(" + rowPoint.getX() + "," + rowPoint.getY() + ")");
            if (cookieMap.hasCookieAt(rowPoint)) {
                if (!revealedCookies.contains(rowPoint)) {
                    downwardCount++;
                }
            }
        }
        return downwardCount;
    }

    private int scanSideWay(Point point) {
        int sidewayCount = 0;
        //System.out.println("Scan Sideway: ");
        int maxCol = cookieMap.getMaxCol();
        for (int i = 0; i < maxCol; i++) {
            Point columnPoint = new Point(point.getX(), i);
            // System.out.println("(" + row.getX() + "," + row.getY() + ")");
            if (cookieMap.hasCookieAt(columnPoint)) {
                if (!revealedCookies.contains(columnPoint)) {
                    sidewayCount++;
                }
            }
        }
        return sidewayCount;
    }

    //Console debug
    public void draw() {
        System.out.println("Turn taken: " + turnCount);
        System.out.println("Cookies left: " + (cookieMap.getTotalCookies() - revealedCookies.size()));
        System.out.println("Total Cookies: " + cookieMap.getTotalCookies());
        int row = 0;
        for (ArrayList<Boolean> rows : cookieMap) {
            int col = 0;
            System.out.print('[');
            for (Boolean cell : rows) {
                Point point = new Point(row, col);
                if (revealedCookies.contains(point)) {
                    System.out.print(" " + scanCookieHint(point) + " ");
                } else if (cell) {
                    System.out.print(" C ");
                } else {
                    System.out.print(" \\ ");
                }
                col++;
            }
            System.out.print("] \n");
            row++;
        }
        System.out.println('\n');
    }

    public boolean cookieIsRevealed(Point point) {
        return revealedCookies.contains(point);
    }

    public boolean hasHintedCookieAt(Point point) {
        return hintedCookies.contains(point);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getCountedCookies(){
        if(revealedCookies.isEmpty()){
            return 0;
        }
        return revealedCookies.size();
    }

    public int getTurnCounts() {
        return turnCount;
    }

    public boolean won() {
        return score == maxScore;
    }

    public int getMAX_ROW() {
        return MAX_ROW;
    }
    public int getMAX_COL() {
        return MAX_COL;
    }

    public ArrayList<Point> getPointsToUpdate(Point startingPoint) {
        ArrayList<Point> answer = new ArrayList<>();

        for(Point point : visitedPoints){
            int row = point.getX();
            int col = point.getY();

            if (!point.equals(startingPoint)) {
                if (!hasCookieAt(point)) {
                    answer.add(point);
                } else if (hasHintedCookieAt(point)) {
                    answer.add(point);
                }
            }
        }
        return answer;
    }
}

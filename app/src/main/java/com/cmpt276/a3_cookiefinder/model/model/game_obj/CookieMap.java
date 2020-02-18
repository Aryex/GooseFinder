package com.cmpt276.a3_cookiefinder.model.model.game_obj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


/*
 * Cookies Map represent a map of cookies on a 2D Boolean-array.
 * Cookies map can be constructed given a dimension and cookies amount
   at initialization.
 * Cookies map check for cookies at coordinate.
 * Cookies map expect x = Row and y = Col
 * */

public class CookieMap implements Iterable<ArrayList<Boolean>> {
    final int MAX_ROW;
    final int MAX_COL;
    private int totalCookies;
    ArrayList<ArrayList<Boolean>> cookieMap = new ArrayList<>();

    public CookieMap(int rowMax, int colsMax, int maxCookies) {
        this.totalCookies = 0;
        this.MAX_COL = colsMax;
        this.MAX_ROW = rowMax;
        for (int i = 0; i < rowMax; i++) {
            cookieMap.add(generateRow(colsMax));
        }
        populateCookies(maxCookies);
    }

    private ArrayList<Boolean> generateRow(int colsMax) {
        ArrayList<Boolean> row = new ArrayList<>();
        for (int i = 0; i < colsMax; i++) {
            row.add(false);
        }
        return row;
    }

    //cookies are populated randomly at cell
    private void populateCookies(int maxCookies) {
        Random random = new Random();
        int cookiesAdded;
        for (cookiesAdded = 0; cookiesAdded < maxCookies; ) {
            int x = random.nextInt(MAX_ROW);
            int y = random.nextInt(MAX_COL);
            Point point = new Point(x, y);
            if (!hasCookieAt(point)) {
                putCookieAt(point);
                cookiesAdded++;
            }
        }
    }

    private boolean putCookieAt(Point point) {
        if (!hasCookieAt(point)) {
            int row = point.getX();
            int col = point.getY();
            cookieMap.get(row).remove(col);
            cookieMap.get(row).add(col, true);
            totalCookies++;
            return true;
        } else {
            return false;
        }
    }

    public boolean hasCookieAt(Point point) {
        int row = point.getX();
        int col = point.getY();
        return cookieMap.get(row).get(col);
    }

    public void print() {
        System.out.println("Total Cookies: " + this.totalCookies);
        //System.out.println("Dimension: " + this.MAX_ROW + " by " + this.MAX_COL);
        for (ArrayList<Boolean> rows : cookieMap) {
            System.out.print('[');
            for (Boolean cell : rows) {
                if (cell) {
                    System.out.print(" C ");
                } else {
                    System.out.print(" \\ ");
                }
            }
            System.out.print("] \n");
        }
        System.out.println('\n');
    }

    @Override
    public Iterator iterator() {
        return cookieMap.iterator();
    }

    public int size() {
        return cookieMap.size();
    }

    public int getMaxRow() {
        return MAX_ROW;
    }

    public int getMaxCol() {
        return MAX_COL;
    }

    //reset the board to have no cookies
    public void clear() {
        for (ArrayList<Boolean> row : this.cookieMap) {
            for (int i = 0; i < MAX_COL; i++) {
                row.set(i, false);
            }
        }
    }

    public int getTotalCookies() {
        return totalCookies;
    }
}

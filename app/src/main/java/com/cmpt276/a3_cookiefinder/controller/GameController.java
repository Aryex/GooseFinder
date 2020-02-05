package com.cmpt276.a3_cookiefinder.controller;

import com.cmpt276.a3_cookiefinder.model.game_obj.CookieMap;
import com.cmpt276.a3_cookiefinder.model.game_obj.Point;

import java.util.ArrayList;

/*
Game Controller manages game logic on a given board.
Main interaction will be with select(Point point):
    at a given point, it will return -1 for cookies
    found, and Int for hints count.
Game controller remembers found-cookies and as such
    hints count will not include them.
Cookie Map expect x = Row and y = Col
* */
public class GameController {
    private final CookieMap cookieMap;
    private int turn;
    ArrayList<Point> revealedCookies;

    public GameController(CookieMap cookieMap) {
        this.cookieMap = cookieMap;
        revealedCookies = new ArrayList<>();
    }

    public void test(){
        //knownCookiesPoints.add(new Point(0,0));
        System.out.println("Bool: " + revealedCookies.contains(new Point(0,0)));
        //System.out.println(scanCookieHint(new point()));
    }
    public int select(Point point){
        this.turn++;
        if(revealedCookies.contains(point)){
            return scanCookieHint(point);
        }
        if(cookieMap.hasCookieAt(point)){
            revealedCookies.add(point);
            return -1;
        }else{
            return scanCookieHint(point);
        }
    }

    private int scanCookieHint(Point point) {
        if(cookieMap.hasCookieAt(point)){
            return scanSideWay(point) + scanDownWard(point);
        }else{
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
            if(cookieMap.hasCookieAt(rowPoint)){
                if(!revealedCookies.contains(rowPoint)){
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
            if(cookieMap.hasCookieAt(columnPoint)){
                if(!revealedCookies.contains(columnPoint)){
                    sidewayCount++;
                }
            }
        }
        return sidewayCount;
    }
    //Console debug
    public void draw() {
        System.out.println("Turn taken: " + turn);
        System.out.println("Cookies left: " + (cookieMap.getTotalCookies() - revealedCookies.size()));
        System.out.println("Total Cookies: " + cookieMap.getTotalCookies());
        int row = 0;
        for (ArrayList<Boolean> rows : cookieMap) {
            int col = 0;
            System.out.print('[');
            for (Boolean cell : rows) {
                Point point = new Point(row, col);
                if(revealedCookies.contains(point)){
                    System.out.print(" " + scanCookieHint(point) + " ");
                }
                else if (cell) {
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
}

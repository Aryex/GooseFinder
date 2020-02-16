package com.cmpt276.a3_cookiefinder.model.model.controller;

import android.content.SharedPreferences;

import com.cmpt276.a3_cookiefinder.OptionsActivity;

public class Options {
    private int maxRow = 4;
    private int maxCol = 6;
    private int cookieNum = 6;

    private static Options instance = new Options();

    public static Options getInstance(){
        if(instance == null){
            instance = new Options();
        }
        return instance;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }

    public int getMaxCol() {
        return maxCol;
    }

    public void setMaxCol(int maxCol) {
        this.maxCol = maxCol;
    }

    public int getCookieNum() {
        return cookieNum;
    }

    public void setCookieNum(int cookieNum) {
        this.cookieNum = cookieNum;
    }

    private Options(){

    }
}

package com.cmpt276.a3_cookiefinder.model.model.controller;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

/*
Singleton for communicating options setting
*/
public class Options {
    private int selectedRowNum = 4;
    private int selectedColNum = 6;
    private int selectedScoreNum = 6;

    private SharedPreferences sharedPreferences;

    private static Options instance = new Options();

    public static Options getInstance(){
        if(instance == null){
            instance = new Options();
        }
        return instance;
    }

    public int getSelectedRowNum() {
        return selectedRowNum;
    }

    public void setSelectedRowNum(int selectedRowNum) {
        this.selectedRowNum = selectedRowNum;
    }

    public int getSelectedColNum() {
        return selectedColNum;
    }

    public void setSelectedColNum(int selectedColNum) {
        this.selectedColNum = selectedColNum;
    }

    public int getSelectedScoreNum() {
        return selectedScoreNum;
    }

    public void setSelectedScoreNum(int selectedScoreNum) {
        this.selectedScoreNum = selectedScoreNum;
    }

    private Options(){

    }
}

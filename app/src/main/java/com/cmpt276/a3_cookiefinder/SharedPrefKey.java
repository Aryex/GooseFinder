package com.cmpt276.a3_cookiefinder;

public class SharedPrefKey {
    private static SharedPrefKey sharedPrefKey;
    public static final String SHARED_PREF_HIGH_SCORE_TAG = "HighScore";
    public static final String SHARED_PREF_SAVED_ROW_KEY = "row";
    public static final String SHARED_PREF_SAVED_COL_KEY = "col";
    public static final String SHARED_PREF_SAVED_SCORE_KEY = "score";

    public static SharedPrefKey getInstance() {
        if (sharedPrefKey == null) {
            sharedPrefKey = new SharedPrefKey();
        }
        return sharedPrefKey;
    }

    public static String makeSharedPrefTotalTurnKey(int selectedRowSize, int selectedColSize, int selectedMaxScore) {
        return selectedRowSize + "x" + selectedColSize + ":" + selectedMaxScore + "total_turn";
    }

    public static String makeSharedPrefScorekey(int selectedRowSize, int selectedColSize, int selectedMaxScore) {
        return selectedRowSize + "x" + selectedColSize + ":" + selectedMaxScore + "score";
    }

    public static String makeSharedPrefTurnKey(int selectedRowSize, int selectedColSize, int selectedMaxScore) {
        return selectedRowSize + "x" + selectedColSize + ":" + selectedMaxScore + "turn";
    }

    private SharedPrefKey() {

    }
}

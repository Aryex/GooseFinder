package com.cmpt276.a3_cookiefinder.model.game_obj;
/*
Point class represents 2D coordinate*/
public class Point {
    int x = 0;
    int y = 0;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        return (this.x == ((Point) obj).getX())
                && (this.y == ((Point) obj).getY());
    }
}

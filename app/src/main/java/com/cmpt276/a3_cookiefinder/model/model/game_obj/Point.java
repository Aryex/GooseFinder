package com.cmpt276.a3_cookiefinder.model.model.game_obj;
/*
Point class represents 2D coordinate*/
public class Point {
    private int x = 0;
    private int y = 0;

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

    public Point clone(){
        return new Point(x,y);
    }
}

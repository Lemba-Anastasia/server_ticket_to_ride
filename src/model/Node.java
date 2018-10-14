package model;

import java.awt.*;

import static java.lang.Math.sqrt;

public class Node {
    private boolean isActive = false;
    private Point point;
    private String name;

    public Node(String name, int x, int y) {
        point=new Point();
        this.point.x = x;
        this.point.y = y;
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int overlapWithCursor(int posX, int posY) {
        return (int)sqrt(posX - point.x) * (posX -point.x ) + (posY - point.y ) * (posY - point.y );
    }
    public String getName() {
        return name;
    }
    public Point getPoint(){return point;}
}

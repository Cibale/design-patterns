package hr.fer.zemris.util;

/**
 * Created by mmatak on 6/10/16.
 */
public class Rectangle {
    private int x, y, height, width;

    /**
     * Upper left corner and width and height.
     *
     * @param x
     * @param y
     * @param height
     * @param width
     */
    public Rectangle(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public boolean isInside(Point point){
        int pointX = point.getX();
        int pointY = point.getY();
        if (pointX >= x && pointX <= x + width && pointY >= y && pointY <= y + height){
            return true;
        }
        return false;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getMinX() {
        return x;
    }

    public int getMaxX() {
        return x + width;
    }

    public int getMinY() {
        return y;
    }

    public int getMaxY() {
        return y + height;
    }
}

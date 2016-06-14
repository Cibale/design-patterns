package hr.fer.zemris.util;

/**
 * Represents one point in 2D.
 * Created by mmatak on 6/10/16.
 */
public class Point {
    private int x;
    private int y;

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

    /**
     * Returns this point + <code>dp</code>.
     *
     * @return new point which is calculated as this point + <code>dp</code>
     */
    public Point translate(Point dp) {
        return new Point(this.x + dp.x, this.y + dp.y);
    }

    /**
     * Returns this point - <code>p</code>.
     *
     * @return new point which is calculated as this point - <code>p</code>
     */
    public Point difference(Point p) {
        return new Point(this.x - p.x, this.y - p.y);
    }


    public Point scalarMultiply(double scalar){
        return new Point((int) Math.round(x * scalar), (int) Math.round(y * scalar));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}

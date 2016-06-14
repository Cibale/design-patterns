package hr.fer.zemris.util;
import java.lang.Math;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Useful functions are implemented here.
 * Created by mmatak on 6/10/16.
 */
public class GeometryUtil {
    /**
     * Returns euclidian distance between two points.
     */
    public static double distanceFromPoint(Point point1, Point point2) {
        return Math.sqrt(
                square(point1.getX() - point2.getX()) +
                square(point1.getY() - point2.getY())
        );
    }

    /**
     * Returns shortest distance between point <code>p</code> and
     * line which is defined with start point <code>s</code> and
     * end point <code>e</code>.
     */
    public static double distanceFromLineSegment(Point s, Point e, Point p) {

        if (isBetween(s, e, p)) {
            int dx = e.getX() - s.getX();
            int dy = e.getY() - s.getY();
            double numerator = Math.abs(dy * p.getX() - dx * p.getY() + e.getX() * s.getY() - e.getY() * s.getX());
            double denominator = distanceFromPoint(s, e);

            return numerator / denominator;
        } else {
            return Math.min(distanceFromPoint(s, p), distanceFromPoint(e, p));
        }

    }
    // check if point is between two points that make a line
    private static boolean isBetween(Point s, Point e, Point p) {
        int minx = Math.min(s.getX(), e.getX());
        int maxx = Math.max(s.getX(), e.getX());

        int miny = Math.min(s.getY(), e.getY());
        int maxy = Math.max(s.getY(), e.getY());

        return (p.getX() >= minx && p.getX() <= maxx) || (p.getY() >= miny && p.getY() <= maxy);
    }

    public static double dotProduct(Point a, Point b) {
        return a.getX()*b.getX() + a.getY()*b.getY();
    }

    public static double square(double value){
        return value*value;
    }
}

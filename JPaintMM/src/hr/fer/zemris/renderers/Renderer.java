package hr.fer.zemris.renderers;

import hr.fer.zemris.util.Point;

/**
 * Created by mmatak on 6/10/16.
 */
public interface Renderer {
    void drawLine(Point s, Point e);
    void fillPolygon(Point[] points);
    default void drawRectangle(Point upperLeft, int width, int height){
        Point upperRight = upperLeft.translate(new Point(width, 0));
        Point downRight = upperRight.translate(new Point(0, height));
        Point downLeft = upperLeft.translate(new Point(0, height));
        drawLine(upperLeft, upperRight);
        drawLine(upperRight, downRight);
        drawLine(downLeft, downRight);
        drawLine(upperLeft, downLeft);
    }
}
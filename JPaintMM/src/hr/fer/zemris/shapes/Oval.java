package hr.fer.zemris.shapes;

import hr.fer.zemris.util.Point;
import hr.fer.zemris.util.Rectangle;
import hr.fer.zemris.renderers.Renderer;

import java.util.List;
import java.util.Stack;

import static hr.fer.zemris.util.GeometryUtil.distanceFromLineSegment;
import static hr.fer.zemris.util.GeometryUtil.square;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Elipse.
 * Created by mmatak on 6/11/16.
 */
public class Oval extends AbstractGraphicalObject {
    private final static String SHAPE_NAME = "Oval";
    private Point center;

    public Oval(Point[] hotPoints) {
        super(hotPoints);
        if (hotPoints.length != 2) {
            throw new IllegalArgumentException("Must be two hot points");
        }
        center = new Point(
                getHotPoint(0).getX(),
                getHotPoint(1).getY()
        );
    }

    //index 0 is bottom hot point
    //index 1 is side hot point
    public Oval() {
        this(new Point[]{new Point(0, 10), new Point(10, 0)});
    }

    @Override
    public Rectangle getBoundingBox() {
        int width = 2 * abs(getHotPoint(0).getX() - getHotPoint(1).getX());
        int height = 2 * abs(getHotPoint(0).getY() - getHotPoint(1).getY());
        int upperLeftX = getHotPoint(0).getX() - width / 2;
        int upperLeftY = getHotPoint(1).getY() - height / 2;
        return new Rectangle(upperLeftX, upperLeftY, height, width);
    }


    @Override
    public double selectionDistance(Point mousePoint) {
        int radiusX = abs(getHotPoint(0).getX() - getHotPoint(1).getX());
        int radiusY = abs(getHotPoint(0).getY() - getHotPoint(1).getY());
        int xCenter = center.getX();
        int yCenter = center.getY();
        int x = mousePoint.getX();
        int y = mousePoint.getY();
        // if inside elipse
        if (Double.compare(
                square(xCenter - x) / square(radiusX) + square(yCenter - y) / square(radiusY), 1)
                <= 0) {
            return 0;
        }
        //if not inside of elipse - approximation of elipse with rectangle :)
        Point north = new Point(xCenter, yCenter - radiusY);
        Point south = new Point(xCenter, yCenter + radiusY);
        Point east = new Point(xCenter + radiusX, yCenter);
        Point west = new Point(xCenter - radiusX, yCenter);
        if (mousePoint.getX() <= xCenter && mousePoint.getY() <= yCenter) {
            //upper left
            return distanceFromLineSegment(west, north, mousePoint);
        } else if (mousePoint.getX() > xCenter && mousePoint.getY() <= yCenter) {
            //upper right
            return distanceFromLineSegment(north, east, mousePoint);
        } else if (mousePoint.getX() <= xCenter && mousePoint.getY() > yCenter) {
            //bottom left
            return distanceFromLineSegment(west, south, mousePoint);
        } else if (mousePoint.getX() > xCenter && mousePoint.getY() > yCenter) {
            //bottom right
            return distanceFromLineSegment(south, east, mousePoint);
        }
        //error in calculation
        return -1;
    }

    @Override
    public void render(Renderer r) {
        r.fillPolygon(calculatePoints());
    }

    /**
     * Calculates points which are needed for drawing implementation.
     * They are calculated from hot points and returned as array.
     * Number of points which approximate oval is defined with <@link>POINTS_SIZE</@link>.
     */
    private static final int POINTS_SIZE = 30;

    private Point[] calculatePoints() {
        Point[] points = new Point[POINTS_SIZE];
        center = new Point(
                getHotPoint(0).getX(),
                getHotPoint(1).getY()
        );
        int x = center.getX();
        int y = center.getY();

        int a = max(abs(x - getHotPoint(0).getX()), abs(x - getHotPoint(1).getX()));
        int b = max(abs(y - getHotPoint(0).getY()), abs(y - getHotPoint(1).getY()));

        for (int i = 0; i < POINTS_SIZE; i++) {
            int px = x + (int) (a * Math.cos(2 * Math.PI * i / POINTS_SIZE));
            int py = y + (int) (b * Math.sin(2 * Math.PI * i / POINTS_SIZE));
            points[i] = new Point(px, py);
        }

        return points;
    }

    @Override
    public String getShapeName() {
        return SHAPE_NAME;
    }

    @Override
    public GraphicalObject duplicate() {
        return new Oval(new Point[]{getHotPoint(0), getHotPoint(1)});
    }

    @Override
    public String getShapeID() {
        return "@OVAL";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] chunks = data.trim().split(" ");
        Point right = new Point(
                Integer.parseInt(chunks[0]),
                Integer.parseInt(chunks[1])
        );
        Point bottom = new Point(
                Integer.parseInt(chunks[2]),
                Integer.parseInt(chunks[3])
        );
        stack.add(new Oval(new Point[]{bottom, right}));
    }

    @Override
    public void save(List<String> rows) {
        StringBuilder buff = new StringBuilder();
        buff.append(getShapeID()).append(" ");
        Point bottom = getHotPoint(0);
        Point right = getHotPoint(1);
        buff.append(right.getX()).append(" ").append(right.getY()).append(" ");
        buff.append(bottom.getX()).append(" ").append(bottom.getY());
        rows.add(buff.toString());
    }
}

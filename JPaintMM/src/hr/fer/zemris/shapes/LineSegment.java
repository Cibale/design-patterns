package hr.fer.zemris.shapes;

import hr.fer.zemris.util.GeometryUtil;
import hr.fer.zemris.util.Point;
import hr.fer.zemris.util.Rectangle;
import hr.fer.zemris.renderers.Renderer;

import java.util.List;
import java.util.Stack;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * Segment of a line. It is defined by start and end point.
 * Created by mmatak on 6/11/16.
 */
public class LineSegment extends AbstractGraphicalObject {
    private final static String SHAPE_NAME = "Linija";

    public LineSegment(Point start, Point end) {
        super(new Point[]{start, end});
    }

    /**
     * Default constructor where line segment is (0,0)-(10,0).
     */
    public LineSegment() {
        this(new Point(0, 0), new Point(10, 0));
    }

    @Override
    public Rectangle getBoundingBox() {
        int width = abs(getHotPoint(0).getX() - getHotPoint(1).getX());
        int height = abs(getHotPoint(1).getY() - getHotPoint(0).getY());
        int upperLeftX = min(getHotPoint(1).getX(), getHotPoint(0).getX());
        int upperLeftY = min(getHotPoint(1).getY(), getHotPoint(0).getY());
        return new Rectangle(upperLeftX, upperLeftY, height, width);
    }


    @Override
    public double selectionDistance(Point mousePoint) {
        return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
    }

    @Override
    public void render(Renderer r) {
        r.drawLine(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeName() {
        return SHAPE_NAME;
    }

    @Override
    public GraphicalObject duplicate() {
        return new LineSegment(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeID() {
        return "@LINE";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] chunks = data.trim().split(" ");
        Point start = new Point(
                Integer.parseInt(chunks[0]),
                Integer.parseInt(chunks[1])
        );
        Point end = new Point(
                Integer.parseInt(chunks[2]),
                Integer.parseInt(chunks[3])
        );
        stack.add(new LineSegment(start, end));
    }

    @Override
    public void save(List<String> rows) {
        StringBuilder buff = new StringBuilder();
        buff.append(getShapeID());
        buff.append(" ");
        Point start = getHotPoint(0);
        Point end = getHotPoint(1);
        buff.append(start.getX()).append(" ").append(start.getY()).append(" ");
        buff.append(end.getX()).append(" ").append(end.getY());
        rows.add(buff.toString());
    }
}

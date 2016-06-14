package hr.fer.zemris.shapes;

import hr.fer.zemris.renderers.Renderer;
import hr.fer.zemris.states.State;
import hr.fer.zemris.util.Point;
import hr.fer.zemris.util.Rectangle;

import java.util.*;

/**
 * Composit shape (Composit design pattern) which consists of collection of primitive shapes.
 * Created by mmatak on 6/13/16.
 */
public class CompositeShape extends AbstractGraphicalObject {
    private final List<GraphicalObject> graphicalObjects;


    public CompositeShape(List<GraphicalObject> graphicalObjects) {
        super(new Point[]{});
        Objects.requireNonNull(graphicalObjects);
        this.graphicalObjects = graphicalObjects;
    }

    /**
     * Empty constructor.
     */
    public CompositeShape() {
        this(new ArrayList<>());
    }

    @Override
    public Rectangle getBoundingBox() {
        int currentMinX = Integer.MAX_VALUE;
        int currentMaxX = Integer.MIN_VALUE;
        int currentMinY = Integer.MAX_VALUE;
        int currentMaxY = Integer.MIN_VALUE;
        for (GraphicalObject shape : graphicalObjects) {
            Rectangle rect = shape.getBoundingBox();
            if (rect.getMaxX() > currentMaxX) {
                currentMaxX = rect.getMaxX();
            }
            if (rect.getMinX() < currentMinX) {
                currentMinX = rect.getMinX();
            }
            if (rect.getMinY() < currentMinY) {
                currentMinY = rect.getMinY();
            }
            if (rect.getMaxY() > currentMaxY) {
                currentMaxY = rect.getMaxY();
            }
        }
        int width = currentMaxX - currentMinX;
        int height = currentMaxY - currentMinY;
        return new Rectangle(currentMinX, currentMinY, height, width);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        double minDistance = Double.MAX_VALUE;
        if (getBoundingBox().isInside(mousePoint)) {
            return 0;
        }
        for (GraphicalObject graphicalObject : graphicalObjects) {
            double objDistance = graphicalObject.selectionDistance(mousePoint);
            if (Double.compare(objDistance, minDistance) < 0) {
                minDistance = objDistance;
            }
        }
        return minDistance;
    }

    @Override
    public void render(Renderer r) {
        for (GraphicalObject object : graphicalObjects) {
            object.render(r);
        }
    }

    @Override
    public String getShapeName() {
        return "Kompozit";
    }

    @Override
    public GraphicalObject duplicate() {
        return new CompositeShape(graphicalObjects);
    }

    @Override
    public String getShapeID() {
        return "@COMP";
    }

    @Override
    public void translate(Point delta) {
        for (GraphicalObject graphicalObject : graphicalObjects) {
            graphicalObject.translate(delta);
        }
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        try {
            List<GraphicalObject> objects = new ArrayList<>();
            for (int i = 0, n = Integer.parseInt(data); i < n; i++) {
                objects.add(stack.pop());
            }
            Collections.reverse(objects);
            stack.add(new CompositeShape(objects));
        } catch (EmptyStackException e) {
            System.err.println("Input file not properly formatted! " +
                    "Probably composite is wrong defined.");
        }
    }

    public List<GraphicalObject> getChildren() {
        return graphicalObjects;
    }

    @Override
    public void save(List<String> rows) {
        for (GraphicalObject object : graphicalObjects) {
            object.save(rows);
        }
        rows.add(getShapeID() + " " + graphicalObjects.size());
    }
}

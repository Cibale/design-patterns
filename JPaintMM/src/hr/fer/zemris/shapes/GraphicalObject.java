package hr.fer.zemris.shapes;

import hr.fer.zemris.GraphicalObjectListener;
import hr.fer.zemris.util.Point;
import hr.fer.zemris.util.Rectangle;
import hr.fer.zemris.renderers.Renderer;

import java.util.List;
import java.util.Stack;

/**
 * Interface which describes any graphical object which is part of JPaintMM.
 * Created by mmatak on 6/10/16.
 */
public interface GraphicalObject {
    // Support for object editing
    boolean isSelected();
    void setSelected(boolean selected);
    int getNumberOfHotPoints();
    Point getHotPoint(int index);
    void setHotPoint(int index, Point point);
    boolean isHotPointSelected(int index);
    void setHotPointSelected(int index, boolean selected);
    double getHotPointDistance(int index, Point mousePoint);

    // Geometrical operations with object
    void translate(Point delta);
    Rectangle getBoundingBox();
    double selectionDistance(Point mousePoint);

    // Support for rendering (part of Bridge design pattern)
    void render(Renderer r);

    // Observer for changes on model
    public void addGraphicalObjectListener(GraphicalObjectListener l);
    public void removeGraphicalObjectListener(GraphicalObjectListener l);

    // Support for prototype design pattern
    String getShapeName();
    GraphicalObject duplicate();

    // Support for save and open
    public String getShapeID();
    public void load(Stack<GraphicalObject> stack, String data);
    public void save(List<String> rows);
}

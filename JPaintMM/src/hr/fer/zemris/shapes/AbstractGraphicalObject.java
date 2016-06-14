package hr.fer.zemris.shapes;

import hr.fer.zemris.util.GeometryUtil;
import hr.fer.zemris.GraphicalObjectListener;
import hr.fer.zemris.util.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Abstraction of graphical objects.
 * Created by mmatak on 6/11/16.
 */
public abstract class AbstractGraphicalObject implements GraphicalObject{
    private Point[] hotPoints;
    private boolean[] hotPointSelected;
    private boolean selected;
    private List<GraphicalObjectListener> listeners = new ArrayList<>();

    /**
     * Hard copy constructor.
     */
    public AbstractGraphicalObject(Point[] hotPoints) {
        Objects.requireNonNull(hotPoints);
        this.hotPoints = Arrays.copyOf(hotPoints, hotPoints.length);
        hotPointSelected = new boolean[hotPoints.length];
    }


    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        notifySelectionListeners();
    }

    @Override
    public int getNumberOfHotPoints() {
        return hotPoints.length;
    }

    @Override
    public Point getHotPoint(int index) {
        return hotPoints[index];
    }

    @Override
    public void setHotPoint(int index, Point point) {
        hotPoints[index] = point;
        notifyListeners();
    }

    @Override
    public boolean isHotPointSelected(int index) {
        return hotPointSelected[index];
    }

    @Override
    public void setHotPointSelected(int index, boolean selected) {
        hotPointSelected[index] = selected;
        notifyListeners();
    }

    @Override
    public double getHotPointDistance(int index, Point mousePoint) {
        return GeometryUtil.distanceFromPoint(hotPoints[index], mousePoint);
    }

    @Override
    public void translate(Point delta) {
        for (int i = 0; i < hotPoints.length; i++){
            hotPoints[i] = hotPoints[i].translate(delta);
        }
        notifyListeners();
    }

    @Override
    public void addGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.add(l);
    }

    @Override
    public void removeGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.remove(l);
    }

    public void notifyListeners(){
        for (GraphicalObjectListener listener : new ArrayList<>(listeners)){
            listener.graphicalObjectChanged(this);
        }
    }

    public void notifySelectionListeners(){
       for (GraphicalObjectListener listener : new ArrayList<>(listeners)){
           listener.graphicalObjectSelectionChanged(this);
       }
    }
}

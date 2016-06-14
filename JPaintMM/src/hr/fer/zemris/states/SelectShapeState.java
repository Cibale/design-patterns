package hr.fer.zemris.states;

import hr.fer.zemris.DocumentModel;
import hr.fer.zemris.renderers.Renderer;
import hr.fer.zemris.shapes.CompositeShape;
import hr.fer.zemris.shapes.GraphicalObject;
import hr.fer.zemris.util.Point;
import hr.fer.zemris.util.Rectangle;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * State in which selection is done.
 * Created by mmatak on 6/13/16.
 */
public class SelectShapeState extends StateAdapter {
    private DocumentModel model;
    private final static int HOT_POINT_RECTANGLE_SIDE = 6;

    public SelectShapeState(DocumentModel model) {
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        if (!ctrlDown) {
            for (GraphicalObject o : new ArrayList<>(model.getSelectedObjects())) {
                o.setSelected(false);
            }
        }
        GraphicalObject object = model.findSelectedGraphicalObject(mousePoint);
        if (object != null) {
            object.setSelected(true);
        }
        //else remove selection from others?
    }


    @Override
    public void mouseDragged(Point mousePoint) {
        if (model.getSelectedObjects().size() != 1) {
            return;
        }
        GraphicalObject go = model.getSelectedObjects().get(0);
        int hotPointIndex = model.findSelectedHotPoint(go, mousePoint);
        if (hotPointIndex == -1) {
            return;
        }
        //go.setHotPointSelected(hotPointIndex, true); ??
        go.setHotPoint(hotPointIndex, mousePoint);
    }

    @Override
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_G:
                groupSelectedShapes();
                break;
            case KeyEvent.VK_U:
                degroupSelectedShapes();
                break;
            case KeyEvent.VK_UP:
                moveSelectedShapes(new Point(0, -1));
                break;
            case KeyEvent.VK_DOWN:
                moveSelectedShapes(new Point(0, 1));
                break;
            case KeyEvent.VK_LEFT:
                moveSelectedShapes(new Point(-1, 0));
                break;
            case KeyEvent.VK_RIGHT:
                moveSelectedShapes(new Point(1, 0));
                break;
            case KeyEvent.VK_ADD:
                changeOrder(true);
                break;
            case KeyEvent.VK_SUBTRACT:
                changeOrder(false);
                break;
            default:
                return;
        }
    }

    /** Method which is opposite of <@link>groupSelectedShapes</@link> method. */
    private void degroupSelectedShapes() {
        if (model.getSelectedObjects().size() != 1){
            return;
        }
        if (!(model.getSelectedObjects().get(0) instanceof CompositeShape)){
            return;
        }
        CompositeShape composite = (CompositeShape) model.getSelectedObjects().get(0);
        composite.setSelected(false);
        for (GraphicalObject shape : composite.getChildren()){
            shape.setSelected(true);
            model.addGraphicalObject(shape);
        }
        model.removeGraphicalObject(composite);
    }

    private void groupSelectedShapes() {
        if (model.getSelectedObjects().size() < 2){
            return;
        }
        List<GraphicalObject> selectedObjects = new ArrayList<>(model.getSelectedObjects());
        for (GraphicalObject selectedObject : selectedObjects){
            selectedObject.setSelected(false);
            model.removeGraphicalObject(selectedObject.duplicate());
        }
        CompositeShape shape = new CompositeShape(selectedObjects);
        shape.setSelected(true);
        model.addGraphicalObject(shape);
    }

    private void changeOrder(boolean increaseZ) {
        if (model.getSelectedObjects().size() != 1) {
            return;
        }
        if (increaseZ) {
            model.increaseZ(model.getSelectedObjects().get(0));
        } else {
            model.decreaseZ(model.getSelectedObjects().get(0));
        }
    }

    private void moveSelectedShapes(Point point) {
        for (GraphicalObject o : model.getSelectedObjects()) {
            o.translate(point);
        }
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if (go.isSelected()) {
            Rectangle rectangle = go.getBoundingBox();
            r.drawRectangle(
                    new Point(rectangle.getX(), rectangle.getY()),
                    rectangle.getWidth(),
                    rectangle.getHeight()
            );
        }
    }

    @Override
    public void afterDraw(Renderer r) {
        if (model.getSelectedObjects().size() == 1) {
            GraphicalObject go = model.getSelectedObjects().get(0);
            for (int i = 0; i < go.getNumberOfHotPoints(); i++) {
                Point upperLeft = go.getHotPoint(i).difference(
                        new Point(
                                HOT_POINT_RECTANGLE_SIDE / 2,
                                HOT_POINT_RECTANGLE_SIDE / 2
                        )
                );
                r.drawRectangle(upperLeft, HOT_POINT_RECTANGLE_SIDE, HOT_POINT_RECTANGLE_SIDE);
            }
        }
    }

    @Override
    public void onLeaving() {
        for (GraphicalObject o : model.list()) {
            o.setSelected(false);
        }
    }
}

package hr.fer.zemris.states;

import hr.fer.zemris.DocumentModel;
import hr.fer.zemris.renderers.Renderer;
import hr.fer.zemris.shapes.GraphicalObject;
import hr.fer.zemris.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Eraser state in which all objects over which line is drawn are going to be deleted from model.
 * Created by mmatak on 6/13/16.
 */
public class EraserState extends StateAdapter {
    private DocumentModel model;
    private List<Point> linePoints = new ArrayList<>();

    public EraserState(DocumentModel model) {
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        linePoints.add(mousePoint);
        model.notifyListeners();
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        for (GraphicalObject o : new ArrayList<>(model.list())) {
            for (Point point : linePoints) {
                if (Double.compare(o.selectionDistance(point), DocumentModel.SELECTION_PROXIMITY) < 0) {
                    model.removeGraphicalObject(o);
                }
            }
        }
        linePoints.clear();
        model.notifyListeners();
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        linePoints.add(mousePoint);
        model.notifyListeners();
    }

    @Override
    public void onLeaving() {
        model.notifyListeners();
    }

    @Override
    public void afterDraw(Renderer r) {
        for (int i = 0; i < linePoints.size() - 1; i++) {
            r.drawLine(linePoints.get(i), linePoints.get(i + 1));
        }
    }
}

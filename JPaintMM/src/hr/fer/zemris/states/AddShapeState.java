package hr.fer.zemris.states;

import hr.fer.zemris.DocumentModel;
import hr.fer.zemris.renderers.Renderer;
import hr.fer.zemris.shapes.GraphicalObject;
import hr.fer.zemris.util.Point;

/**
 * State where in which shapes are added.
 * Created by mmatak on 6/13/16.
 */
public class AddShapeState extends StateAdapter {
    private GraphicalObject prototype;
    private DocumentModel model;

    public AddShapeState(GraphicalObject prototype, DocumentModel model) {
        this.prototype = prototype;
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        GraphicalObject duplicate = prototype.duplicate();
        duplicate.translate(mousePoint);
        //duplicate twice so center in oval is recalculated
        model.addGraphicalObject(duplicate.duplicate());
    }
}

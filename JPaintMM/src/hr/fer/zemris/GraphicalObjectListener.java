package hr.fer.zemris;

import hr.fer.zemris.shapes.GraphicalObject;

/**
 * Listener for changes on graphical object. Part of observer design pattern.
 * Created by mmatak on 6/10/16.
 */
public interface GraphicalObjectListener {
    /** Is called when anything is changed with object. */
    void graphicalObjectChanged(GraphicalObject go);

    /** Called only when object selection state is changed (object, not his hot points) */
    void graphicalObjectSelectionChanged(GraphicalObject go);
}

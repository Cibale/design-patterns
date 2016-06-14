package hr.fer.zemris.states;

import hr.fer.zemris.renderers.Renderer;
import hr.fer.zemris.shapes.GraphicalObject;
import hr.fer.zemris.util.Point;

/**
 * Interface as part of State design pattern.
 * Implementations of this interface should define what
 * is actually done when mouse is pressed, released, some key is pressed etc.
 * <p>
 * Created by mmatak on 6/13/16.
 */
public interface State {
    // poziva se kad progam registrira da je pritisnuta lijeva tipka miša

    /**
     * Called when program registers that left mouse button is pressed.
     */
    void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown);

    /**
     * Called when program registers that left mouse button is released.
     */
    void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown);

    /**
     * Called when program registers that user is moving mouse while left button is pressed.
     */
    void mouseDragged(Point mousePoint);

    /**
     * Called when program registers that user pressed a key on keyboard.
     */
    void keyPressed(int keyCode);

    /**
     * Called after canvas has drawn graphical object passed as argument.
     */
    void afterDraw(Renderer r, GraphicalObject go);

    /**
     * Called after canvas has drawn the whole drawing.
     */
    void afterDraw(Renderer r);

    /**
     * Called when program leaves this state and goes to another.
     */
    void onLeaving();
}

package hr.fer.zemris.states;

import hr.fer.zemris.renderers.Renderer;
import hr.fer.zemris.shapes.GraphicalObject;
import hr.fer.zemris.util.Point;

/**
 * Empty state.
 * Created by mmatak on 6/13/16.
 */
public class StateAdapter implements State{
    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseDragged(Point mousePoint) {

    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {

    }

    @Override
    public void afterDraw(Renderer r) {

    }

    @Override
    public void onLeaving() {

    }
}

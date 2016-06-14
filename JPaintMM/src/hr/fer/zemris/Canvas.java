package hr.fer.zemris;

import hr.fer.zemris.renderers.*;
import hr.fer.zemris.shapes.GraphicalObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Canvas on which all of the objects are drawn.
 * Created by mmatak on 6/13/16.
 */
public class Canvas extends JComponent implements DocumentModelListener, GraphicalObjectListener {
    private final GUI parentFrame;
    private DocumentModel model;

    public Canvas(GUI frame, DocumentModel model) {
        this.parentFrame = frame;
        this.model = model;
        model.addDocumentModelListener(this);
        addListeners();
    }

    private void addListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                parentFrame.getCurrentState().keyPressed(e.getKeyCode());
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                parentFrame.getCurrentState().mouseDragged(new hr.fer.zemris.util.Point(e.getX(), e.getY()));
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                parentFrame.getCurrentState().mouseDown(new hr.fer.zemris.util.Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                parentFrame.getCurrentState().mouseUp(new hr.fer.zemris.util.Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        hr.fer.zemris.renderers.Renderer renderer = new G2DRendererImpl(g2d);
        for (GraphicalObject object : model.list()) {
            object.render(renderer);
            parentFrame.getCurrentState().afterDraw(renderer, object);
        }
        parentFrame.getCurrentState().afterDraw(renderer);
        requestFocusInWindow();
    }

    @Override
    public void documentChanged() {
        repaint();
    }

    @Override
    public void graphicalObjectChanged(GraphicalObject go) {
        repaint();
    }

    @Override
    public void graphicalObjectSelectionChanged(GraphicalObject go) {
        repaint();
    }
}

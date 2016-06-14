package hr.fer.zemris;

import hr.fer.zemris.renderers.SVGRendererImpl;
import hr.fer.zemris.shapes.CompositeShape;
import hr.fer.zemris.shapes.GraphicalObject;
import hr.fer.zemris.shapes.LineSegment;
import hr.fer.zemris.shapes.Oval;
import hr.fer.zemris.states.*;
import hr.fer.zemris.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * Main frame in application.
 * Created by mmatak on 6/13/16.
 */
public class GUI extends JFrame {
    private State currentState;
    /**
     * Prototypes only, real objects are stored in model.
     */
    private final List<GraphicalObject> objects;
    private DocumentModel model = new DocumentModel();

    private GUI(List<GraphicalObject> objects) {
        this.objects = objects;
        currentState = new IdleState();
        final int WINDOW_WIDTH = 500;
        final int WINDOW_HEIGHT = 500;
        final int FRAME_X_POSITION = 100;
        final int FRAME_Y_POSITION = 100;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        setTitle("JPaintMM " + Character.toString((char) 0x00a9) + " Martin Matak 2016");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(FRAME_X_POSITION, FRAME_Y_POSITION);
        initGUI();
        //when escape is typed, go to Idle state
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "idle");
        am.put("idle", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentState.onLeaving();
                currentState = new IdleState();
            }
        });
    }

    private void initGUI() {
        getContentPane().setLayout(new BorderLayout());
        Canvas canvas = new Canvas(this, model);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(canvas, BorderLayout.CENTER);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        createToolbar();
    }

    private void createToolbar() {
        JToolBar toolBar = new JToolBar("Tools");
        toolBar.setFloatable(true);

        for (GraphicalObject o : objects) {
            JButton button = new JButton(o.getShapeName());
            button.addActionListener(e -> {
                currentState.onLeaving();
                currentState = new AddShapeState(o, model);
            });
            toolBar.add(button);
        }
        JButton selectionMode = new JButton("Selektiraj");
        selectionMode.addActionListener(e -> {
            currentState.onLeaving();
            currentState = new SelectShapeState(model);
        });
        toolBar.add(selectionMode);


        JButton eraserMode = new JButton("Brisalo");
        eraserMode.addActionListener(e -> {
            currentState.onLeaving();
            currentState = new EraserState(model);
        });
        toolBar.add(eraserMode);

        JButton svgExport = new JButton("SVG Export");
        svgExport.addActionListener(e -> saveAsSVG());
        toolBar.add(svgExport);

        JButton saveNative = new JButton("Pohrani");
        saveNative.addActionListener(e ->
                saveNativeFormat());
        toolBar.add(saveNative);

        JButton loadNative = new JButton("Učitaj");
        loadNative.addActionListener(e -> loadNativeFormat());
        toolBar.add(loadNative);

        getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    private void loadNativeFormat() {
        String fileName = loadFile();
        if (fileName == null) {
            return;
        }
        List<String> lines = FileUtil.load(fileName);
        if (lines == null) {
            return;
        }
        Map<String, GraphicalObject> idObject = new HashMap<>();
        for (GraphicalObject object : objects) {
            idObject.put(object.getShapeID(), object);
        }
        CompositeShape composite = new CompositeShape();
        idObject.put(composite.getShapeID(), composite);
        Stack<GraphicalObject> graphicalObjectStack = new Stack<>();
        for (String line : lines) {
            String[] chunks = line.split(" ", 2);
            String id = chunks[0].trim();
            String data = chunks[1].trim();
            idObject.get(id).load(graphicalObjectStack, data);
        }
        model.clear();
        for (GraphicalObject object : graphicalObjectStack) {
            model.addGraphicalObject(object);
        }
    }

    private String loadFile() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open file");
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File fileName = fc.getSelectedFile();
        Path filePath = fileName.toPath();

        //TODO: Add check if same file is already opened

        if (!Files.isReadable(filePath)) {
            JOptionPane.showMessageDialog(this,
                    "Error while reading",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return filePath.toString();
    }

    private void saveNativeFormat() {
        String fileName = chooseFile();
        if (fileName == null) {
            return;
        }
        List<String> lines = new ArrayList<>();
        for (GraphicalObject g : model.list()) {
            g.save(lines);
        }
        FileUtil.saveFile(Paths.get(fileName), lines);
    }

    private void saveAsSVG() {
        String fileName = chooseFile();
        if (fileName == null) {
            return;
        }
        SVGRendererImpl r = new SVGRendererImpl(fileName);
        for (GraphicalObject o : model.list()) {
            o.render(r);
        }
        r.close();
    }

    private String chooseFile() {
        JFileChooser jfc = new JFileChooser() {
            private static final long serialVersionUID = 1L;

            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                if (f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(this,
                            "File you choosed already exists, are you sure you want to overwrite it?",
                            "File already exists..",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;
                        case JOptionPane.NO_OPTION:
                            return;
                        case JOptionPane.CLOSED_OPTION:
                            return;
                        case JOptionPane.CANCEL_OPTION:
                            cancelSelection();
                            return;
                    }
                }
                super.approveSelection();
            }
        };
        jfc.setDialogTitle("Save file as");
        if (jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        return jfc.getSelectedFile().toString();
    }

    public static void main(String[] args) {
        List<GraphicalObject> objects = new ArrayList<>();

        objects.add(new LineSegment());
        objects.add(new Oval());

        GUI gui = new GUI(objects);
        gui.setVisible(true);
    }

    public State getCurrentState() {
        return currentState;
    }
}

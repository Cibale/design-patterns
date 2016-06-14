package hr.fer.zemris.app;

import hr.fer.zemris.app.actions.*;
import hr.fer.zemris.app.plugins.IPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Frame in which editor is shown.
 * //TODO: Make it scrollable
 * <p>
 * Created by mmatak on 6/1/16.
 */
public class JNotepadMM extends JFrame {
    private TextEditor textArea;
    private final String PLUGINS_PACKAGE = "hr.fer.zemris.app.plugins";

    private JNotepadMM() {
        final int WINDOW_WIDTH = 500;
        final int WINDOW_HEIGHT = 500;
        final int FRAME_X_POSITION = 100;
        final int FRAME_Y_POSITION = 100;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        setTitle("JNotepadMM " + Character.toString((char) 0x00a9) + " Martin Matak 2016");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(FRAME_X_POSITION, FRAME_Y_POSITION);
        initGUI();
    }

    private void initGUI() {
        getContentPane().setLayout(new BorderLayout());

        textArea = new TextEditor(
                new TextEditorModel(
                        "Ovo je super! \n " +
                                "Ovo je drugi red.. \n" +
                                " A ovo treci :)"
                )
        );
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(textArea, BorderLayout.CENTER);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        createActions();
        createMenus();
        createToolbar();
        getContentPane().add(new JStatusBar(textArea), BorderLayout.PAGE_END);
    }


    private void createToolbar() {
        JToolBar toolBar = new JToolBar("Tools");
        toolBar.setFloatable(true);

        toolBar.add(openAction);
        toolBar.add(saveAsAction);
        toolBar.addSeparator();

        toolBar.add(cutAction);
        toolBar.add(copyAction);
        toolBar.add(pasteAction);

        toolBar.addSeparator();

        toolBar.add(undoAction);
        toolBar.add(redoAction);

        getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        // FILE MENU
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(openAction);
        fileMenu.addSeparator();
        //fileMenu.add(saveAction);
        fileMenu.add(saveAsAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        menuBar.add(fileMenu);

        // EDIT MENU
        JMenu editMenu = new JMenu("Edit");
        editMenu.add(undoAction);
        editMenu.add(redoAction);
        editMenu.addSeparator();
        editMenu.add(cutAction);
        editMenu.add(copyAction);
        editMenu.add(pasteAction);
        editMenu.add(pasteAndTakeAction);
        editMenu.addSeparator();
        editMenu.add(deleteSelectionAction);
        editMenu.add(clearAction);
        editMenu.add(copyAction);
        menuBar.add(editMenu);

        //MOVE MENU
        JMenu moveMenu = new JMenu("Move");
        moveMenu.add(cursorStartAction);
        moveMenu.add(cursorEndAction);
        menuBar.add(moveMenu);

        //PLUGINS
        JMenu pluginsMenu = new JMenu("Plugins");
        try {
            Files.walk(Paths.get("./src/" + PLUGINS_PACKAGE.replace('.', '/'))).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    String fileName = filePath.getFileName().toString();
                    fileName = fileName.substring(0, fileName.indexOf('.'));
                    try {
                        IPlugin plugin = (IPlugin) Class.forName(PLUGINS_PACKAGE + "." +fileName).newInstance();
                        pluginsMenu.add(new AbstractAction(plugin.getName()) {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                plugin.execute(
                                        textArea.getModel(),
                                        UndoManager.getInstance(),
                                        textArea.getClipboardStack()
                                );
                            }
                        });
                    } catch (ClassNotFoundException e) {
                        System.out.println("Class " + fileName + " not found.");
                    } catch (InstantiationException interfaceIsIgnorable) {
                    } catch (IllegalAccessException e){
                        System.out.println("Illegal access to " + fileName);
                    }
                }
            });
        } catch (IOException e) {
            System.out.println("Error occurred while loading plugins..");
        }
        menuBar.add(pluginsMenu);

        this.setJMenuBar(menuBar);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JNotepadMM().setVisible(true));
    }

    /*
     * -----------------------------------------------------
	 * -------------- CREATING ACTIONS ---------------------
	 * -----------------------------------------------------
	 */

    private void createActions() {
        openAction = new OpenAction(this.textArea, this);
        saveAction = new SaveAction(this.textArea);
        saveAsAction = new SaveAsAction(this.textArea, this);
        cutAction = new CutAction(this.textArea);
        copyAction = new CopyAction(this.textArea);
        pasteAction = new PasteAction(this.textArea);
        undoAction = new UndoAction(this.textArea);
        redoAction = new RedoAction(this.textArea);
        exitAction = new ExitAction(this.textArea, this);
        pasteAndTakeAction = new PasteAndTakeAction(this.textArea);
        deleteSelectionAction = new DeleteSelectionAction(this.textArea);
        clearAction = new ClearAction(this.textArea);
        cursorStartAction = new CursorStartAction(this.textArea);
        cursorEndAction = new CursorEndAction(this.textArea);
    }

    private Action openAction;

    //TODO: Implement saveAction
    private Action saveAction;

    private Action saveAsAction;

    private Action cutAction;

    private Action copyAction;

    private Action pasteAction;

    private Action undoAction;

    private Action redoAction;

    private Action exitAction;

    private Action pasteAndTakeAction;

    private Action deleteSelectionAction;

    private Action clearAction;

    private Action cursorStartAction;

    private Action cursorEndAction;

    public void exit() {
        int result = JOptionPane.showConfirmDialog(this,
                "Do you want to save this file before exiting?",
                "Save before exit?", JOptionPane.YES_NO_CANCEL_OPTION);
        if (result == JOptionPane.CANCEL_OPTION) {
            return;
        }
        if (result == JOptionPane.YES_OPTION) {
            new SaveAsAction(textArea, this).actionPerformed(null);
        }
        dispose();
    }
}

package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.RedoStackObserver;
import hr.fer.zemris.app.TextEditor;
import hr.fer.zemris.app.UndoManager;
import hr.fer.zemris.app.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by mmatak on 6/3/16.
 */
public class RedoAction extends AbstractAction implements RedoStackObserver{
    private TextEditor editor;

    public RedoAction(TextEditor editor) {
        super("Redo");
        this.editor = editor;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Y"));
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Y);
        putValue(Action.SMALL_ICON, Icons.REDO_ICON);
        UndoManager.getInstance().addRedoStackObserver(this);
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        UndoManager.getInstance().redo();
    }

    @Override
    public void redoStackChanged(boolean empty) {
        setEnabled(!empty);
    }
}

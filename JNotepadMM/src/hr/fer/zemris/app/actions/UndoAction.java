package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.TextEditor;
import hr.fer.zemris.app.UndoManager;
import hr.fer.zemris.app.UndoStackObserver;
import hr.fer.zemris.app.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by mmatak on 6/3/16.
 */
public class UndoAction extends AbstractAction implements UndoStackObserver{
    private TextEditor editor;

    public UndoAction(TextEditor editor) {
        super("Undo");
        this.editor = editor;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Z"));
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Z);
        putValue(Action.SMALL_ICON, Icons.UNDO_ICON);
        UndoManager.getInstance().addUndoStackObserver(this);
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        UndoManager.getInstance().undo();
    }

    @Override
    public void undoStackChanged(boolean empty) {
        setEnabled(!empty);
    }
}

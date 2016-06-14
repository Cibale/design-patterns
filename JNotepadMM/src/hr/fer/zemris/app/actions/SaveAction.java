package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.TextEditor;
import hr.fer.zemris.app.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * TODO: Implement this action, abstract this and Save As action in another class
 * Created by mmatak on 6/3/16.
 */
public class SaveAction extends AbstractAction {
    private TextEditor editor;

    public SaveAction(TextEditor editor) {
        super("Save");
        this.editor = editor;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(Action.SMALL_ICON, Icons.SAVE_FILE_ICON);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.JNotepadMM;
import hr.fer.zemris.app.TextEditor;
import hr.fer.zemris.app.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Exit whole program.
 * <p>
 * TODO: Possible improvement: Add check if file is saved before quitting.
 * Created by mmatak on 6/3/16.
 */
public class ExitAction extends AbstractAction {
    JNotepadMM frame;
    public ExitAction(TextEditor textArea, JNotepadMM frame) {
        super("Exit");
        this.frame = frame;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
        putValue(Action.SMALL_ICON, Icons.EXIT_ICON);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.exit();
    }
}

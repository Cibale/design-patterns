package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.Location;
import hr.fer.zemris.app.LocationRange;
import hr.fer.zemris.app.TextEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by mmatak on 6/3/16.
 */
public class CursorStartAction extends AbstractAction {
    private TextEditor editor;

    public CursorStartAction(TextEditor editor) {
        super("Move cursor to the start");
        this.editor = editor;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("PAGE_UP"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.getModel().setCursorLocation(new Location());
        editor.getModel().setSelectionRange(new LocationRange(editor.getModel().getCursorLocation(), editor.getModel().getCursorLocation()));
        editor.getModel().cursorUpdated();
        editor.getModel().textUpdated();
    }
}

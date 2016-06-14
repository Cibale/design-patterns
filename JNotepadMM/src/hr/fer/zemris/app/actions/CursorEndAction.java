package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.Location;
import hr.fer.zemris.app.LocationRange;
import hr.fer.zemris.app.TextEditor;
import hr.fer.zemris.app.TextEditorModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Created by mmatak on 6/3/16.
 */
public class CursorEndAction extends AbstractAction {
    private TextEditor editor;
    public CursorEndAction(TextEditor editor) {
        super("Move cursor to the end");
        this.editor = editor;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("PAGE_DOWN"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TextEditorModel model = editor.getModel();
        ArrayList<String> lines = model.getLines();
        if (lines.isEmpty()){
            return;
        }
        String lastLine = lines.get(lines.size() - 1);
        Location endLocation = new Location(lines.size() - 1, lastLine.length());
        model.setCursorLocation(endLocation);
        model.setSelectionRange(new LocationRange(endLocation, endLocation));
        model.cursorUpdated();
    }
}

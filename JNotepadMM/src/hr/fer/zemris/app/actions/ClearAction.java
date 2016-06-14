package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Created by mmatak on 6/3/16.
 */
public class ClearAction extends AbstractAction implements TextObserver{
    private final TextEditor editor;

    public ClearAction(TextEditor editor) {
        super("Clear document");
        this.editor = editor;
        editor.getModel().addTextObserver(this);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TextEditorModel model = editor.getModel();
        ArrayList<String> lines = model.getLines();
        if (lines.isEmpty()){
            return;
        }
        String lastLine = lines.get(lines.size() - 1);
        model.setSelectionRange(new LocationRange(new Location(0, 0), new Location(lines.size() - 1, lastLine.length())));
        new DeleteSelectionAction(editor).actionPerformed(null);
    }

    @Override
    public void updateText() {
        setEnabled(!editor.getModel().isEmpty());
    }
}

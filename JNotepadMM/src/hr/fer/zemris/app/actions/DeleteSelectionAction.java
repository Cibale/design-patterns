package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.TextEditor;
import hr.fer.zemris.app.TextObserver;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by mmatak on 6/3/16.
 */
public class DeleteSelectionAction extends AbstractAction implements TextObserver{
    private TextEditor editor;
    public DeleteSelectionAction(TextEditor editor) {
        super("Delete selection");
        this.editor = editor;
        editor.getModel().addTextObserver(this);
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.getModel().deleteRange(editor.getModel().getSelectionRange());
    }

    @Override
    public void updateText() {
        setEnabled(!editor.getModel().getSelectionRange().isEmpty());
    }
}

package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.ClipboardStack;
import hr.fer.zemris.app.TextEditor;
import hr.fer.zemris.app.TextEditorModel;
import hr.fer.zemris.app.TextObserver;
import hr.fer.zemris.app.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by mmatak on 6/3/16.
 */
public class CutAction extends AbstractAction implements TextObserver {
    private TextEditor editor;

    public CutAction(TextEditor editor) {
        super("Cut");
        this.editor = editor;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
        putValue(Action.SMALL_ICON, Icons.CUT_ICON);
        editor.getModel().addTextObserver(this);
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TextEditorModel model = editor.getModel();
        String selectedText = model.getSelectedText();
        ClipboardStack clipboardStack = editor.getClipboardStack();

        if (!selectedText.isEmpty()) {
            clipboardStack.push(selectedText);
            model.deleteRange(model.getSelectionRange());
        }
    }

    @Override
    public void updateText() {
        setEnabled(!editor.getModel().getSelectionRange().isEmpty());
    }
}

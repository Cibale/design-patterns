package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.ClipboardObserver;
import hr.fer.zemris.app.ClipboardStack;
import hr.fer.zemris.app.TextEditor;
import hr.fer.zemris.app.TextEditorModel;
import hr.fer.zemris.app.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by mmatak on 6/3/16.
 */
public class PasteAndTakeAction extends AbstractAction implements ClipboardObserver{
    private final TextEditor editor;

    public PasteAndTakeAction(TextEditor editor) {
        super("Paste");
        this.editor = editor;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift V"));
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Z);
        putValue(Action.SMALL_ICON, Icons.PASTE_ICON);
        editor.getClipboardStack().addObserver(this);
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ClipboardStack clipboardStack = editor.getClipboardStack();
        TextEditorModel model = editor.getModel();
        if (!clipboardStack.isEmpty()) {
            model.insert(clipboardStack.pop());
        }
    }

    @Override
    public void updateClipboard() {
        setEnabled(!editor.getClipboardStack().isEmpty());
    }
}

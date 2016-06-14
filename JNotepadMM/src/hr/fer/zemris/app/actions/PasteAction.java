package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.*;
import hr.fer.zemris.app.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by mmatak on 6/3/16.
 */
public class PasteAction extends AbstractAction implements ClipboardObserver{
    private TextEditor editor;

    public PasteAction(TextEditor editor) {
        super("Paste");
        this.editor = editor;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
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
            model.insert(clipboardStack.peek());
        }
    }

    @Override
    public void updateClipboard() {
        setEnabled(!editor.getClipboardStack().isEmpty());
    }
}

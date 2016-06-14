package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.TextEditor;
import hr.fer.zemris.app.TextObserver;
import hr.fer.zemris.app.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by mmatak on 6/3/16.
 */
public class CopyAction extends AbstractAction implements TextObserver{
    private TextEditor editor;

    public CopyAction(TextEditor editor) {
        super("Copy");
        this.editor = editor;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
        putValue(Action.SMALL_ICON, Icons.COPY_ICON);
        editor.getModel().addTextObserver(this);
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedText = editor.getModel().getSelectedText();
        if (!selectedText.isEmpty()){
            editor.getClipboardStack().push(selectedText);
        }
    }

    @Override
    public void updateText() {
     setEnabled(!editor.getModel().getSelectionRange().isEmpty());
    }
}

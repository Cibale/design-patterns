package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.TextEditor;
import hr.fer.zemris.app.TextEditorModel;
import hr.fer.zemris.app.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by mmatak on 6/3/16.
 */
public class OpenAction extends AbstractAction {
    private TextEditor editor;
    private JFrame frame;

    public OpenAction(TextEditor editor, JFrame frame) {
        super("Open...");
        this.frame = frame;
        this.editor = editor;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
        putValue(Action.SMALL_ICON, Icons.OPEN_FILE_ICON);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open file");
        if (fc.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileName = fc.getSelectedFile();
        Path filePath = fileName.toPath();

        //TODO: Add check if same file is already opened

        if (!Files.isReadable(filePath)) {
            JOptionPane.showMessageDialog(frame,
                    "Error while reading",
                    "Error", JOptionPane.ERROR_MESSAGE);

        }

        byte[] data;
        try {
            data = Files.readAllBytes(filePath);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                    "Error while reading",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //TODO: Different charset ?
        new ClearAction(editor).actionPerformed(null);
        editor.getModel().newDocument(new String(data, StandardCharsets.UTF_8));
        editor.getClipboardStack().clear();
    }
}

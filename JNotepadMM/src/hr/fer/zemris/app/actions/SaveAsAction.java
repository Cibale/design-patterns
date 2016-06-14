package hr.fer.zemris.app.actions;

import hr.fer.zemris.app.TextEditor;
import hr.fer.zemris.app.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.awt.SystemColor.text;

/**
 * Created by mmatak on 6/3/16.
 * TODO: Must create "Save" action and abstract code from this and that action in new abstract save action.
 */
public class SaveAsAction extends AbstractAction {
    private TextEditor editor;
    private JFrame parentFrame;

    public SaveAsAction(TextEditor editor, JFrame parentFrame) {
        super("Save as...");
        this.editor = editor;
        this.parentFrame = parentFrame;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("shift control S"));
        putValue(Action.SMALL_ICON, Icons.SAVE_AS_ICON);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser() {
            private static final long serialVersionUID = 1L;

            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                if (f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(parentFrame,
                            "File you choosed already exists, are you sure you want to overwrite it?",
                            "File already exists..",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;
                        case JOptionPane.NO_OPTION:
                            return;
                        case JOptionPane.CLOSED_OPTION:
                            return;
                        case JOptionPane.CANCEL_OPTION:
                            cancelSelection();
                            return;
                    }
                }
                super.approveSelection();
            }
        };
        jfc.setDialogTitle("Save file as");
        if (jfc.showSaveDialog(parentFrame) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path savedFilePath = jfc.getSelectedFile().toPath();
        saveFile(savedFilePath);
    }

    private void saveFile(Path savedFilePath) {
        StringBuilder buff = new StringBuilder();
        for (String line : editor.getModel().getLines()){
            buff.append(line).append("\n");
        }
        //remove last "\n"'
        buff.deleteCharAt(buff.length()-1);

        //TODO: Add support for saving as in other charsets
        byte[] data = buff.toString().getBytes(StandardCharsets.UTF_8);

        try {
            Files.write(savedFilePath, data);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parentFrame, "File not saved!",
                    "Error occurred while saving. File is not saved.", JOptionPane.WARNING_MESSAGE);
        }
    }
}

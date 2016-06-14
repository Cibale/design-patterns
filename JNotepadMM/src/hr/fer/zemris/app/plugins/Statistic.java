package hr.fer.zemris.app.plugins;

import hr.fer.zemris.app.ClipboardStack;
import hr.fer.zemris.app.TextEditorModel;
import hr.fer.zemris.app.UndoManager;

import javax.swing.*;

/**
 * Created by mmatak on 6/5/16.
 */
public class Statistic implements IPlugin {
    @Override
    public String getName() {
        return "Statistic";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
        JOptionPane.showMessageDialog(null, "Total lines number: " + model.getLines().size(), "Total lines", JOptionPane.INFORMATION_MESSAGE);
    }
}

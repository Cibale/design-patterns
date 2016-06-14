package hr.fer.zemris.app.plugins;

import hr.fer.zemris.app.ClipboardStack;
import hr.fer.zemris.app.TextEditorModel;
import hr.fer.zemris.app.UndoManager;

import javax.swing.*;

/**
 * Interface for every plugin which wants to be in plugin menu bar.
 * If you want a new plugin, everything you need to do is
 * create instantiable class in this package which implements this interface.
 *
 * Created by mmatak on 6/5/16.
 */
public interface IPlugin {
    /** Plugin name for menu bar. */
    String getName();

    /** Short description of plugin. */
    String getDescription();

    void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack);
}

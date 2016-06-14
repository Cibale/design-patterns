package hr.fer.zemris.app.icons;

import hr.fer.zemris.app.JNotepadMM;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Icons class consists of public static Icon objects for fast retrieval of
 * icons for specific actions.
 *
 * @author mmatak
 */
public class Icons {
    public static final Icon OPEN_FILE_ICON = createIcon("icons/openFile.png");
    public static final Icon SAVE_FILE_ICON = createIcon("icons/saveFile.png");
    public static final Icon SAVE_AS_ICON = createIcon("icons/saveAs.png");
    public static final Icon CUT_ICON = createIcon("icons/cut.png");
    public static final Icon COPY_ICON = createIcon("icons/copy.png");
    public static final Icon PASTE_ICON = createIcon("icons/paste.png");
    public static final Icon UNDO_ICON = createIcon("icons/undo.png");
    public static final Icon REDO_ICON = createIcon("icons/redo.png");
    public static final Icon EXIT_ICON = createIcon("icons/redo.png");

    /**
     * Factory method for loading icons.
     *
     * @param path icon path
     * @return new instance of ImageIcon
     */
    private static Icon createIcon(String path) {
        return new ImageIcon(JNotepadMM.class.getResource(path));
    }
}

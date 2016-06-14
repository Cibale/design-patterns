package hr.fer.zemris.app.actions.model_actions;

import hr.fer.zemris.app.Location;
import hr.fer.zemris.app.TextEditorModel;

import java.util.ArrayList;

/**
 * Created by mmatak on 6/2/16.
 */
public class DeleteAfterAction implements IEditAction {
    private TextEditorModel model;
    private final int cursorColumn;
    private final int cursorRow;
    private char charDeleted;

    public DeleteAfterAction(TextEditorModel model) {
        this.model = model;
        cursorColumn = model.getCursorLocation().getColumn();
        cursorRow = model.getCursorLocation().getRow();
    }

    @Override
    public void executeDo() {
        ArrayList<String> lines = model.getLines();
        model.setCursorLocation(new Location(cursorRow, cursorColumn));
        //if most right column, get next row here and remove it from bottom
        if (cursorColumn == lines.get(cursorRow).length()) {
            charDeleted = '\n';
            lines.set(cursorRow, lines.get(cursorRow) + lines.get(cursorRow + 1));
            lines.remove(cursorRow + 1);
        } else {
            String line = lines.get(cursorRow);
            charDeleted = line.charAt(cursorColumn);
            //else only delete next character
            String lineBeforeCursor = line.substring(0, cursorColumn);
            String lineAfterCursor = "";
            //if not one before last character
            if (cursorColumn < line.length() - 1) {
                lineAfterCursor = line.substring(cursorColumn + 1, line.length());
            }
            lines.set(cursorRow, lineBeforeCursor + lineAfterCursor);
        }
        model.textUpdated();
    }

    @Override
    public void executeUndo() {
        model.setCursorLocation(new Location(cursorRow, cursorColumn));
        new InsertCharAction(model, charDeleted).executeDo();
        model.setCursorLocation(new Location(cursorRow, cursorColumn));
        model.cursorUpdated();
    }
}

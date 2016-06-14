package hr.fer.zemris.app.actions.model_actions;

import hr.fer.zemris.app.Location;
import hr.fer.zemris.app.LocationRange;
import hr.fer.zemris.app.TextEditorModel;
import hr.fer.zemris.app.actions.model_actions.DeleteAfterAction;
import hr.fer.zemris.app.actions.model_actions.DeleteRangeAction;
import hr.fer.zemris.app.actions.model_actions.IEditAction;

import java.util.ArrayList;

/**
 * Created by mmatak on 6/3/16.
 */
public class InsertCharAction implements IEditAction {
    private TextEditorModel model;
    private int cursorColumn;
    private int cursorRow;
    private char charInserted;
    private DeleteRangeAction deleteRangeAction;

    public InsertCharAction(TextEditorModel model, char c) {
        this.model = model;
        charInserted = c;
        cursorRow = model.getCursorLocation().getRow();
        cursorColumn = model.getCursorLocation().getColumn();
        deleteRangeAction = new DeleteRangeAction(model);
    }


    @Override
    public void executeDo() {
        model.setCursorLocation(new Location(cursorRow, cursorColumn));
        deleteRangeAction.executeDo();
        cursorColumn = model.getCursorLocation().getColumn();
        cursorRow = model.getCursorLocation().getRow();
        ArrayList<String> lines = model.getLines();

        String line = lines.get(cursorRow);
        String firstPart = line.substring(0, cursorColumn);
        String secondPart = line.substring(cursorColumn);
        //if ENTER pressed
        if (String.valueOf(charInserted).matches("\\n") || charInserted == '\n') {
            lines.set(cursorRow, firstPart);
            //O(n)
            lines.add(lines.get(lines.size() - 1));
            for (int i = lines.size() - 1; i > cursorRow; i--) {
                lines.set(i, lines.get(i - 1));
            }
            lines.set(cursorRow + 1, secondPart);
        } else {
            lines.set(cursorRow, firstPart + charInserted + secondPart);
        }
        //TODO: Add support for TAB
        model.moveCursorRight();
        model.setSelectionRange(new LocationRange(model.getCursorLocation(), model.getCursorLocation()));
        model.textUpdated();
        model.cursorUpdated();
    }

    @Override
    public void executeUndo() {
        model.setSelectionRange(new LocationRange(new Location(cursorRow, cursorColumn), new Location(cursorRow, cursorColumn)));
        model.setCursorLocation(new Location(cursorRow, cursorColumn));
        new DeleteAfterAction(model).executeDo();
        deleteRangeAction.executeUndo();
        model.textUpdated();
        model.cursorUpdated();
    }
}

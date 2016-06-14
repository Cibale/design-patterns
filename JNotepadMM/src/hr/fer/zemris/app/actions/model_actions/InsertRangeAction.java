package hr.fer.zemris.app.actions.model_actions;

import hr.fer.zemris.app.Location;
import hr.fer.zemris.app.LocationRange;
import hr.fer.zemris.app.TextEditorModel;

/**
 * Created by mmatak on 6/3/16.
 */
public class InsertRangeAction implements IEditAction {
    private TextEditorModel model;
    private final String text;
    private final Location cursorLocation;
    private Location cursorUndoLocation;

    public InsertRangeAction(TextEditorModel model, String text) {
        this.model = model;
        this.text = text;
        this.cursorLocation = model.getCursorLocation().copy();
    }

    @Override
    public void executeDo() {
        model.setCursorLocation(cursorLocation);
        for (char c : text.toCharArray()){
            new InsertCharAction(model,c).executeDo();
        }
        cursorUndoLocation = model.getCursorLocation().copy();
        model.textUpdated();
        model.cursorUpdated();
    }

    @Override
    public void executeUndo() {
        model.setCursorLocation(cursorUndoLocation);
        model.setSelectionRange(new LocationRange(cursorLocation, cursorUndoLocation));
        new DeleteRangeAction(model).executeDo();

    }
}

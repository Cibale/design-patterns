package hr.fer.zemris.app.actions.model_actions;

import hr.fer.zemris.app.Location;
import hr.fer.zemris.app.LocationRange;
import hr.fer.zemris.app.TextEditorModel;

/**
 * Created by mmatak on 6/3/16.
 */
public class DeleteRangeAction implements IEditAction {
    private TextEditorModel model;
    private LocationRange range;
    private String text;
    private final Location cursorLocation;

    public DeleteRangeAction(TextEditorModel textEditorModel) {
        this.model = textEditorModel;
        this.range = model.getSelectionRange().copy();
        this.text = model.getSelectedText();
        this.cursorLocation = model.getCursorLocation().copy();
    }

    @Override
    public void executeDo() {
        if (range.isEmpty()){
            return;
        }
        model.setCursorLocation(range.getStart().copy());
        DeleteAfterAction action = new DeleteAfterAction(model);
        for (int i = 0; i < text.length(); i++){
           action.executeDo();
        }
        model.setSelectionRange(new LocationRange(model.getCursorLocation(), model.getCursorLocation()));
        model.cursorUpdated();
        model.textUpdated();
    }

    @Override
    public void executeUndo() {
        if (range.isEmpty()){
            return;
        }
        model.setCursorLocation(range.getStart().copy());
        model.setSelectionRange(new LocationRange(model.getCursorLocation(), model.getCursorLocation()));
        IEditAction action = new InsertRangeAction(model, text);
        action.executeDo();
        model.setSelectionRange(range);
        model.setCursorLocation(cursorLocation);
        model.cursorUpdated();
        model.textUpdated();
    }
}

package hr.fer.zemris.app.actions.model_actions;

import hr.fer.zemris.app.Location;
import hr.fer.zemris.app.TextEditorModel;

import java.util.ArrayList;

/**
 * Created by mmatak on 6/2/16.
 */
public class DeleteBeforeAction implements IEditAction {
    private TextEditorModel model;
    private int cursorColumnDo;
    private int cursorRowDo;
    private int cursorColumnUndo;
    private int cursorRowUndo;
    private char charDeleted;

    public DeleteBeforeAction(TextEditorModel model) {
        this.model = model;
        cursorColumnDo = model.getCursorLocation().getColumn();
        cursorRowDo = model.getCursorLocation().getRow();
    }


    @Override
    public void executeDo() {
        model.setCursorLocation(new Location(cursorRowDo, cursorColumnDo));
        //if start of the line
        ArrayList<String> lines = model.getLines();
        if (cursorColumnDo == 0) {
            charDeleted = '\n';
            int nextCursorColumn = lines.get(cursorRowDo - 1).length();
            lines.set(cursorRowDo - 1,
                    lines.get(cursorRowDo - 1) + lines.get(cursorRowDo)
            );
            lines.remove(cursorRowDo);
            model.getCursorLocation().setColumn(nextCursorColumn);
            model.getCursorLocation().setRow(cursorRowDo - 1);
            cursorColumnUndo = nextCursorColumn;
            cursorRowUndo = cursorRowDo - 1;
        } else {
            String line = lines.get(cursorRowDo);
            charDeleted = line.charAt(cursorColumnDo-1);
            String newLineFirstPart = "";
            //if not one character only after start
            if (cursorColumnDo > 1) {
                newLineFirstPart = line.substring(0, cursorColumnDo - 1);
            }
            String newLineSecondPart = line.substring(cursorColumnDo);
            lines.set(cursorRowDo, newLineFirstPart + newLineSecondPart);
            model.getCursorLocation().setColumn(cursorColumnDo - 1);
            cursorColumnUndo = cursorColumnDo - 1;
            cursorRowUndo = cursorRowDo;
        }
        model.textUpdated();
        model.cursorUpdated();
    }

    @Override
    public void executeUndo() {
        model.setCursorLocation(new Location(cursorRowUndo, cursorColumnUndo));
        new InsertCharAction(model, charDeleted).executeDo();
        model.cursorUpdated();
    }
}

package hr.fer.zemris.app;

import hr.fer.zemris.app.actions.ClearAction;
import hr.fer.zemris.app.actions.model_actions.*;

import java.awt.datatransfer.Clipboard;
import java.util.*;

/**
 * Data model for hr.fer.zemris.app.JNotepadMM.
 * Created by mmatak on 6/1/16.
 */
public class TextEditorModel {
    /**
     * Lines which are shown in hr.fer.zemris.app.JNotepadMM.
     */
    private ArrayList<String> lines;
    /**
     * Currently selected area.
     */
    private LocationRange selectionRange;
    /**
     * Current cursor location.
     */
    private Location cursorLocation;
    /**
     * Observers which are notified when cursors' position changes.
     */
    private ArrayList<CursorObserver> cursorObservers;
    /**
     * Observers which are notified when text manipulation happens.
     */
    private ArrayList<TextObserver> textObservers;

    public TextEditorModel(String input) {
        Objects.requireNonNull(input);

        lines = new ArrayList<>(Arrays.asList(input.split("\\r?\\n")));
        cursorLocation = new Location();
        cursorObservers = new ArrayList<>();
        textObservers = new ArrayList<>();
        selectionRange = new LocationRange(new Location(), new Location());
    }

    /* -------- TEXT MANIPULATION METHODS ------ */

    /**
     * Deletes one character before cursor and sets cursor to new location.
     *
     * @return What it deleted
     */
    public void deleteBefore() {
        //if (0,0) do nothing
        if (cursorLocation.equals(new Location())) {
            return;
        }
        DeleteBeforeAction action = new DeleteBeforeAction(this);
        action.executeDo();
        UndoManager.getInstance().push(action);
    }

    /**
     * Deletes one character after cursor.
     */
    public void deleteAfter() {
        int cursorRow = cursorLocation.getRow();
        int cursorColumn = cursorLocation.getColumn();
        //if end of the last line - do nothing
        if (cursorRow == lines.size() - 1 &&
                cursorColumn == lines.get(cursorRow).length()) {
            return;
        }
        DeleteAfterAction action = new DeleteAfterAction(this);
        action.executeDo();
        UndoManager.getInstance().push(action);

    }

    /**
     * Deletes selected range (start - inclusive, end - exclusive),
     * updates text value and cursor position.
     * It is assumed that start is before end.
     *
     * @param range Range which needs to be removed.
     */
    public void deleteRange(LocationRange range) {
        Objects.requireNonNull(range);

        if (range.isEmpty()) {
            return;
        }

        DeleteRangeAction action = new DeleteRangeAction(this);
        action.executeDo();
        UndoManager.getInstance().push(action);
    }

    public void insert(char c) {
        if (((int) c >= 32 && (int) c < 127) ||
                (String.valueOf(c).matches("\\n")) ||
                (c == '\t')
                || Character.isAlphabetic(c)) {
            InsertCharAction action = new InsertCharAction(this, c);
            action.executeDo();
            UndoManager.getInstance().push(action);
        } else {
            return;
        }

    }

    /**
     * When new file is opened, call this method to set text appropriately.
     * @param text Text in new file
     */
    public void newDocument(String text){
        lines.clear();
        lines.add("");
        setSelectionRange(new LocationRange(new Location(), new Location()));
        setCursorLocation(new Location());
        insert(text);
        UndoManager.getInstance().clear();
    }
    public void insert(String text) {
        IEditAction action = new InsertRangeAction(this, text);
        action.executeDo();
        UndoManager.getInstance().push(action);
    }

    /* -------- GET ONE OR MORE LINES -------- */

    public Iterator<String> allLines() {
        return lines.iterator();
    }

    public String getLine(int index) {
        return lines.get(index);
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    /**
     * Iterator over range of lines between inclusive <code>index1</code>
     * and <code>incdex2</code>, exclusive.
     *
     * @param index1 Inclusive index.
     * @param index2 Exclusive index.
     * @return Iterator over lines between indexes.
     */
    public Iterator linesRange(int index1, int index2) {
        if (index1 >= index2) {
            throw new IllegalArgumentException("Index1 must be lower than index2");
        }
        if (index1 >= lines.size()) {
            throw new IndexOutOfBoundsException();
        }
        return new Iterator() {
            int currentIndex = index1;

            @Override
            public boolean hasNext() {
                return currentIndex < index2 && currentIndex < lines.size();
            }

            @Override
            public Object next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return lines.get(currentIndex++);
            }
        };
    }


    /* -------- MOVE CURSOR IN SOME DIRECTION ------- */

    public boolean moveCursorRight() {
        //if most bottom row and most right column, do nothing
        if (cursorLocation.getRow() == lines.size() - 1 &&
                cursorLocation.getColumn() == lines.get(cursorLocation.getRow()).length()) {
            return false;
        }
        //if most right column, go to next row at start of it
        if (cursorLocation.getColumn() == lines.get(cursorLocation.getRow()).length()) {
            cursorLocation.setColumn(0);
            cursorLocation.setRow(cursorLocation.getRow() + 1);
        } else {
            //else only move one right
            cursorLocation.setColumn(cursorLocation.getColumn() + 1);
        }
        cursorUpdated();
        return true;
    }

    public boolean moveCursorUp() {
        //if (0,0) do nothing
        if (cursorLocation.getRow() == 0 && cursorLocation.getColumn() == 0) {
            return false;
        }
        //if most upper row, move it to the start of the row
        if (cursorLocation.getRow() == 0) {
            cursorLocation.setColumn(0);
        } else {
            //else move row up or at the end of upper row
            int upperLineLength = lines.get(cursorLocation.getRow() - 1).length();
            int currentColumn = cursorLocation.getColumn();
            int nextColumn = Math.min(upperLineLength, currentColumn);
            cursorLocation.setRow(cursorLocation.getRow() - 1);
            cursorLocation.setColumn(nextColumn);
        }
        cursorUpdated();
        return true;
    }

    public boolean moveCursorDown() {
        //if most bottom row and most right column, do nothing
        if (cursorLocation.getRow() == lines.size() - 1 &&
                cursorLocation.getColumn() == lines.get(cursorLocation.getRow()).length()) {
            return false;
        }
        //if most bottom row, move it to the end of that row
        if (cursorLocation.getRow() == lines.size() - 1) {
            cursorLocation.setColumn(lines.get(cursorLocation.getRow()).length());
        } else {
            //move at the end of next row or stay at the same column in next row
            int bottomLineLength = lines.get(cursorLocation.getRow() + 1).length();
            int currentColumn = cursorLocation.getColumn();
            int nextColumn = Math.min(currentColumn, bottomLineLength);
            cursorLocation.setRow(cursorLocation.getRow() + 1);
            cursorLocation.setColumn(nextColumn);
        }
        cursorUpdated();
        return true;
    }

    public boolean moveCursorLeft() {
        //if (0,0) do nothing
        if (cursorLocation.getRow() == 0 && cursorLocation.getColumn() == 0) {
            return false;
        }
        //if max left, go row up and right
        if (cursorLocation.getColumn() == 0) {
            cursorLocation.setRow(cursorLocation.getRow() - 1);
            cursorLocation.setColumn(lines.get(cursorLocation.getRow()).length());
        } else {
            //else only move one left
            cursorLocation.setColumn(cursorLocation.getColumn() - 1);
        }
        cursorUpdated();
        return true;
    }


    /* ------- CURSOR OBSERVER METHODS ---------- */

    public void addCursorObserver(CursorObserver observer) {
        cursorObservers.add(observer);
    }

    public void removeCursorObserver(CursorObserver observer) {
        cursorObservers.remove(observer);
    }

    public void cursorUpdated() {
        //new collection so it is possible to auto deregister after N updates
        for (CursorObserver observer : new ArrayList<>(cursorObservers)) {
            observer.updateCursorLocation(cursorLocation);
        }
    }


    /* ------- TEXT OBSERVER METHODS ---------- */

    public void addTextObserver(TextObserver observer) {
        textObservers.add(observer);
    }

    public void removeTextObserver(TextObserver observer) {
        textObservers.remove(observer);
    }

    public void textUpdated() {
        for (TextObserver observer : new ArrayList<>(textObservers)) {
            observer.updateText();
        }
    }


    /* ------- GETTERS AND SETTERS ----------- */

    public LocationRange getSelectionRange() {
        return selectionRange;
    }

    public Location getCursorLocation() {
        return cursorLocation;
    }

    public void setCursorLocation(Location location) {
        if (location.getRow() >= lines.size() || location.getColumn() > lines.get(location.getRow()).length()){
            throw new IndexOutOfBoundsException();
        }
        cursorLocation = location.copy();
    }

    public void setSelectionRange(LocationRange selectionRange) {
        Objects.requireNonNull(selectionRange);
        this.selectionRange = selectionRange;
        textUpdated();
    }

    /**
     * @return Currently selected text or empty string if there is no selection.
     */
    public String getSelectedText() {
        if (selectionRange.isEmpty()) {
            return "";
        }
        Location start = selectionRange.getStart();
        Location end = selectionRange.getEnd();
        int startRow = start.getRow();
        int startColumn = start.getColumn();
        int endRow = end.getRow();
        int endColumn = end.getColumn();
        if (startRow == endRow) {
            return lines.get(startRow).substring(startColumn, endColumn);
        }
        StringBuilder buff = new StringBuilder();
        buff.append(lines.get(startRow).substring(startColumn)).append("\n");
        for (int i = startRow + 1; i < endRow; i++) {
            buff.append(lines.get(i)).append("\n");
        }
        buff.append(lines.get(endRow).substring(0, endColumn));
        return buff.toString();
    }

    public char getCharacterAt(Location location) {
        Objects.requireNonNull(location);
        return lines.get(location.getRow()).charAt(location.getColumn());
    }

    public boolean isEmpty() {
        if (lines.isEmpty()){
            return true;
        }
        if (lines.size() == 1 && lines.get(0).isEmpty()){
            return true;
        }
        return false;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
        textUpdated();
    }
}

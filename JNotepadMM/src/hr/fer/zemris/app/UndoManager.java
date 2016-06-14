package hr.fer.zemris.app;

import hr.fer.zemris.app.actions.model_actions.IEditAction;
import hr.fer.zemris.utils.MyStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager for undo and redo commands. It is implemented as singleton.
 * Created by mmatak on 6/2/16.
 */
public final class UndoManager {
    private final static UndoManager MY_INSTANCE = new UndoManager();
    private MyStack<IEditAction> undoStack;
    private MyStack<IEditAction> redoStack;
    private List<UndoStackObserver> undoStackObservers;
    private List<RedoStackObserver> redoStackObservers;

    public static UndoManager getInstance() {
        return MY_INSTANCE;
    }

    private UndoManager() {
        undoStack = new MyStack<>();
        redoStack = new MyStack<>();
        undoStackObservers = new ArrayList<>();
        redoStackObservers = new ArrayList<>();
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            return;
        }
        IEditAction action = undoStack.pop();
        action.executeUndo();
        notifyUndoStackObservers(undoStack.isEmpty());
        redoStack.push(action);
        notifyRedoStackObservers(false);
    }

    public void redo() {
        if (redoStack.isEmpty()){
            return;
        }
        IEditAction action = redoStack.pop();
        action.executeDo();
        undoStack.push(action);
        notifyUndoStackObservers(false);
        notifyRedoStackObservers(redoStack.isEmpty());
    }

    /** Should be called only when new model is set up. */
    public void clear(){
        undoStack = new MyStack<>();
        redoStack = new MyStack<>();
        notifyRedoStackObservers(true);
        notifyUndoStackObservers(true);
    }
    /**
     * Push new action on undo stack and remove redo stack.
     *
     * @param action Action which needs to be pushed on undo stack.
     */
    public void push(IEditAction action) {
        undoStack.push(action);
        redoStack = null;
        redoStack = new MyStack<>();
        notifyRedoStackObservers(true);
        notifyUndoStackObservers(false);
    }

    /* ------ UNDO and REDO methods for observer design pattern ------ */
    public void addUndoStackObserver(UndoStackObserver o) {
        undoStackObservers.add(o);
    }

    public void removeUndoStackObserver(UndoStackObserver o) {
        undoStackObservers.remove(o);
    }

    private void notifyUndoStackObservers(boolean empty) {
        for (UndoStackObserver o : new ArrayList<>(undoStackObservers)) {
            o.undoStackChanged(empty);
        }
    }

    public void addRedoStackObserver(RedoStackObserver o) {
        redoStackObservers.add(o);
    }

    public void removeRedoStackObserver(RedoStackObserver o) {
        redoStackObservers.remove(o);
    }

    private void notifyRedoStackObservers(boolean empty) {
        for (RedoStackObserver o : new ArrayList<>(redoStackObservers)) {
            o.redoStackChanged(empty);
        }
    }
}

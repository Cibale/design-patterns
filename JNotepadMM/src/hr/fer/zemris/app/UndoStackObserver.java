package hr.fer.zemris.app;

/**
 * Created by mmatak on 6/2/16.
 */
public interface UndoStackObserver {
    void undoStackChanged(boolean empty);
}

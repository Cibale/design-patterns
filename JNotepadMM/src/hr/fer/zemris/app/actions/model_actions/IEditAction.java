package hr.fer.zemris.app.actions.model_actions;

/**
 * Action which edits texts and supports do and undo commands.
 * Created by mmatak on 6/2/16.
 */
public interface IEditAction {
    /**
     * This happens when action is executed.
     */
    void executeDo();

    /**
     * This happens when undo is called on this action
     */
    void executeUndo();
}

package hr.fer.zemris.app;

/**
 * Observer in observer design pattern.
 * Everyone who cares when cursor is moved should implement this interface.
 * Created by mmatak on 6/1/16.
 */
public interface CursorObserver {
    /**
     * Method called when cursor changes its location.
     * @param location New location of cursor.
     */
    void updateCursorLocation(Location location);
}

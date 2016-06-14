package hr.fer.zemris.app;

/**
 * Observer for text manipulation in observer design pattern.
 * Created by mmatak on 6/1/16.
 */
public interface TextObserver {
    /**
     * Method called when observer needs to know that text is updated.
     **/
    void updateText();
}

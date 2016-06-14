package hr.fer.zemris.app;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mmatak on 6/5/16.
 */
public class JStatusBar extends JPanel implements TextObserver, CursorObserver{
    JLabel cursorRow;
    JLabel cursorColumn;
    JLabel numberOfLines;
    TextEditor editor;

    public JStatusBar(TextEditor editor){
        this.editor = editor;
        editor.getModel().addTextObserver(this);
        editor.getModel().addCursorObserver(this);
        Location cursorLocation = editor.getModel().getCursorLocation();
        cursorRow = new JLabel("Row: " + (cursorLocation.getRow() + 1));
        cursorColumn = new JLabel(" Column: " + (cursorLocation.getColumn() + 1));
        numberOfLines = new JLabel("Number of lines: " + editor.getModel().getLines().size());
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(cursorRow);
        this.add(cursorColumn);
        this.add(Box.createHorizontalGlue());
        this.add(numberOfLines);
        this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
    }

    @Override
    public void updateCursorLocation(Location location) {
        cursorRow.setText("Row: " + (location.getRow() + 1));
        cursorColumn.setText(" Column: " + (location.getColumn() + 1));
    }

    @Override
    public void updateText() {
        numberOfLines.setText("Number of lines: " + editor.getModel().getLines().size());
    }
}

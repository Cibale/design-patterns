package hr.fer.zemris.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Objects;

/**
 * Acts as a textarea.
 * Created by mmatak on 6/1/16.
 */
public class TextEditor extends JComponent {
    private TextEditorModel model;
    private ClipboardStack clipboardStack;

    public TextEditor(TextEditorModel model) {
        Objects.requireNonNull(model);

        this.model = model;
        this.clipboardStack = new ClipboardStack();

        model.addCursorObserver(location -> repaint());
        model.addTextObserver(() -> repaint());
        this.setFocusable(true);
        this.requestFocusInWindow();

        addListeners();

    }

    private void addListeners() {
        //move cursor, create selection
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                boolean changeRange = (e.getModifiers() & KeyEvent.SHIFT_MASK) != 0;
                LocationRange oldRange = null;
                if (changeRange) {
                    //if only shift
                    if (keyCode == KeyEvent.VK_SHIFT) {
                        return;
                    }
                    oldRange = model.getSelectionRange().copy();
                }
                Location oldCursorLocation = model.getCursorLocation().copy();
                if (moveCursor(keyCode)) {
                    updateSelection(oldCursorLocation, keyCode, oldRange, changeRange);
                }
            }
        });

        //delete
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode != KeyEvent.VK_DELETE) {
                    return;
                }
                if (model.getSelectionRange().isEmpty()) {
                    model.deleteAfter();
                } else {
                    model.deleteRange(model.getSelectionRange());
                }

            }
        });

        //backspace
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode != KeyEvent.VK_BACK_SPACE) {
                    return;
                }
                if (model.getSelectionRange().isEmpty()) {
                    model.deleteBefore();
                } else {
                    model.deleteRange(model.getSelectionRange());
                }
            }
        });

        //insert text
        //TODO: Add support for TAB and croatian letters
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                model.insert(e.getKeyChar());
            }
        });
    }


    private void updateSelection(Location oldCursorLocation, int keyCode, LocationRange oldRange, boolean changeRange) {
        Location cursorLocation = model.getCursorLocation();
        //start always left, end always right
        if (changeRange) {
            Location oldStart = oldRange.getStart();
            Location oldEnd = oldRange.getEnd();
            Location start = cursorLocation;
            Location end = cursorLocation;
            if (cursorLocation.compareTo(oldStart) < 0
                    && cursorLocation.compareTo(oldEnd) < 0) {
                start = cursorLocation;
                if (oldCursorLocation.equals(oldEnd)) {
                    end = oldRange.getStart();
                } else {
                    end = oldRange.getEnd();
                }
            } else if (cursorLocation.compareTo(oldStart) > 0
                    && cursorLocation.compareTo(oldEnd) < 0) {
                if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_UP) {
                    //went left or up
                    start = oldStart;
                    end = cursorLocation;
                } else {
                    //went right or down
                    start = cursorLocation;
                    end = oldEnd;
                }
            } else if (cursorLocation.compareTo(oldStart) > 0
                    && cursorLocation.compareTo(oldEnd) > 0) {
                //fixing left left down bug
                if (oldCursorLocation.equals(oldStart)) {
                    start = oldEnd;
                    end = cursorLocation;
                } else {
                    start = oldStart;
                    end = cursorLocation;
                }
            }
            model.setSelectionRange(new LocationRange(start, end));
        } else {
            //make range empty
            model.setSelectionRange(
                    new LocationRange(cursorLocation, cursorLocation)
            );
        }
    }


    private boolean moveCursor(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                return model.moveCursorUp();
            case KeyEvent.VK_DOWN:
                return model.moveCursorDown();
            case KeyEvent.VK_LEFT:
                return model.moveCursorLeft();
            case KeyEvent.VK_RIGHT:
                return model.moveCursorRight();
            default:
                return false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Insets ins = getInsets();
        Dimension dim = getSize();
        Rectangle r = new Rectangle(
                ins.left + getX(),
                ins.top + getY(),
                dim.width - ins.left - ins.right,
                dim.height - ins.top - ins.bottom
        );

        if (isOpaque()) {
            g2d.setColor(getBackground());
            g2d.fillRect(r.x, r.y, r.width, r.height);
        }


        FontMetrics fm = getFontMetrics(getFont());

        if (!model.getSelectionRange().isEmpty()) {
            //print selection background
            g2d.setColor(Color.YELLOW);
            Location start = model.getSelectionRange().getStart();
            Location end = model.getSelectionRange().getEnd();
            int startRow = start.getRow();
            int startColumn = start.getColumn();
            int endRow = end.getRow();
            int endColumn = end.getColumn();
            //first row
            String line = model.getLine(startRow);
            int x = fm.charsWidth(line.toCharArray(), 0, startColumn);
            int y = startRow * fm.getHeight() + fm.getDescent();
            int height = fm.getHeight() + fm.getDescent();
            if (startRow == endRow) {
                int width = fm.stringWidth(line.substring(startColumn, endColumn));
                g2d.fillRect(r.x + x, r.y + y, width, height);
            } else {
                //paint only part of the first row
                int width = fm.stringWidth(line.substring(startColumn));
                g2d.fillRect(r.x + x, r.y + y, width, height);
                //paint whole line every line before the last one
                for (int i = startRow + 1; i < endRow; i++) {
                    width = fm.stringWidth(model.getLine(i));
                    g2d.fillRect(r.x, r.y + i * fm.getHeight(), width, height);
                }
                //paint only part of the last row
                y = endRow * fm.getHeight() + fm.getDescent();
                width = fm.stringWidth(model.getLine(endRow).substring(0, endColumn));
                g2d.fillRect(r.x, r.y + y, width, height);
            }
        }
        //print text
        g2d.setColor(Color.BLACK);
        Iterator<String> iterator = model.allLines();
        int counter = 0;
        while (iterator.hasNext()) {
            String line = iterator.next();
            g2d.drawString(line, r.x, r.y + (1 + counter++) * fm.getHeight());
        }

        //draw cursor
        g2d.setColor(Color.BLACK);
        Location cursor = model.getCursorLocation();
        int cursorRow = cursor.getRow();
        int cursorColumn = cursor.getColumn();
        //System.out.println("Row: " + cursorRow + ", column: " + cursorColumn);
        int stringWidth = fm.charsWidth(model.getLine(cursorRow).toCharArray(), 0, cursorColumn);
        g2d.drawLine(
                r.x + stringWidth,
                r.y + cursor.getRow() * fm.getHeight(),
                r.x + stringWidth,
                r.y + (cursor.getRow() + 1) * fm.getHeight()
        );
        //focus because of key adapter
        this.requestFocusInWindow();
    }

    //not so safe..

    public ClipboardStack getClipboardStack() {
        return clipboardStack;
    }

    public TextEditorModel getModel() {
        return model;
    }

    public void setModel(TextEditorModel model) {
        this.model = model;
        model.textUpdated();
        model.cursorUpdated();
        clipboardStack.clear();
    }
}

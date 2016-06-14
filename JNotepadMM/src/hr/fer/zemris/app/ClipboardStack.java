package hr.fer.zemris.app;

import hr.fer.zemris.utils.MyStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Stack used for copy-paste actions.
 * Created by mmatak on 6/2/16.
 */
public class ClipboardStack {
    private MyStack<String> texts;
    private List<ClipboardObserver> observerList;

    public ClipboardStack() {
        texts = new MyStack<>();
        observerList = new ArrayList<>();
    }

    public String pop() {
        String text =  texts.pop();
        notifyObservers();
        return text;
    }

    public String peek() {
        return texts.peek();
    }

    public void push(String item) {
        texts.push(item);
        notifyObservers();
    }

    public boolean isEmpty() {
        return texts.isEmpty();
    }

    public void clear() {
        texts = null;
        texts = new MyStack<>();
        notifyObservers();
    }

    public void addObserver(ClipboardObserver o) {
        observerList.add(o);
    }

    public void removeObserver(ClipboardObserver o) {
        observerList.remove(o);
    }

    public void notifyObservers() {
        for (ClipboardObserver o : new ArrayList<>(observerList)) {
            o.updateClipboard();
        }
    }
}

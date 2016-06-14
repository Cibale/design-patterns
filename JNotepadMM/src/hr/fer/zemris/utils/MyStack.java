package hr.fer.zemris.utils;

import java.util.NoSuchElementException;

/**
 * The <tt>MyStack</tt></tt> class represents a last-in-first-out (LIFO) stack
 * of generic items.
 * It supports the usual <em>push</em> and <em>pop</em> operations, along with methods
 * for peeking at the top item and testing if the stack is empty.
 * <p>
 * This implementation uses a resizing array, which double the underlying array
 * when it is full and halves the underlying array when it is one-quarter full.
 * The <em>push</em> and <em>pop</em> operations take constant amortized time.
 * The <em>size</em>, <em>peek</em>, and <em>is-empty</em> operations takes
 * constant time in the worst case.
 * Created by mmatak on 6/2/16.
 */
public class MyStack<Item> {
    private Item[] a;         // array of items
    private int N;            // number of elements on stack


    /**
     * Initializes an empty stack.
     */
    public MyStack() {
        a = (Item[]) new Object[2];
        N = 0;
    }

    /**
     * Is this stack empty?
     *
     * @return true if this stack is empty; false otherwise
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Returns the number of items in the stack.
     *
     * @return the number of items in the stack
     */
    public int size() {
        return N;
    }


    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    /**
     * Adds the item to this stack.
     *
     * @param item the item to add
     */
    public void push(Item item) {
        if (N == a.length) resize(2 * a.length);    // double size of array if necessary
        a[N++] = item;                            // add item
    }

    /**
     * Removes and returns the item most recently added to this stack.
     *
     * @return the item most recently added
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    public Item pop() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = a[N - 1];
        a[N - 1] = null;                              // to avoid loitering
        N--;
        // shrink size of array if necessary
        if (N > 0 && N == a.length / 4) resize(a.length / 2);
        return item;
    }


    /**
     * Returns (but does not remove) the item most recently added to this stack.
     *
     * @return the item most recently added to this stack
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        return a[N - 1];
    }
}

package hr.fer.zemris.app;

import java.util.Objects;

/**
 * Range defined by location of start (inclusive) and location of end (exclusive) of selection.
 * Created by mmatak on 6/1/16.
 */
public class LocationRange {
    private Location start;
    private Location end;

    public LocationRange(Location start, Location end) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);

        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "hr.fer.zemris.app.LocationRange{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    public boolean isEmpty(){
        return start.equals(end);
    }

    /**
     * Performs a hard copy.
     * @return New instance of <code>hr.fer.zemris.app.LocationRange</code> with same values as this.
     */
    public LocationRange copy(){
        return new LocationRange(start.copy(), end.copy());
    }
    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getEnd() {
        return end;
    }

    public void setEnd(Location end) {
        this.end = end;
    }
}

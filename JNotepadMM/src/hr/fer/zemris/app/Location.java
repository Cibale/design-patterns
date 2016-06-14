package hr.fer.zemris.app;

/**
 * Location is defined by row and column TextEditor.
 * Created by mmatak on 6/1/16.
 */
public class Location implements Comparable<Location>{
    private int row;
    private int column;

    public Location(int row, int column) {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Row and column must be non-negative numbers.");
        }
        this.row = row;
        this.column = column;
    }

    /**
     * Default constructor at (0,0).
     */
    public Location() {
        this(0, 0);
    }

    /** Performs a hard copy */
    public Location copy(){
        Location loc = new Location();
        loc.row = this.row;
        loc.column = this.column;
        return loc;
    }
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "hr.fer.zemris.app.Location{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (row != location.row) return false;
        return column == location.column;

    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }

    @Override

    public int compareTo(Location o) {
        if (row != o.getRow()){
            return Integer.compare(row, o.getRow());
        } else {
            return Integer.compare(column, o.getColumn());
        }
    }


}

package polimi.ingsw.model;

import java.io.Serializable;

/**
 * Represents a cell with row and column coordinates.
 */
public class Cell implements Serializable{

    private final int row;
    private final int column;

    /**
     * Constructs a Cell object with specified row and column coordinates.
     *
     * @param row    the row coordinate of the cell
     * @param column the column coordinate of the cell
     */

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * @return the row coordinate
     */
    public int getRow(){
        return this.row;
    }

    /**
     * @return the column coordinate
     */
    public int getColumn(){
        return this.column;
    }

    /**
     * Checks if this Cell object is equal to another object.
     * Two cells are considered equal if they have the same row and column coordinates.
     *
     * @param o the object to compare with this Cell
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && column == cell.column;
    }

    /**
     * Returns a string representation of the Cell object.
     * The format is "{row, column}".
     *
     * @return a string representation of the Cell object
     */
    @Override
    public String toString() {
        return "{" + row +
                ", " + column +
                '}';
    }
}

package cz.cvut.fel.pjv.arimaa.model;

public class Coords {
    private int row;
    private int col;

    public Coords(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "Pull position: " + row + ", " + col;
    }
}

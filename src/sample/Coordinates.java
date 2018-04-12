package sample;

public class Coordinates {
    private int row, col;

    public Coordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Coordinates() {
        this.row = -1;
        this.col = -1;
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
        return "(" + this.row + ", " + this.col + ")";
    }
}

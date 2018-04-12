public class Cell {
    private Coordinates coordinates;
    private char symbol;
    boolean isOccupied;

    public Cell(Coordinates coordinates, char symbol, boolean isOccupied) {
        this.coordinates = coordinates;
        this.symbol = symbol;
        this.isOccupied = isOccupied;
    }

    public Cell() {
        this.coordinates = new Coordinates();
        this.symbol = ' ';
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    @Override
    public String toString() {
        return String.valueOf(this.symbol);
    }

}

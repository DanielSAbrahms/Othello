public class CellValContainer {
    private Coordinates coordinates;
    private int value;

    public CellValContainer(Coordinates coordinates, int value) {
        this.coordinates = coordinates;
        this.value = value;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

package aaron.model.spatial;

import java.io.Serializable;
import java.util.Arrays;

public class Coordinate implements Serializable {

    private final double[] coordinate;

    public Coordinate(double... coordinate) {
        if (coordinate.length < 2) {
            throw new IllegalArgumentException("A coordinate must have at least two elements");
        }
        this.coordinate = coordinate;
    }

    public double[] getCoordinate() {
        return coordinate;
    }

    public double[] getCoordinateCopy() {
        return Arrays.copyOf(coordinate, coordinate.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Coordinate that = (Coordinate) o;

        return Arrays.equals(coordinate, that.coordinate);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coordinate);
    }
}

package aaron.model.spatial;

import org.neo4j.graphdb.spatial.Point;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class WGS84Point implements Point, Serializable {

    private final Coordinate coordinate;
    private final CRS crs;

    public WGS84Point(double[] coordinate) {
        this.coordinate = new Coordinate(coordinate);
        this.crs = new CRS(4326, "WGS-84", "https://spatialreference.org/ref/epsg/4326/");
    }

    @Override
    public org.neo4j.graphdb.spatial.Coordinate getCoordinate() {
        return new org.neo4j.graphdb.spatial.Coordinate(this.coordinate.getCoordinate());
    }

    @Override
    public List<org.neo4j.graphdb.spatial.Coordinate> getCoordinates() {
        return List.of(new org.neo4j.graphdb.spatial.Coordinate(this.coordinate.getCoordinate()));
    }

    @Override
    public org.neo4j.graphdb.spatial.CRS getCRS() {
        return new CRSProxy(this.crs);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WGS84Point that = (WGS84Point) o;
        return Objects.equals(coordinate, that.coordinate) && Objects.equals(crs, that.crs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate, crs);
    }

    public static class CRSProxy implements org.neo4j.graphdb.spatial.CRS {

        private final CRS crs;

        public CRSProxy(CRS crs) {
            this.crs = crs;
        }

        @Override
        public int getCode() {
            return crs.getCode();
        }

        @Override
        public String getType() {
            return crs.getType();
        }

        @Override
        public String getHref() {
            return crs.getHref();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            CRSProxy crsProxy = (CRSProxy) o;
            return Objects.equals(crs, crsProxy.crs);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(crs);
        }
    }
}

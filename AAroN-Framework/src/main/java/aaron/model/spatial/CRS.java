package aaron.model.spatial;

import java.io.Serializable;
import java.util.Objects;

public class CRS implements Serializable {

    private final int code;
    private final String type;
    private final String href;

    public CRS(int code, String type, String href) {
        this.code = code;
        this.type = type;
        this.href = href;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getHref() {
        return href;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CRS crs = (CRS) o;
        return code == crs.code && Objects.equals(type, crs.type) && Objects.equals(href, crs.href);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, type, href);
    }
}

package aaron.model.spatial;

import java.io.Serializable;

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
}

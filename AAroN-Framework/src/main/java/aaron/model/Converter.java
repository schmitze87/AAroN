package aaron.model;

import java.io.IOException;

public interface Converter {

    Model convert() throws IOException;
}

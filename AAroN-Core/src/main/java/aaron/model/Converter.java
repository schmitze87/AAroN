package aaron.model;

import java.io.File;
import java.io.IOException;

public interface Converter {

    Model convert(final File file) throws IOException;
}

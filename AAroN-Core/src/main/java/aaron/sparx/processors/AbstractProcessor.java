package aaron.sparx.processors;

import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.model.Processor;

import java.time.LocalDateTime;
import java.util.Map;

public abstract class AbstractProcessor implements Processor<Map<String, Object>> {

    protected final Model model;
    protected final String sha1;
    protected final LocalDateTime time;
    protected final ImportConext context;

    protected AbstractProcessor(final String sha1, final LocalDateTime time, final Model model, final ImportConext context) {
        this.model = model;
        this.sha1 = sha1;
        this.time = time;
        this.context = context;
    }
}

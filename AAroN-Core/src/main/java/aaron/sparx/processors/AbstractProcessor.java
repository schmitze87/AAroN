package aaron.sparx.processors;

import aaron.logging.Logger;
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
    protected final Logger logger;

    protected AbstractProcessor(final String sha1, final LocalDateTime time, final Model model, final ImportConext context, Logger logger) {
        this.model = model;
        this.sha1 = sha1;
        this.time = time;
        this.context = context;
        this.logger = logger;
    }
}

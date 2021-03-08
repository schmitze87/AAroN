package aaron.sparx.processors;

import aaron.model.Model;

import java.time.LocalDateTime;

public abstract class AbstractProcessor implements Processor{

    protected final Model model;
    protected final String sha1;
    protected final LocalDateTime time;

    public AbstractProcessor(final String sha1, final LocalDateTime time, final Model model) {
        this.model = model;
        this.sha1 = sha1;
        this.time = time;
    }
}

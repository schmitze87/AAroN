package aaron.sparx;

import aaron.model.Converter;
import aaron.model.Model;
import aaron.model.Processor;
import aaron.sparx.processors.*;

import java.time.LocalDateTime;
import java.util.Map;

public abstract class AbstractSparxConverter implements Converter {

    protected Model model;
    protected Config config;

    protected AbstractSparxConverter(Model model, Config config) {
        this.model = model;
        this.config = config;
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processObjectConstraints(
            final String sha1, final LocalDateTime time, final T table) {
        ObjectConstraintProcessor processor = new ObjectConstraintProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processAttributeConstraints(
            final String sha1, final LocalDateTime time, final T table) {
        AttributeConstraintProcessor processor = new AttributeConstraintProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processConnectorConstraints(
            final String sha1, final LocalDateTime time, final T table) {
        ConnectorConstraintProcessor processor = new ConnectorConstraintProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processPackages(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new PackageProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processObjects(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new ObjectProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processConnectors(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new ConnectorProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processConnectorTags(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new ConnectorTagProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processDiagrams(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new DiagramProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processXRefs(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new XRefProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processObjectProperties(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new ObjectPropertiesProcessor(sha1, time, model, config.getTaggedValueMode());
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processOperations(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new OperationProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processOperationTags(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new OperationTagProcessor(sha1, time, model, config.getTaggedValueMode());
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processOperationParams(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new OperationParamsProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processAttributes(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new AttributeProcessor(sha1, time, model);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processAttributeTags(
            final String sha1, final LocalDateTime time, final T table) {
        Processor processor = new AttributeTagProcessor(sha1, time, model, config.getTaggedValueMode());
        table.forEach(processor::process);
    }
}

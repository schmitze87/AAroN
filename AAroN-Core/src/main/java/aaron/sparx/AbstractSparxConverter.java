package aaron.sparx;

import aaron.model.Converter;
import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.model.Processor;
import aaron.sparx.processors.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Stream.generate;

public abstract class AbstractSparxConverter implements Converter {

    protected Model model;
    protected Config config;

    protected ImportConext context;

    protected AbstractSparxConverter(Model model, Config config) {
        this.model = model;
        this.config = config;
        this.context = new ImportConext();
        this.model.setContext(this.context);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processSystem(
            final String sha1, final LocalDateTime time, final T table) {
        SystemProcessor processor = new SystemProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processObjectConstraints(
            final String sha1, final LocalDateTime time, final T table) {
        ObjectConstraintProcessor processor = new ObjectConstraintProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processAttributeConstraints(
            final String sha1, final LocalDateTime time, final T table) {
        AttributeConstraintProcessor processor = new AttributeConstraintProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processConnectorConstraints(
            final String sha1, final LocalDateTime time, final T table) {
        ConnectorConstraintProcessor processor = new ConnectorConstraintProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processPackages(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new PackageProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processObjects(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new ObjectProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processConnectors(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new ConnectorProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processConnectorTags(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new ConnectorTagProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processDiagrams(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new DiagramProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processDiagramObjects(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new DiagramObjectsProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processDiagramLinks(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new DiagramLinksProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processXRefs(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new XRefProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processObjectProperties(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new ObjectPropertiesProcessor(sha1, time, model, context, config.getTaggedValueMode());
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processOperations(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new OperationProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processOperationTags(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new OperationTagProcessor(sha1, time, model, context, config.getTaggedValueMode());
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processOperationParams(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new OperationParamsProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processAttributes(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new AttributeProcessor(sha1, time, model, context);
        table.forEach(processor::process);
    }

    protected <T extends Iterable<U>, U extends Map<String, Object>> void processAttributeTags(
            final String sha1, final LocalDateTime time, final T table) {
        Processor<Map<String, Object>> processor = new AttributeTagProcessor(sha1, time, model, context, config.getTaggedValueMode());
        table.forEach(processor::process);
    }

    void handleTable(String sha1, LocalDateTime now, Connection connection, ProcessInterface processInterface, String sql) throws SQLException {
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            processInterface.process(sha1, now, handleResultSet(resultSet));
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ignored) {
                } // ignore
                resultSet = null;
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignored) {
                } // ignore
                statement = null;
            }
        }
    }

    Iterable<Map<String, Object>> handleResultSet(ResultSet rs) throws SQLException {
        return new SparxTableIterator(rs);
    }

    public class SparxTableIterator implements Iterator<Map<String, Object>>, Iterable<Map<String, Object>> {

        ResultSet rs;
        ResultSetMetaData metaData;
        int columnCount;
        int index = 0;

        public SparxTableIterator(ResultSet rs) throws SQLException {
            this.rs = rs;
            this. metaData = rs.getMetaData();
            this. columnCount = metaData.getColumnCount();
        }

        @Override
        public boolean hasNext() {
            try {
                return rs.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Map<String, Object> next() {
            Map<String, Object> rowData = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                // toLowerCase to handle case insensitivity of column names in different DB schema
                try {
                    rowData.put(metaData.getColumnName(i).toLowerCase(Locale.ROOT), rs.getObject(i));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            index++;
            return rowData;
        }

        @Override
        public Iterator<Map<String, Object>> iterator() {
            return this;
        }
    }

    @FunctionalInterface
    interface ProcessInterface {
        <T extends Iterable<U>, U extends Map<String, Object>> void process(final String sha1, final LocalDateTime time, final T table);
    }
}

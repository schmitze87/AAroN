package aaron.plugin.export;

import aaron.export.AAroNCsvWriter;
import aaron.model.AAroNEdge;
import aaron.model.AAroNNode;
import aaron.model.Model;
import aaron.model.WithProperties;
import aaron.util.Util;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import static aaron.model.PropertyType.*;

public class Exporter {

    @Context
    public Transaction tx;

    @Context
    public GraphDatabaseService db;

    @Context
    public Log log;

    public Exporter() {

    }

    public static class ExportOutput {
        public long nodesCount;
        public long edgesCount;
        public String nodesFile;
        public String edgesFile;
    }

    @Procedure(name = "aaron.export.csv", mode = Mode.READ)
    @Description("Export the whole Neo4j graph db to Neo4j's CSV header format.")
    public Stream<ExportOutput> exportCSV(@Name("csvFileNamePrefix") String fileName, @Name(value = "config", defaultValue = "{}") Map<String, Object> configMap) {
        String importFolderStr;
        try (Transaction transaction = db.beginTx()) {
            importFolderStr = Util.getImportFolder(transaction);
        }
        boolean parenthesesFix = (boolean) configMap.getOrDefault("parenthesesFix", false);
        Path importFolder = Path.of(importFolderStr);
        Path nodesFile = importFolder.resolve(fileName + "_nodes.csv");
        Path edgesFile = importFolder.resolve(fileName + "_edges.csv");
        Model model = new Model();
        tx.getAllNodes().forEach(node -> {
            long id = node.getId();
            AAroNNode.Builder builder = AAroNNode.builder();
            node.getLabels().forEach(label -> {
                builder.addLabel(label.name());
            });
            AAroNNode aaronNode = builder.build();
            node.getAllProperties().forEach((key, value) -> {
                addProperty(aaronNode, key, value);
            });
            model.addNode(new Neo4jNodeIdentifier(id), aaronNode);
        });
        tx.getAllRelationships().forEach(relationship -> {
            long id = relationship.getId();
            AAroNEdge.Builder builder = AAroNEdge.builder();
            builder.setType(relationship.getType().name());
            builder.setStart(new Neo4jNodeIdentifier(relationship.getStartNodeId()));
            builder.setEnd(new Neo4jNodeIdentifier(relationship.getEndNodeId()));
            AAroNEdge aaronEdge = builder.build();
            relationship.getAllProperties().forEach((key, value) -> {
                addProperty(aaronEdge, key, value);
            });
            model.addEdge(new Neo4jEdgeIdentifier(id), aaronEdge);
        });
        try {
            AAroNCsvWriter.write(model, nodesFile.toFile(), edgesFile.toFile(), parenthesesFix);
        } catch (IOException e) {
            log.error("Could not write CSV files", e);
            throw new RuntimeException(e);
        }
        ExportOutput output = new ExportOutput();
        output.nodesCount = model.getNodes().size();
        output.edgesCount = model.getEdges().size();
        output.nodesFile = nodesFile.toFile().toString();
        output.edgesFile = edgesFile.toFile().toString();
        return Stream.of(output);
    }

    private void addProperty(WithProperties aaronObject, String key, Object value) {
        Class<?> valueClass = value.getClass();
        switch (valueClass.getSimpleName()) {
            case "Boolean":
                aaronObject.addProperty(key, BOOLEAN, BOOLEAN.cast(value));
                break;
            case "boolean[]":
                aaronObject.addProperty(key, BOOLEAN_ARRAY, BOOLEAN_ARRAY.cast(value));
                break;
            case "Byte":
                aaronObject.addProperty(key, BYTE, BYTE.cast(value));
                break;
            case "byte[]":
                aaronObject.addProperty(key, BYTE_ARRAY, BYTE_ARRAY.cast(value));
                break;
            case "Short":
                aaronObject.addProperty(key, SHORT, SHORT.cast(value));
                break;
            case "short[]":
                aaronObject.addProperty(key, SHORT_ARRAY, SHORT_ARRAY.cast(value));
                break;
            case "Integer":
                aaronObject.addProperty(key, INTEGER, INTEGER.cast(value));
                break;
            case "int[]":
                aaronObject.addProperty(key, INTEGER_ARRAY, INTEGER_ARRAY.cast(value));
                break;
            case "Long":
                aaronObject.addProperty(key, LONG, LONG.cast(value));
                break;
            case "long[]":
                aaronObject.addProperty(key, LONG_ARRAY, LONG_ARRAY.cast(value));
                break;
            case "Float":
                aaronObject.addProperty(key, FLOAT, FLOAT.cast(value));
                break;
            case "float[]":
                aaronObject.addProperty(key, FLOAT_ARRAY, FLOAT_ARRAY.cast(value));
                break;
            case "Double":
                aaronObject.addProperty(key, DOUBLE, DOUBLE.cast(value));
                break;
            case "double[]":
                aaronObject.addProperty(key, DOUBLE_ARRAY, DOUBLE_ARRAY.cast(value));
                break;
            case "Character":
                aaronObject.addProperty(key, CHAR, CHAR.cast(value));
                break;
            case "char[]":
                aaronObject.addProperty(key, CHAR_ARRAY, CHAR_ARRAY.cast(value));
                break;
            case "String":
                aaronObject.addProperty(key, STRING, STRING.cast(value));
                break;
            case "String[]":
                aaronObject.addProperty(key, STRING_ARRAY, STRING_ARRAY.cast(value));
                break;
            case "Point":
                aaronObject.addProperty(key, POINT, POINT.cast(value));
                break;
            case "Point[]":
                aaronObject.addProperty(key, POINT_ARRAY, POINT_ARRAY.cast(value));
                break;
            case "LocalDate":
                aaronObject.addProperty(key, LOCALDATE, LOCALDATE.cast(value));
                break;
            case "LocalDate[]":
                aaronObject.addProperty(key, LOCALDATE_ARRAY, LOCALDATE_ARRAY.cast(value));
                break;
            case "OffsetTime":
                OffsetDateTime offsetDateTime = (OffsetDateTime) value;
                LocalTime localTime = LocalTime.from(offsetDateTime);
                aaronObject.addProperty(key, LOCALTIME, localTime);
                break;
            case "OffsetTime[]":
                OffsetDateTime[] offsetDateTimeArray = (OffsetDateTime[]) value;
                LocalTime[] localTimeArray = Arrays.stream(offsetDateTimeArray).map(LocalTime::from).toArray(LocalTime[]::new);
                aaronObject.addProperty(key, LOCALTIME_ARRAY, localTimeArray);
                break;
            case "LocalTime":
                aaronObject.addProperty(key, LOCALTIME, LOCALTIME.cast(value));
                break;
            case "LocalTime[]":
                aaronObject.addProperty(key, LOCALTIME_ARRAY, LOCALTIME_ARRAY.cast(value));
                break;
            case "ZonedDateTime":
                ZonedDateTime zonedDateTime = (ZonedDateTime) value;
                LocalDateTime localDateTime = LocalDateTime.from(zonedDateTime);
                aaronObject.addProperty(key, LOCALDATETIME, localDateTime);
                break;
            case "ZonedDateTime[]":
                ZonedDateTime[] zonedDateTimeArray = (ZonedDateTime[]) value;
                LocalDateTime[] localDateTimeArray = Arrays.stream(zonedDateTimeArray).map(LocalDateTime::from).toArray(LocalDateTime[]::new);
                aaronObject.addProperty(key, LOCALDATETIME_ARRAY, localDateTimeArray);
                break;
            case "LocalDateTime":
                aaronObject.addProperty(key, LOCALDATETIME, LOCALDATETIME.cast(value));
                break;
            case "LocalDateTime[]":
                aaronObject.addProperty(key, LOCALDATETIME_ARRAY, LOCALDATETIME_ARRAY.cast(value));
                break;
            case "TemporalAmount":
            case "Duration":
                aaronObject.addProperty(key, DURATION, DURATION.cast(value));
                break;
            case "TemporalAmount[]":
            case "Duration[]":
                aaronObject.addProperty(key, DURATION_ARRAY, DURATION_ARRAY.cast(value));
                break;
            default:
                log.error("Can not map property type of {}", valueClass.getSimpleName());
        }
    }
}

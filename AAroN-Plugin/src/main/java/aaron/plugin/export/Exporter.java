package aaron.plugin.export;

import aaron.export.AAroNCsvWriter;
import aaron.model.*;
import aaron.util.ProgressInfo;
import aaron.util.Util;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;
import org.neo4j.values.storable.DurationValue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static aaron.model.PropertyType.*;

public class Exporter {

    @Context
    public GraphDatabaseService db;

    @Context
    public Log log;

    public Exporter() {

    }

    @Procedure(name = "aaron.export.csv", mode = Mode.READ)
    @Description("Export the whole Neo4j graph db to Neo4j's CSV header format.")
    public Map<String, Object> exportCSV(@Name("csvFileNamePrefix") String fileName, @Name(value = "config", defaultValue = "{}") Map<String, Object> configMap) {
        Map<String, Object> resultMap = new HashMap<>();
        String importFolderStr;
        try (Transaction transaction = db.beginTx()) {
            importFolderStr = Util.getImportFolder(transaction);
        }
        Path importFolder = Path.of(importFolderStr);
        Path nodesFile = importFolder.resolve(fileName + "_nodes.csv");
        Path edgesFile = importFolder.resolve(fileName + "_edges.csv");
        Model model = new Model();
        try ( Transaction tx = db.beginTx() ) {
            tx.getAllNodes().forEach(node -> {
                AAroNNode.Builder builder = AAroNNode.builder();
                node.getLabels().forEach(label -> {
                    builder.addLabel(label.name());
                });
                AAroNNode aaronNode = builder.build();
                node.getProperties().forEach((key, value) -> {
                    addProperty(aaronNode, key, value);
                });
                model.addNode(new Neo4jNodeIdentifier(node.getId()),  aaronNode);
            });
            tx.getAllRelationships().forEach(relationship -> {
                AAroNEdge.Builder builder = AAroNEdge.builder();
                builder.setType(relationship.getType().name());
                builder.setStart(new Neo4jNodeIdentifier(relationship.getStartNodeId()));
                builder.setEnd(new Neo4jNodeIdentifier(relationship.getEndNodeId()));
                AAroNEdge aaronEdge = builder.build();
                relationship.getProperties().forEach((key, value) -> {
                    addProperty(aaronEdge, key, value);
                });
                model.addEdge(new Neo4jEdgeIdentifier(relationship.getId()),  aaronEdge);
            });
            AAroNCsvWriter.write(model, nodesFile.toFile(), edgesFile.toFile());
            resultMap.put("nodesFile", nodesFile.toFile());
            resultMap.put("edgesFile", edgesFile.toFile());
            return resultMap;
        } catch (IOException e) {
            log.error("Could not write CSV files", e);
            throw new RuntimeException(e);
        }
    }

    private static void addProperty(WithProperties aaronObject, String key, Object value) {
        Class<?> valueClass = value.getClass();
        switch (valueClass.getSimpleName()) {
            case "Boolean":
                aaronObject.addProperty(key, BOOLEAN, BOOLEAN.cast(value));
                break;
            case "Byte":
                aaronObject.addProperty(key, BYTE, BYTE.cast(value));
                break;
            case "Short":
                aaronObject.addProperty(key, SHORT, SHORT.cast(value));
                break;
            case "Integer":
                aaronObject.addProperty(key, INTEGER, INTEGER.cast(value));
                break;
            case "Long":
                aaronObject.addProperty(key, LONG, LONG.cast(value));
                break;
            case "Float":
                aaronObject.addProperty(key, FLOAT, FLOAT.cast(value));
                break;
            case "Double":
                aaronObject.addProperty(key, DOUBLE, DOUBLE.cast(value));
                break;
            case "Character":
                aaronObject.addProperty(key, CHAR, CHAR.cast(value));
                break;
            case "String":
                aaronObject.addProperty(key, STRING, STRING.cast(value));
                break;
            case "Point":
                aaronObject.addProperty(key, POINT, POINT.cast(value));
                break;
            case "LocalDate":
                aaronObject.addProperty(key, LOCALDATE, LOCALDATE.cast(value));
                break;
            case "OffsetTime":
                OffsetDateTime offsetDateTime = (OffsetDateTime) value;
                LocalTime localTime = LocalTime.from(offsetDateTime);
                aaronObject.addProperty(key, LOCALTIME, localTime);
                break;
            case "LocalTime":
                aaronObject.addProperty(key, LOCALTIME, LOCALTIME.cast(value));
                break;
            case "ZonedDateTime":
                ZonedDateTime zonedDateTime = (ZonedDateTime) value;
                LocalDateTime localDateTime = LocalDateTime.from(zonedDateTime);
                aaronObject.addProperty(key, LOCALDATETIME, localDateTime);
                break;
            case "LocalDateTime":
                aaronObject.addProperty(key, LOCALDATETIME, LOCALDATETIME.cast(value));
                break;
            case "TemporalAmount":
            case "Duration":
                aaronObject.addProperty(key, DURATION, DURATION.cast(value));
                break;
        }
    }
}

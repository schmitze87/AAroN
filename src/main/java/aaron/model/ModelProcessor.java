package aaron.model;

import aaron.apoc.export.util.BatchTransaction;
import aaron.apoc.export.util.ProgressReporter;
import org.neo4j.graphdb.*;

import java.util.*;
import java.util.stream.Collectors;

public class ModelProcessor {

    private final GraphDatabaseService db;
    private final ProgressReporter reporter;
    private final Map<Identifier, Long> identifierToNeo4jId;
    private final Map<AAroNNode, Set<Identifier>> nodeToIdentifiers;

    public ModelProcessor(final GraphDatabaseService db, final ProgressReporter reporter) {
        this.db = db;
        this.reporter = reporter;
        identifierToNeo4jId = new HashMap<>();
        nodeToIdentifiers = new HashMap<>();
    }

    public void process(final Model model) {
        init(model);
        processNodes(model.getNodes());
        processEdges(model.getEdges());
    }

    private void init(final Model model) {
        Map<Identifier, AAroNNode> nodesMap = model.getNodes();
        nodesMap.forEach((identifier, node) -> {
            Set<Identifier> identifiers = nodeToIdentifiers.getOrDefault(node, new HashSet<>());
            identifiers.add(identifier);
            nodeToIdentifiers.put(node, identifiers);
        });
    }

    private void processNodes(final Map<Identifier, AAroNNode> nodesMap) {
        try (BatchTransaction btx = new BatchTransaction(db, 1000, reporter)) {
            List<AAroNNode> nodes = nodesMap.values().stream().distinct().collect(Collectors.toList());
            for (AAroNNode node : nodes) {
                Transaction tx = btx.getTransaction();
                Node newNode = tx.createNode();
                long neo4jId = newNode.getId();
                Set<Identifier> identifiers = nodeToIdentifiers.get(node);
                identifiers.forEach(i -> identifierToNeo4jId.put(i, neo4jId));
                node.getLabels().forEach(label -> newNode.addLabel(Label.label(label)));
                int props = 0;
                for (Map.Entry<String, Object> prop : node.getProperties().entrySet()) {
                    Object value = prop.getValue();
                    if (value != null) {
                        newNode.setProperty(prop.getKey(), value);
                        props++;
                    }
                }
                reporter.update(1, 0, props);
                btx.increment();
            }
        }
    }

    private void processEdges(final Map<Identifier, Edge> edges) {
        try (BatchTransaction btx = new BatchTransaction(db, 1000, reporter)) {
            edges.forEach((key, edge) -> {
                Transaction tx = btx.getTransaction();
                Long startId = identifierToNeo4jId.get(edge.getStart());
                Long endId = identifierToNeo4jId.get(edge.getEnd());
                if (startId == null) {
                    return;
                }
                if (endId == null) {
                    return;
                }
                Node startNode = tx.getNodeById(startId);
                Node endNode = tx.getNodeById(endId);
                Relationship relationship = startNode.createRelationshipTo(endNode, RelationshipType.withName(edge.getType()));
                int props = 0;
                for (Map.Entry<String, Object> prop : edge.getProperties().entrySet()) {
                    Object value = prop.getValue();
                    if (value != null) {
                        relationship.setProperty(prop.getKey(), value);
                        props++;
                    }
                }
                reporter.update(0, 1, props);
                btx.increment();
            });
        }
    }
}

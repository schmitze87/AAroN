package aaron.model;

import aaron.util.BatchTransaction;
import aaron.util.ProgressReporter;
import org.neo4j.graphdb.*;

import java.util.*;

public class ModelProcessor {

    private final GraphDatabaseService db;
    private final ProgressReporter reporter;
    private final Map<Identifier, String> identifierToNeo4jId;
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
            List<AAroNNode> nodes = nodesMap.values().stream().distinct().toList();
            for (AAroNNode node : nodes) {
                Transaction tx = btx.getTransaction();
                Node newNode = tx.createNode();
                String neo4jId = newNode.getElementId();
                Set<Identifier> identifiers = nodeToIdentifiers.get(node);
                identifiers.forEach(i -> identifierToNeo4jId.put(i, neo4jId));
                node.getLabels().forEach(label -> newNode.addLabel(Label.label(label)));
                int props = 0;
                for (Map.Entry<String, Property> prop : node.getProperties().entrySet()) {
                    Property property = prop.getValue();
                    Object value = property.getValue();
                    if (value != null) {
                        newNode.setProperty(prop.getKey(), value);
                        props++;
                    }
                }
                reporter.update(1, 0, props);
                btx.increment();
            }
            btx.commit();
        }
    }

    private void processEdges(final Map<Identifier, AAroNEdge> edgesMap) {
        try (BatchTransaction btx = new BatchTransaction(db, 1000, reporter)) {
            List<AAroNEdge> edges = edgesMap.values().stream().distinct().toList();
            edges.forEach(edge -> {
                Transaction tx = btx.getTransaction();
                String startId = identifierToNeo4jId.get(edge.getStart());
                String endId = identifierToNeo4jId.get(edge.getEnd());
                if (startId == null) {
                    return;
                }
                if (endId == null) {
                    return;
                }
                Node startNode = tx.getNodeByElementId(startId);
                Node endNode = tx.getNodeByElementId(endId);
                Relationship relationship = startNode.createRelationshipTo(endNode, RelationshipType.withName(edge.getType()));
                int props = 0;
                for (Map.Entry<String, Property> prop : edge.getProperties().entrySet()) {
                    Property property = prop.getValue();
                    Object value = property.getValue();
                    if (value != null) {
                        relationship.setProperty(prop.getKey(), value);
                        props++;
                    }
                }
                edge.setNeo4jElementId(relationship.getElementId());
                reporter.update(0, 1, props);
                btx.increment();
            });
            btx.commit();
        }
    }

    private String resolveNeo4jEdgeId(final Map<Identifier, AAroNEdge> edgesMap, final Identifier identifier) {
        AAroNEdge aAroNEdge = edgesMap.get(identifier);
        return aAroNEdge != null ? aAroNEdge.getNeo4jElementId() : null;
    }
}

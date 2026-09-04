package aaron.model;

import aaron.util.BatchTransaction;
import aaron.util.ProgressReporter;
import org.neo4j.graphdb.*;

import java.util.*;

public class ModelProcessor {

    private final GraphDatabaseService db;
    private final ProgressReporter reporter;
    private final Map<Identifier, String> identifierToNeo4jId;

    public ModelProcessor(final GraphDatabaseService db, final ProgressReporter reporter) {
        this.db = db;
        this.reporter = reporter;
        identifierToNeo4jId = new HashMap<>();
    }

    public void process(final Model model) {
        processNodes(model);
        processEdges(model);
    }


    private void processNodes(final Model model) {
        try (BatchTransaction btx = new BatchTransaction(db, 1000, reporter)) {
            for (Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode> entry : model.iterateNodeEntries()) {
                UniqueNodeIdentifier<UUID> uniqueNodeIdentifier = entry.getKey();
                AAroNNode node = entry.getValue();
                Transaction tx = btx.getTransaction();
                Node newNode = tx.createNode();
                String neo4jId = newNode.getElementId();
                Set<Identifier> identifiers = model.getIdentifiersByUniqueNodeIdentifiers().get(uniqueNodeIdentifier);
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

    private void processEdges(final Model model) {
        try (BatchTransaction btx = new BatchTransaction(db, 1000, reporter)) {
            for (AAroNEdge edge : model.iterateEdges()) {
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
            }
            btx.commit();
        }
    }

    private String resolveNeo4jEdgeId(final Map<Identifier, AAroNEdge> edgesMap, final Identifier identifier) {
        AAroNEdge aAroNEdge = edgesMap.get(identifier);
        return aAroNEdge != null ? aAroNEdge.getNeo4jElementId() : null;
    }
}

package aaron.model.stores;

import aaron.model.AAroNEdge;
import aaron.model.AAroNNode;
import aaron.model.UniqueEdgeIdentifier;
import aaron.model.UniqueNodeIdentifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class InHeapModelStore implements ModelStore {

    private final Map<UniqueNodeIdentifier<UUID>, AAroNNode> nodes = new HashMap<>();
    private final Map<UniqueEdgeIdentifier<UUID>, AAroNEdge> edges = new HashMap<>();

    @Override
    public void addNode(UniqueNodeIdentifier<UUID> nodeIdentifier, AAroNNode node) {
        Objects.requireNonNull(nodeIdentifier, "nodeIdentifier must not be null");
        Objects.requireNonNull(node, "node must not be null");
        nodes.put(nodeIdentifier, node);
    }

    @Override
    public AAroNNode getNode(UniqueNodeIdentifier<UUID> nodeIdentifier) {
        Objects.requireNonNull(nodeIdentifier, "nodeIdentifier must not be null");
        return nodes.get(nodeIdentifier);
    }

    @Override
    public Iterable<AAroNNode> iterateNodes() {
        return nodes.values();
    }

    @Override
    public Iterable<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> iterateNodeEntries() {
        return nodes.entrySet();
    }

    @Override
    public void addEdge(UniqueEdgeIdentifier<UUID> edgeIdentifier, AAroNEdge edge) {
        Objects.requireNonNull(edgeIdentifier, "edgeIdentifier must not be null");
        Objects.requireNonNull(edge, "edge must not be null");
        edges.put(edgeIdentifier, edge);
    }

    @Override
    public AAroNEdge getEdge(UniqueEdgeIdentifier<UUID> edgeIdentifier) {
        Objects.requireNonNull(edgeIdentifier, "edgeIdentifier must not be null");
        return edges.get(edgeIdentifier);
    }

    @Override
    public Iterable<AAroNEdge> iterateEdges() {
        return edges.values();
    }

    @Override
    public int countNodes() {
        return nodes.size();
    }

    @Override
    public int countEdges() {
        return edges.size();
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
        nodes.clear();
        edges.clear();
    }
}

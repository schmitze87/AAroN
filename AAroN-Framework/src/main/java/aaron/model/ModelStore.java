package aaron.model;

import java.io.Closeable;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("rawtypes")
public interface ModelStore extends Closeable {

    void addNode(UniqueNodeIdentifier<UUID> nodeIdentifier, AAroNNode node);
    AAroNNode getNode(UniqueNodeIdentifier<UUID> nodeIdentifier);
    Iterable<AAroNNode> iterateNodes();
    Iterable<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> iterateNodeEntries();

    void addEdge(UniqueEdgeIdentifier<UUID> edgeIdentifier, AAroNEdge edge);
    AAroNEdge getEdge(UniqueEdgeIdentifier<UUID> edgeIdentifier);
    Iterable<AAroNEdge> iterateEdges();

    int countNodes();
    int countEdges();

    void flush();
    @Override
    void close();
}

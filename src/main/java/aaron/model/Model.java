package aaron.model;

import java.util.HashMap;
import java.util.Map;

public class Model {

    private Map<Identifier, AAroNNode> nodes = new HashMap<>();
    private Map<Identifier, Edge> edges = new HashMap<>();

    public Model () {

    }

    public AAroNNode addNode(final Identifier identifier, final AAroNNode node) {
        return nodes.put(identifier, node);
    }

    public AAroNNode getNode(final Identifier identifier) {
        return nodes.get(identifier);
    }

    public Map<Identifier, AAroNNode> getNodes() {
        return nodes;
    }

    public Map<Identifier, Edge> getEdges() {
        return edges;
    }

    public Edge addEdge(final Identifier identifier, final Edge edge) {
        return edges.put(identifier, edge);
    }

    public Edge getEdge(final Identifier identifier) {
        return edges.get(identifier);
    }

}

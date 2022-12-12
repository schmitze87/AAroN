package aaron.model;

import java.util.HashMap;
import java.util.Map;

public class Model {

    private Map<Identifier, AAroNNode> nodes = new HashMap<>();
    private Map<Identifier, AAroNEdge> edges = new HashMap<>();

    private ImportConext context = null;

    public Model () {
    }

    public ImportConext getContext() {
        return context;
    }

    public void setContext(ImportConext context) {
        this.context = context;
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

    public Map<Identifier, AAroNEdge> getEdges() {
        return edges;
    }

    public AAroNEdge addEdge(final Identifier identifier, final AAroNEdge edge) {
        return edges.put(identifier, edge);
    }

    public AAroNEdge getEdge(final Identifier identifier) {
        return edges.get(identifier);
    }

}

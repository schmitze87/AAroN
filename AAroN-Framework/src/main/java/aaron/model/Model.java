package aaron.model;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class Model implements Closeable {

    private MVStore mvStoreNodes;
    private MVStore mvStoreEdges;
    private MVMap<UniqueNodeIdentifier, byte[]> nodes;
    private MVMap<UniqueEdgeIdentifier, byte[]> edges;
    @SuppressWarnings("rawtypes")
    private final Map<Identifier, UniqueNodeIdentifier> nodeIdentifiers = new HashMap<>();
    private final Map<UniqueNodeIdentifier, Set<Identifier>> IdentifiersByUniqueNodeIdentifiers = new HashMap<>();
    @SuppressWarnings("rawtypes")
    private final Map<Identifier, UniqueEdgeIdentifier> edgeIdentifiers = new HashMap<>();

    private int totalPropertiesCount = 0;

    private ImportConext context = null;

    public static class Builder {

        private File workingDir;

        public Builder workingDir(File workingDir) {
            this.workingDir = workingDir;
            return this;
        }

        public Model build() {
            if (workingDir == null) {
                return new Model();
            }
            return new Model(workingDir);
        }
    }

    private Model() {
        mvStoreNodes = new MVStore.Builder().open();
        mvStoreEdges = new MVStore.Builder().open();
        nodes = mvStoreNodes.openMap("nodes");
        edges = mvStoreEdges.openMap("edges");
    }

    @SuppressWarnings("resource")
    private Model(File workingDir) {
        Path nodesStorePath = workingDir.toPath().resolve("nodes.mvStore");
        Path edgesStorePath = workingDir.toPath().resolve("edges.mvStore");
        mvStoreNodes = new MVStore.Builder().fileName(nodesStorePath.toString()).compress().open();
        mvStoreEdges = new MVStore.Builder().fileName(edgesStorePath.toString()).compress().open();
        nodes = mvStoreNodes.openMap("nodes");
        edges = mvStoreEdges.openMap("edges");
        nodes.clear();
        edges.clear();
    }

    public ImportConext getContext() {
        return context;
    }

    public void setContext(ImportConext context) {
        this.context = context;
    }

    @SuppressWarnings("rawtypes")
    public void addNode(final UniqueNodeIdentifier identifier, final AAroNNode node) {
        totalPropertiesCount = (int) (totalPropertiesCount + node.properties.values().stream().filter(Objects::nonNull).count());
        byte[] byteArray = NodeSerializer.serialize(node);
        nodes.put(identifier, byteArray);
        nodes.store.commit();
        nodeIdentifiers.put(identifier, identifier);
    }

    @SuppressWarnings("rawtypes")
    public void addNodeIdentifier(final Identifier identifier, UniqueNodeIdentifier uniqueNodeIdentifier) {
        nodeIdentifiers.put(identifier, uniqueNodeIdentifier);
        IdentifiersByUniqueNodeIdentifiers
                .computeIfAbsent(uniqueNodeIdentifier, ignored -> new HashSet<>())
                .add(identifier);
    }

    public Map<UniqueNodeIdentifier, Set<Identifier>> getIdentifiersByUniqueNodeIdentifiers() {
        return  IdentifiersByUniqueNodeIdentifiers;
    }

    @SuppressWarnings("rawtypes")
    public UniqueNodeIdentifier getUniqueNodeIdentifier(final Identifier identifier) {
        return nodeIdentifiers.get(identifier);
    }

    @SuppressWarnings("rawtypes")
    public Set<Identifier> getNodeIdentifiers() {
        return nodeIdentifiers.keySet();
    }

    @SuppressWarnings("rawtypes")
    public AAroNNode getNode(final Identifier identifier) {
        if (identifier == null) return null;
        UniqueNodeIdentifier uniqueNodeIdentifier;
        if (identifier instanceof UniqueNodeIdentifier) {
            uniqueNodeIdentifier = (UniqueNodeIdentifier) identifier;
        } else {
            uniqueNodeIdentifier = nodeIdentifiers.get(identifier);
        }
        if (uniqueNodeIdentifier == null) {
            return null;
        } else {
            byte[] bytes = nodes.get(uniqueNodeIdentifier);
            return NodeSerializer.deserialize(bytes);
        }
    }

    public Iterable<AAroNNode> iterateNodes() {
        return () -> new Iterator<>() {
            private final Iterator<byte[]> iterator = nodes.values().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public AAroNNode next() {
                return NodeSerializer.deserialize(iterator.next());
            }
        };
    }

    public Iterable<Map.Entry<UniqueNodeIdentifier, AAroNNode>> iterateNodeEntries() {
        return () -> new Iterator<>() {
            private final Iterator<Map.Entry<UniqueNodeIdentifier, byte[]>> iterator = nodes.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Map.Entry<UniqueNodeIdentifier, AAroNNode> next() {
                Map.Entry<UniqueNodeIdentifier, byte[]> next = iterator.next();
                Map.Entry<UniqueNodeIdentifier, AAroNNode> nodeEntry = Map.entry(next.getKey(), NodeSerializer.deserialize(next.getValue()));
                return nodeEntry;
            }
        };
    }

    public Iterable<AAroNEdge> iterateEdges() {
        return () -> new Iterator<>() {
            private final Iterator<byte[]> iterator = edges.values().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public AAroNEdge next() {
                return EdgeSerializer.deserialize(iterator.next());
            }
        };
    }

    public int countNodes() {
        return nodes.size();
    }

    public int countEdges() {
        return edges.size();
    }

    public int countProperties() {
        return totalPropertiesCount;
    }

    @SuppressWarnings("rawtypes")
    public void addEdge(final UniqueEdgeIdentifier identifier, final AAroNEdge edge) {
        totalPropertiesCount = (int) (totalPropertiesCount + edge.properties.values().stream().filter(Objects::nonNull).count());
        byte[] byteArray = EdgeSerializer.serialize(edge);
        edges.put(identifier, byteArray);
        edges.store.commit();
        edgeIdentifiers.put(identifier, identifier);
    }

    @SuppressWarnings("rawtypes")
    public void addEdgeIdentifier(final Identifier identifier, UniqueEdgeIdentifier uniqueEdgeIdentifier) {
        edgeIdentifiers.put(identifier, uniqueEdgeIdentifier);
    }

    @SuppressWarnings("rawtypes")
    public UniqueEdgeIdentifier getUniqueEdgeIdentifier(final Identifier identifier) {
        return edgeIdentifiers.get(identifier);
    }

    @SuppressWarnings("rawtypes")
    public AAroNEdge getEdge(final Identifier identifier) {
        if (identifier == null) return null;
        UniqueEdgeIdentifier uniqueEdgeIdentifier;
        if (identifier instanceof UniqueEdgeIdentifierImpl) {
            uniqueEdgeIdentifier = (UniqueEdgeIdentifier) identifier;
        } else {
            uniqueEdgeIdentifier = edgeIdentifiers.get(identifier);
        }
        if (uniqueEdgeIdentifier == null) {
            return null;
        } else {
            byte[] bytes = edges.get(uniqueEdgeIdentifier);
            return EdgeSerializer.deserialize(bytes);
        }
    }

    @Override
    public void close() {
        mvStoreNodes.commit();
        mvStoreNodes.close();
        mvStoreEdges.commit();
        mvStoreEdges.close();
    }
}

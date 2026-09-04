package aaron.model;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

public class Model implements Closeable {

    public static class Builder {

        private File workingDir;
        private ModelStore modelStore;

        public Builder workingDir(File workingDir) {
            this.workingDir = workingDir;
            return this;
        }

        public Model build() {
            UUID uuid = UUID.randomUUID();
            Path currentDir = Paths.get("").toAbsolutePath();
            if (workingDir == null) {
                workingDir = currentDir.toFile();
            }
            Path modelStoreFile = workingDir.toPath().resolve(uuid.toString() + ".db");
            try {
                SQLiteModelStore sqLiteModelStore = new SQLiteModelStore(modelStoreFile.toFile());
                return new Model(sqLiteModelStore);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private final Map<Identifier, UniqueNodeIdentifier> nodeIdentifiers = new HashMap<>();
    @SuppressWarnings("rawtypes")
    private final Map<UniqueNodeIdentifier, Set<Identifier>> IdentifiersByUniqueNodeIdentifiers = new HashMap<>();
    @SuppressWarnings("rawtypes")
    private final Map<Identifier, UniqueEdgeIdentifier> edgeIdentifiers = new HashMap<>();
    private int totalPropertiesCount = 0;
    private ImportConext context = null;
    private final ModelStore modelStore;

    private Model(ModelStore modelStore) {
        this.modelStore = modelStore;
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
        modelStore.addNode(identifier, node);
        nodeIdentifiers.put(identifier, identifier);
    }

    @SuppressWarnings("rawtypes")
    public void addNodeIdentifier(final Identifier identifier, UniqueNodeIdentifier uniqueNodeIdentifier) {
        nodeIdentifiers.put(identifier, uniqueNodeIdentifier);
        IdentifiersByUniqueNodeIdentifiers
                .computeIfAbsent(uniqueNodeIdentifier, ignored -> new HashSet<>())
                .add(identifier);
    }

    @SuppressWarnings("rawtypes")
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
            return modelStore.getNode(uniqueNodeIdentifier);
        }
    }

    public Iterable<AAroNNode> iterateNodes() {
        return modelStore.iterateNodes();
    }

    public Iterable<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> iterateNodeEntries() {
        return modelStore.iterateNodeEntries();
    }

    public Iterable<AAroNEdge> iterateEdges() {
        return modelStore.iterateEdges();
    }

    public int countNodes() {
        return modelStore.countNodes();
    }

    public int countEdges() {
        return modelStore.countEdges();
    }

    public int countProperties() {
        return totalPropertiesCount;
    }

    @SuppressWarnings("rawtypes")
    public void addEdge(final UniqueEdgeIdentifier identifier, final AAroNEdge edge) {
        totalPropertiesCount = (int) (totalPropertiesCount + edge.properties.values().stream().filter(Objects::nonNull).count());
        modelStore.addEdge(identifier, edge);
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
            return modelStore.getEdge(uniqueEdgeIdentifier);
        }
    }

    public void flush() {
        modelStore.flush();
    }

    @Override
    public void close() throws IOException {
        modelStore.close();
    }
}

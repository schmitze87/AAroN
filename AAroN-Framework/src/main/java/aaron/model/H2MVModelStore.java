package aaron.model;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class H2MVModelStore implements ModelStore {

    private final MVStore mvStoreNodes;
    private final MVStore mvStoreEdges;
    private final MVMap<UniqueNodeIdentifier<UUID>, byte[]> nodes;
    private final MVMap<UniqueEdgeIdentifier<UUID>, byte[]> edges;
    private final Path mvStoreNodesPath;
    private final Path mvStoreEdgesPath;

    public H2MVModelStore() {
        this.mvStoreNodesPath = null;
        this.mvStoreEdgesPath = null;
        mvStoreNodes = new MVStore.Builder().open();
        mvStoreEdges = new MVStore.Builder().open();
        nodes = mvStoreNodes.openMap("nodes");
        edges = mvStoreEdges.openMap("edges");
    }

    public H2MVModelStore(File workingDir) {
        UUID uuid = UUID.randomUUID();
        this.mvStoreNodesPath = workingDir.toPath().resolve("nodes-" + uuid + ".mvStore");
        this.mvStoreEdgesPath = workingDir.toPath().resolve("edges-" + uuid + ".mvStore");
        this.mvStoreNodes = new MVStore.Builder()
                .fileName(mvStoreNodesPath.toString())
//                .compress()
                .pageSplitSize(16)
                .cacheSize(16)
//                .autoCommitBufferSize(10240)
                .autoCommitDisabled()
                .open();
        this.mvStoreEdges = new MVStore.Builder()
                .fileName(mvStoreEdgesPath.toString())
//                .compress()
                .pageSplitSize(16)
                .cacheSize(16)
//                .autoCommitBufferSize(10240)
                .autoCommitDisabled()
                .open();
        this.nodes = this.mvStoreNodes.openMap("nodes");
        this.edges = this.mvStoreEdges.openMap("edges");
        this.nodes.clear();
        this.edges.clear();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void addNode(UniqueNodeIdentifier<UUID> nodeIdentifier, AAroNNode node) {
        byte[] byteArray = NodeSerializer.serialize(node);
        nodes.put(nodeIdentifier, byteArray);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public AAroNNode getNode(UniqueNodeIdentifier<UUID> nodeIdentifier) {
        byte[] bytes = nodes.get(nodeIdentifier);
        return NodeSerializer.deserialize(bytes);
    }

    @Override
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

    @Override
    @SuppressWarnings("rawtypes")
    public Iterable<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> iterateNodeEntries() {
        return () -> new Iterator<>() {
            private final Iterator<Map.Entry<UniqueNodeIdentifier<UUID>, byte[]>> iterator = nodes.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode> next() {
                Map.Entry<UniqueNodeIdentifier<UUID>, byte[]> next = iterator.next();
                return Map.entry(next.getKey(), NodeSerializer.deserialize(next.getValue()));
            }
        };
    }

    @Override

    @SuppressWarnings("rawtypes")
    public void addEdge(UniqueEdgeIdentifier<UUID> edgeIdentifier, AAroNEdge edge) {
        byte[] byteArray = EdgeSerializer.serialize(edge);
        edges.put(edgeIdentifier, byteArray);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public AAroNEdge getEdge(UniqueEdgeIdentifier<UUID> edgeIdentifier) {
        byte[] bytes = edges.get(edgeIdentifier);
        return EdgeSerializer.deserialize(bytes);
    }

    @Override
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
        this.nodes.store.commit();
        this.edges.store.commit();
    }

    @Override
    public void close() {
        mvStoreNodes.commit();
        mvStoreNodes.close();
        mvStoreEdges.commit();
        mvStoreEdges.close();
        try {
            Files.delete(this.mvStoreNodesPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.delete(this.mvStoreEdgesPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

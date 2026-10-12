package aaron.model.stores;

import aaron.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FilebasedModelStore implements ModelStore {

    private final Path nodesPath;
    private final Path edgesPath;

    public FilebasedModelStore(Path modelStoreDir) throws IOException {
        nodesPath = modelStoreDir.resolve("nodes");
        edgesPath = modelStoreDir.resolve("edges");
        Files.createDirectories(nodesPath);
        Files.createDirectories(edgesPath);
    }

    @Override
    public void addNode(UniqueNodeIdentifier<UUID> nodeIdentifier, AAroNNode node) {
        Objects.requireNonNull(nodeIdentifier, "nodeIdentifier must not be null");
        Objects.requireNonNull(node, "node must not be null");

        byte[] byteArray = NodeSerializer.serialize(node);
        Path nodeFile = nodesPath.resolve(nodeIdentifier.getIdentifier().toString());
        try {
            Files.write(nodeFile, byteArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AAroNNode getNode(UniqueNodeIdentifier<UUID> nodeIdentifier) {
        Objects.requireNonNull(nodeIdentifier, "nodeIdentifier must not be null");

        Path nodeFile = nodesPath.resolve(nodeIdentifier.getIdentifier().toString());
        if (Files.exists(nodeFile)) {
            try {
                return NodeSerializer.deserialize(Files.readAllBytes(nodeFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public Iterable<AAroNNode> iterateNodes() {
        return () -> getNodeEntries()
                .map(Map.Entry::getValue)
                .iterator();
    }

    private Stream<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> getNodeEntries() {
        class NodeEntryIterator implements Iterator<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> {

            private final Iterator<Path> internalIterator;

            private NodeEntryIterator() throws IOException {
                try (Stream<Path> nodeFiles = Files.list(nodesPath)) {
                    internalIterator = nodeFiles.toList().iterator();
                }
            }

            @Override
            public boolean hasNext() {
                return internalIterator.hasNext();
            }

            @Override
            public Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode> next() {
                Path next = internalIterator.next();
                String fileName = next.getFileName().toString();
                UniqueNodeIdentifier<UUID> identifier = new UniqueNodeIdentifierImpl(UUID.fromString(fileName));
                try {
                    AAroNNode node = NodeSerializer.deserialize(Files.readAllBytes(next));
                    return Map.entry(identifier, node);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new NodeEntryIterator(), 0), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Map.Entry<UniqueEdgeIdentifier<UUID>, AAroNEdge>> getEdgeEntries() {
        class EdgeEntryIterator implements Iterator<Map.Entry<UniqueEdgeIdentifier<UUID>, AAroNEdge>> {

            private final Iterator<Path> internalIterator;

            private EdgeEntryIterator() throws IOException {
                try (Stream<Path> edgeFiles = Files.list(edgesPath)) {
                    internalIterator = edgeFiles.toList().iterator();
                }
            }

            @Override
            public boolean hasNext() {
                return internalIterator.hasNext();
            }

            @Override
            public Map.Entry<UniqueEdgeIdentifier<UUID>, AAroNEdge> next() {
                Path next = internalIterator.next();
                String fileName = next.getFileName().toString();
                UniqueEdgeIdentifier<UUID> identifier = new UniqueEdgeIdentifierImpl(UUID.fromString(fileName));
                try {
                    AAroNEdge edge = EdgeSerializer.deserialize(Files.readAllBytes(next));
                    return Map.entry(identifier, edge);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new EdgeEntryIterator(), 0), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> iterateNodeEntries() {
        return () -> getNodeEntries()
                .iterator();
    }

    @Override
    public void addEdge(UniqueEdgeIdentifier<UUID> edgeIdentifier, AAroNEdge edge) {
        Objects.requireNonNull(edgeIdentifier, "edgeIdentifier must not be null");
        Objects.requireNonNull(edge, "edge must not be null");

        byte[] byteArray = EdgeSerializer.serialize(edge);
        Path edgeFile = edgesPath.resolve(edgeIdentifier.getIdentifier().toString());
        try {
            Files.write(edgeFile, byteArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AAroNEdge getEdge(UniqueEdgeIdentifier<UUID> edgeIdentifier) {
        Objects.requireNonNull(edgeIdentifier, "edgeIdentifier must not be null");

        Path edgeFile = edgesPath.resolve(edgeIdentifier.getIdentifier().toString());
        if (Files.exists(edgeFile)) {
            try {
                return EdgeSerializer.deserialize(Files.readAllBytes(edgeFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public Iterable<AAroNEdge> iterateEdges() {
        return () -> getEdgeEntries()
                .map(Map.Entry::getValue)
                .iterator();
    }

    @Override
    public int countNodes() {
        try (Stream<Path> files = Files.list(nodesPath)) {
            return Math.toIntExact(files.count());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countEdges() {
        try (Stream<Path> files = Files.list(edgesPath)) {
            return Math.toIntExact(files.count());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() {
        deleteDirectory(nodesPath);
        deleteDirectory(edgesPath);
    }

    private static void deleteDirectory(Path directory) {
        try (Stream<Path> paths = Files.walk(directory)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(FilebasedModelStore::deletePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deletePath(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package aaron.model.stores;

import aaron.model.*;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SQLiteModelStore implements ModelStore {

    private static final int NODE_ENTRY_CHUNK_SIZE = 1_000;
    private static final int EDGE_ENTRY_CHUNK_SIZE = 1_000;

    private final Connection connection;
    private final File storeFile;

    public SQLiteModelStore(File storeFile) throws SQLException {
        this.storeFile = storeFile;
        String url = "jdbc:sqlite:" + storeFile.getAbsolutePath();
        connection = DriverManager.getConnection(url);
        initialiseStore(connection);
        connection.setAutoCommit(false);
    }

    private void initialiseStore(Connection connection) {
        Objects.requireNonNull(connection);
        try {
            var meta = connection.getMetaData();
            createTables(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void createTables(Connection connection) throws SQLException {
        // SQL statement for creating a new table
        var sqlCreateNodes = """
                CREATE TABLE IF NOT EXISTS nodes (\
                	key TEXT PRIMARY KEY,\
                	value BLOB NOT NULL\
                );""";
        var sqlCreateEdges = """
                CREATE TABLE IF NOT EXISTS edges (\
                	key TEXT PRIMARY KEY,\
                	value BLOB NOT NULL\
                );""";
        Statement createNodesStatement = connection.createStatement();
        createNodesStatement.execute(sqlCreateNodes);
        Statement createEdgesStatement = connection.createStatement();
        createEdgesStatement.execute(sqlCreateEdges);
    }

    @Override
    public void addNode(UniqueNodeIdentifier<UUID> nodeIdentifier, AAroNNode node) {
        Objects.requireNonNull(nodeIdentifier, "nodeIdentifier must not be null");
        Objects.requireNonNull(node, "node must not be null");

        byte[] byteArray = NodeSerializer.serialize(node);
        String sql = """
                INSERT INTO nodes(key,value) VALUES(?,?)
                ON CONFLICT(key) DO UPDATE SET
                    value = excluded.value
                """;
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nodeIdentifier.getIdentifier().toString());
            pstmt.setBytes(2, byteArray);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to store node " + nodeIdentifier, e);
        }
    }

    @Override
    public AAroNNode getNode(UniqueNodeIdentifier<UUID> nodeIdentifier) {
        Objects.requireNonNull(nodeIdentifier, "nodeIdentifier must not be null");
        String sql = "SELECT key, value FROM nodes WHERE key = ?";
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nodeIdentifier.getIdentifier().toString());
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                byte[] bytes = resultSet.getBytes(2);
                return NodeSerializer.deserialize(bytes);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<AAroNNode> iterateNodes() {
        return () -> getNodeEntries()
                .map(Map.Entry::getValue)
                .iterator();
    }

    @Override
    public Iterable<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> iterateNodeEntries() {
        return () -> getNodeEntries().iterator();
    }

    @Override
    public void addEdge(UniqueEdgeIdentifier<UUID> edgeIdentifier, AAroNEdge edge) {
        Objects.requireNonNull(edgeIdentifier, "edgeIdentifier must not be null");
        Objects.requireNonNull(edge, "edge must not be null");

        byte[] byteArray = EdgeSerializer.serialize(edge);
        String sql = """
                INSERT INTO edges(key,value) VALUES(?,?)
                ON CONFLICT(key) DO UPDATE SET value = excluded.value
                """;
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, edgeIdentifier.getIdentifier().toString());
            pstmt.setBytes(2, byteArray);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to store node " + edgeIdentifier, e);
        }
    }

    @Override
    public AAroNEdge getEdge(UniqueEdgeIdentifier<UUID> edgeIdentifier) {
        Objects.requireNonNull(edgeIdentifier, "edgeIdentifier must not be null");
        String sql = "SELECT key, value FROM edges WHERE key = ?";
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, edgeIdentifier.getIdentifier().toString());
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                byte[] bytes = resultSet.getBytes(2);
                return EdgeSerializer.deserialize(bytes);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<AAroNEdge> iterateEdges() {
        return () -> getEdgeEntries()
                .map(Map.Entry::getValue)
                .iterator();
    }

    private Stream<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> getNodeEntries() {
        class NodeEntryIterator implements Iterator<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> {
            private Iterator<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> chunkIterator = Collections.emptyIterator();
            private String lastKey;
            private boolean exhausted;

            @Override
            public boolean hasNext() {
                while (!chunkIterator.hasNext() && !exhausted) {
                    fetchNextChunk();
                }
                return chunkIterator.hasNext();
            }

            @Override
            public Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return chunkIterator.next();
            }

            private void fetchNextChunk() {
                String sql = lastKey == null
                        ? "SELECT key, value FROM nodes ORDER BY key LIMIT ?"
                        : "SELECT key, value FROM nodes WHERE key > ? ORDER BY key LIMIT ?";
                try (var pstmt = connection.prepareStatement(sql)) {
                    if (lastKey == null) {
                        pstmt.setInt(1, NODE_ENTRY_CHUNK_SIZE);
                    } else {
                        pstmt.setString(1, lastKey);
                        pstmt.setInt(2, NODE_ENTRY_CHUNK_SIZE);
                    }
                    List<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> chunk = new ArrayList<>(NODE_ENTRY_CHUNK_SIZE);
                    try (var resultSet = pstmt.executeQuery()) {
                        while (resultSet.next()) {
                            String key = resultSet.getString(1);
                            UniqueNodeIdentifier<UUID> identifier = new UniqueNodeIdentifierImpl(UUID.fromString(key));
                            AAroNNode node = NodeSerializer.deserialize(resultSet.getBytes(2));
                            chunk.add(Map.entry(identifier, node));
                            lastKey = key;
                        }
                    }
                    exhausted = chunk.size() < NODE_ENTRY_CHUNK_SIZE;
                    chunkIterator = chunk.iterator();
                } catch (SQLException e) {
                    throw new IllegalStateException("Failed to iterate nodes", e);
                }
            }
        }
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new NodeEntryIterator(), 0), false);
    }

    private Stream<Map.Entry<UniqueEdgeIdentifier<UUID>, AAroNEdge>> getEdgeEntries() {
        class EdgeEntryIterator implements Iterator<Map.Entry<UniqueEdgeIdentifier<UUID>, AAroNEdge>> {
            private Iterator<Map.Entry<UniqueEdgeIdentifier<UUID>, AAroNEdge>> chunkIterator = Collections.emptyIterator();
            private String lastKey;
            private boolean exhausted;

            @Override
            public boolean hasNext() {
                while (!chunkIterator.hasNext() && !exhausted) {
                    fetchNextChunk();
                }
                return chunkIterator.hasNext();
            }

            @Override
            public Map.Entry<UniqueEdgeIdentifier<UUID>, AAroNEdge> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return chunkIterator.next();
            }

            private void fetchNextChunk() {
                String sql = lastKey == null
                        ? "SELECT key, value FROM edges ORDER BY key LIMIT ?"
                        : "SELECT key, value FROM edges WHERE key > ? ORDER BY key LIMIT ?";
                try (var pstmt = connection.prepareStatement(sql)) {
                    if (lastKey == null) {
                        pstmt.setInt(1, EDGE_ENTRY_CHUNK_SIZE);
                    } else {
                        pstmt.setString(1, lastKey);
                        pstmt.setInt(2, EDGE_ENTRY_CHUNK_SIZE);
                    }
                    List<Map.Entry<UniqueEdgeIdentifier<UUID>, AAroNEdge>> chunk = new ArrayList<>(EDGE_ENTRY_CHUNK_SIZE);
                    try (var resultSet = pstmt.executeQuery()) {
                        while (resultSet.next()) {
                            String key = resultSet.getString(1);
                            UniqueEdgeIdentifier<UUID> identifier = new UniqueEdgeIdentifierImpl(UUID.fromString(key));
                            AAroNEdge edge = EdgeSerializer.deserialize(resultSet.getBytes(2));
                            chunk.add(Map.entry(identifier, edge));
                            lastKey = key;
                        }
                    }
                    exhausted = chunk.size() < EDGE_ENTRY_CHUNK_SIZE;
                    chunkIterator = chunk.iterator();
                } catch (SQLException e) {
                    throw new IllegalStateException("Failed to iterate edges", e);
                }
            }
        }
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new EdgeEntryIterator(), 0), false);
    }

    @Override
    public int countNodes() {
        return count("nodes");
    }

    @Override
    public int countEdges() {
        return count("edges");
    }

    private int count(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (var pstmt = connection.prepareStatement(sql);
             var resultSet = pstmt.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to count " + tableName, e);
        }
    }

    @Override
    public void flush() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.delete(storeFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

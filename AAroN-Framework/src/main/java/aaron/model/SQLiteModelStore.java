package aaron.model;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterators;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SQLiteModelStore implements ModelStore {

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
                AAroNNode node = NodeSerializer.deserialize(bytes);
                return node;
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
    @SuppressWarnings("rawtypes")
    public Iterable<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> iterateNodeEntries() {
        return () -> getNodeEntries().iterator();
    }

    @Override
    @SuppressWarnings("rawtypes")
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
    @SuppressWarnings("rawtypes")
    public AAroNEdge getEdge(UniqueEdgeIdentifier<UUID> edgeIdentifier) {
        Objects.requireNonNull(edgeIdentifier, "edgeIdentifier must not be null");
        String sql = "SELECT key, value FROM edges WHERE key = ?";
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, edgeIdentifier.getIdentifier().toString());
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                byte[] bytes = resultSet.getBytes(2);
                AAroNEdge edge = EdgeSerializer.deserialize(bytes);
                return edge;
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
        return () -> getEdges().iterator();
    }

    @SuppressWarnings("rawtypes")
    private Stream<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>> getNodeEntries() {
        String sql = "SELECT key, value FROM nodes";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            var pstmt = statement;
            var resultSet = pstmt.executeQuery();
            class NodeEntryIterator implements Iterator<Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode>>, AutoCloseable {
                private boolean nextLoaded;
                private boolean hasNext;
                private boolean closed;

                @Override
                public boolean hasNext() {
                    if (!nextLoaded) {
                        try {
                            hasNext = resultSet.next();
                            nextLoaded = true;
                            if (!hasNext) {
                                close();
                            }
                        } catch (SQLException e) {
                            closeAfterFailure(e);
                            throw new IllegalStateException("Failed to iterate nodes", e);
                        }
                    }
                    return hasNext;
                }

                @Override
                public Map.Entry<UniqueNodeIdentifier<UUID>, AAroNNode> next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    nextLoaded = false;
                    try {
                        String key = resultSet.getString(1);
                        UniqueNodeIdentifier<UUID> identifier = new UniqueNodeIdentifierImpl(UUID.fromString(key));
                        AAroNNode node = NodeSerializer.deserialize(resultSet.getBytes(2));
                        return Map.entry(identifier, node);
                    } catch (SQLException e) {
                        closeAfterFailure(e);
                        throw new IllegalStateException("Failed to iterate nodes", e);
                    } catch (RuntimeException e) {
                        closeAfterFailure(e);
                        throw e;
                    }
                }

                @Override
                public void close() {
                    if (closed) {
                        return;
                    }
                    closed = true;
                    SQLException failure = null;
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        failure = e;
                    }
                    try {
                        pstmt.close();
                    } catch (SQLException e) {
                        if (failure == null) {
                            failure = e;
                        } else {
                            failure.addSuppressed(e);
                        }
                    }
                    if (failure != null) {
                        throw new IllegalStateException("Failed to close node iterator", failure);
                    }
                }

                private void closeAfterFailure(Throwable originalException) {
                    try {
                        close();
                    } catch (IllegalStateException closeException) {
                        originalException.addSuppressed(closeException);
                    }
                }
            }

            var iterator = new NodeEntryIterator();
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false)
                    .onClose(iterator::close);
        } catch (SQLException e) {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException closeException) {
                    e.addSuppressed(closeException);
                }
            }
            throw new IllegalStateException("Failed to iterate nodes", e);
        }
    }

    private List<AAroNEdge> getEdges() {
        String sql = "SELECT value FROM edges";
        try (var pstmt = connection.prepareStatement(sql);
             var resultSet = pstmt.executeQuery()) {
            List<AAroNEdge> edges = new ArrayList<>();
            while (resultSet.next()) {
                edges.add(EdgeSerializer.deserialize(resultSet.getBytes(1)));
            }
            return edges;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to iterate edges", e);
        }
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

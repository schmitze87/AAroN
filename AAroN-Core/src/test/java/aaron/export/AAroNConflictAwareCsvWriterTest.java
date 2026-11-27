package aaron.export;

import aaron.logging.Logger;
import aaron.model.*;
import aaron.model.stores.InHeapModelStore;
import aaron.sparx.SparxSQLiteConverter;
import aaron.sparx.config.Config;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AAroNConflictAwareCsvWriterTest {

    @TempDir
    Path tempDir;

    @Disabled
    @Test
    void testQEABig_InHeapModelStore() throws IOException, SQLException {
        Logger logger = new TestLogger(LoggerFactory.getLogger(AAroNCsvWriterTest.class));
        URL resource = ClassLoader.getSystemResource("BigModel.qea");
        File file = new File(resource.getFile());
        Map<String, Object> config = new HashMap<>();
        config.put("taggedValues", "AS_PROPERTY");
        Model model = new Model.Builder()
                .workingDir(new File("."))
                .modelStore(new InHeapModelStore())
                .build();
        Converter converter = new SparxSQLiteConverter(model, Config.createFromMap(config), file, logger);
        model = converter.convert();
        File nodesFile = new File("nodes.csv");
        File edgesFile = new File("edges.csv");
        System.out.println(nodesFile.getAbsoluteFile());
        AAroNConflictAwareCsvWriter writer = new AAroNConflictAwareCsvWriter();
        writer.write(model, nodesFile, edgesFile, false);
        model.close();
    }

    @Test
    void addsNonConflictingColumnsToOneNodeHeader() throws IOException {
        Model model = new Model.Builder().build();
        model.addNode(new UniqueNodeIdentifierImpl(), AAroNNode.builder()
                .addLabel("First")
                .addProperty("name", PropertyType.STRING, "first")
                .build());
        model.addNode(new UniqueNodeIdentifierImpl(), AAroNNode.builder()
                .addLabel("Second")
                .addProperty("datum", PropertyType.LOCALDATE, LocalDate.of(2026, 1, 2))
                .build());
        model.addNode(new UniqueNodeIdentifierImpl(), AAroNNode.builder()
                .addLabel("Third")
                .addProperty("count", PropertyType.INTEGER, 3)
                .build());

        AAroNConflictAwareCsvWriter writer = new AAroNConflictAwareCsvWriter();
        AAroNConflictAwareCsvWriter.CsvFiles files = writer.writeFiles(
                model, tempDir.resolve("nodes.csv").toFile(), null, false);

        assertEquals(List.of("nodes.csv"), fileNames(files.nodeFiles()));
        List<String> lines = Files.readAllLines(files.nodeFiles().getFirst().toPath());
        assertEquals("\":ID\",\":LABEL\",\"count:long\",\"datum:date\",\"name:string\"", lines.getFirst());
        assertEquals(4, lines.size());
        assertEquals(3, writer.getNodesCount());
        model.close();
    }

    @Test
    void keepsManyDifferentNonConflictingColumnsInOneFile() throws IOException {
        Model model = new Model.Builder().build();
        for (int i = 0; i < 250; i++) {
            model.addNode(new UniqueNodeIdentifierImpl(), AAroNNode.builder()
                    .addLabel("Node")
                    .addProperty("property_" + i, PropertyType.STRING, "value_" + i)
                    .build());
        }

        AAroNConflictAwareCsvWriter.CsvFiles files = new AAroNConflictAwareCsvWriter().writeFiles(
                model, tempDir.resolve("many-nodes.csv").toFile(), null, false);

        assertEquals(List.of("many-nodes.csv"), fileNames(files.nodeFiles()));
        List<String> lines = Files.readAllLines(files.nodeFiles().getFirst().toPath());
        assertEquals(251, lines.size());
        assertEquals(252, lines.getFirst().split(",").length);
        model.close();
    }

    @Test
    void createsNewNodeHeaderOnlyForConflictingPropertyType() throws IOException {
        Model model = new Model.Builder().build();
        model.addNode(new UniqueNodeIdentifierImpl(), AAroNNode.builder()
                .addLabel("Date")
                .addProperty("datum", PropertyType.LOCALDATE, LocalDate.of(2026, 1, 2))
                .addProperty("name", PropertyType.STRING, "date")
                .build());
        model.addNode(new UniqueNodeIdentifierImpl(), AAroNNode.builder()
                .addLabel("DateTime")
                .addProperty("datum", PropertyType.LOCALDATETIME, LocalDateTime.of(2026, 1, 2, 3, 4))
                .addProperty("description", PropertyType.STRING, "datetime")
                .build());
        model.addNode(new UniqueNodeIdentifierImpl(), AAroNNode.builder()
                .addLabel("Compatible")
                .addProperty("name", PropertyType.STRING, "compatible")
                .addProperty("additional", PropertyType.BOOLEAN, true)
                .build());

        AAroNConflictAwareCsvWriter writer = new AAroNConflictAwareCsvWriter();
        List<AAroNConflictAwareCsvWriter.CsvHeaderDefinition> definitions =
                writer.createNodeHeaderDefinitions(model, false);
        AAroNConflictAwareCsvWriter.CsvFiles files = writer.writeFiles(
                model, tempDir.resolve("nodes.csv").toFile(), null, false);

        assertEquals(2, definitions.size());
        assertTrue(definitions.stream().allMatch(definition ->
                definition.getSupportedPropertyDefinitions().stream()
                        .filter(property -> property.name().equals("datum"))
                        .count() == 1));
        assertEquals(1, definitions.stream()
                .filter(definition -> definition.supports(
                        new PropertyDefinition("datum", PropertyType.LOCALDATE)))
                .count());
        assertEquals(1, definitions.stream()
                .filter(definition -> definition.supports(
                        new PropertyDefinition("datum", PropertyType.LOCALDATETIME)))
                .count());
        assertEquals(List.of("nodes_1.csv", "nodes_2.csv"), fileNames(files.nodeFiles()));
        List<List<String>> contents = read(files.nodeFiles());
        assertEquals(Set.of("\"datum:date\"", "\"datum:localdatetime\""), contents.stream()
                .map(lines -> datumHeader(lines.getFirst()))
                .collect(Collectors.toSet()));
        assertEquals(List.of(2, 3), contents.stream().map(List::size).sorted().toList());
        assertTrue(contents.stream().allMatch(lines -> lines.getFirst().startsWith("\":ID\",\":LABEL\"")));
        model.close();
    }

    @Test
    void createsConflictAwareEdgeHeadersWithThreeFixedColumns() throws IOException {
        Model model = new Model.Builder().build();
        UniqueNodeIdentifierImpl first = new UniqueNodeIdentifierImpl();
        UniqueNodeIdentifierImpl second = new UniqueNodeIdentifierImpl();
        model.addNode(first, AAroNNode.builder().addLabel("Node").build());
        model.addNode(second, AAroNNode.builder().addLabel("Node").build());
        model.addEdge(new UniqueEdgeIdentifierImpl(), edge(first, second)
                .addProperty("datum", PropertyType.LOCALDATE, LocalDate.of(2026, 1, 2))
                .build());
        model.addEdge(new UniqueEdgeIdentifierImpl(), edge(second, first)
                .addProperty("datum", PropertyType.LOCALDATETIME, LocalDateTime.of(2026, 1, 2, 3, 4))
                .build());

        AAroNConflictAwareCsvWriter.CsvFiles files = new AAroNConflictAwareCsvWriter().writeFiles(
                model, tempDir.resolve("nodes.csv").toFile(), tempDir.resolve("edges.csv").toFile(), false);

        assertEquals(List.of("edges_1.csv", "edges_2.csv"), fileNames(files.edgeFiles()));
        List<List<String>> contents = read(files.edgeFiles());
        assertEquals(Set.of("\"datum:date\"", "\"datum:localdatetime\""), contents.stream()
                .map(lines -> lines.getFirst().split(",")[3])
                .collect(Collectors.toSet()));
        assertTrue(contents.stream().allMatch(lines -> lines.getFirst().startsWith("\":START_ID\",\":TYPE\",\":END_ID\"")));
        assertTrue(contents.stream().allMatch(lines -> lines.size() == 2));
        model.close();
    }

    private static AAroNEdge.Builder edge(UniqueNodeIdentifierImpl start, UniqueNodeIdentifierImpl end) {
        return AAroNEdge.builder().setType("RELATED").setStart(start).setEnd(end);
    }

    private static List<String> fileNames(List<File> files) {
        return files.stream().map(File::getName).toList();
    }

    private static String datumHeader(String header) {
        return Arrays.stream(header.split(","))
                .filter(column -> column.startsWith("\"datum:"))
                .findFirst()
                .orElseThrow();
    }

    private static List<List<String>> read(List<File> files) {
        return files.stream().map(file -> {
            try {
                return Files.readAllLines(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}

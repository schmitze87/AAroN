package aaron.sparx.processors;

import aaron.export.TestLogger;
import aaron.logging.Logger;
import aaron.model.*;
import aaron.sparx.identifiers.ConnectorGUID;
import aaron.sparx.identifiers.ObjectGUID;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static aaron.model.PropertyType.STRING;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class XRefProcessorTest {

    private XRefProcessor newProcessor(Model model) {
        Logger logger = new TestLogger(LoggerFactory.getLogger(XRefProcessorTest.class));
        return new XRefProcessor("sha1", LocalDateTime.now(), model, new ImportConext(), logger);
    }

    private Map<String, Object> stereotypeRow(String client, String description) {
        Map<String, Object> row = new HashMap<>();
        row.put("type", "element property");
        row.put("name", "Stereotypes");
        row.put("client", client);
        row.put("description", description);
        return row;
    }

    @Test
    void elementStereotypeWithNullDescriptionDoesNotThrow() throws IOException {
        Model.Builder modelBuilder = new Model.Builder();
        Model model = modelBuilder.build();
        String client = "node-1";
        ObjectGUID objectGUID = new ObjectGUID(client);
        UniqueNodeIdentifier<UUID> uniqueNodeIdentifier = new UniqueNodeIdentifierImpl();
        model.addNode(uniqueNodeIdentifier, AAroNNode.builder().build());
        model.addNodeIdentifier(objectGUID, uniqueNodeIdentifier);

        XRefProcessor processor = newProcessor(model);

        assertDoesNotThrow(() -> processor.process(stereotypeRow(client, null)));
        model.close();
    }

    @Test
    void connectorStereotypeWithNullDescriptionDoesNotThrow() throws IOException {
        Model.Builder modelBuilder = new Model.Builder();
        Model model = modelBuilder.build();
        String client = "edge-1";
        UniqueEdgeIdentifier<UUID> uniqueEdgeIdentifier = new UniqueEdgeIdentifierImpl();
        ConnectorGUID connectorGUID = new ConnectorGUID(client);
        model.addEdge(uniqueEdgeIdentifier, AAroNEdge.builder().build());
        model.addEdgeIdentifier(connectorGUID, uniqueEdgeIdentifier);

        XRefProcessor processor = newProcessor(model);

        Map<String, Object> row = new HashMap<>();
        row.put("type", "connector property");
        row.put("name", "Stereotypes");
        row.put("client", client);
        row.put("description", null);

        assertDoesNotThrow(() -> processor.process(row));
        model.close();
    }

    @Test
    void connectorStereotypeWithMatchingNameSetsFqStereotype() throws IOException {
        Model.Builder modelBuilder = new Model.Builder();
        Model model = modelBuilder.build();
        String client = "edge-2";
        AAroNEdge edge = AAroNEdge.builder()
                .addProperty("stereotype", STRING, "Foo")
                .build();
        UniqueEdgeIdentifier<UUID> uniqueEdgeIdentifier = new UniqueEdgeIdentifierImpl();
        ConnectorGUID connectorGUID = new ConnectorGUID(client);
        model.addEdge(uniqueEdgeIdentifier, edge);
        model.addEdgeIdentifier(connectorGUID, uniqueEdgeIdentifier);

        XRefProcessor processor = newProcessor(model);

        Map<String, Object> row = new HashMap<>();
        row.put("type", "connector property");
        row.put("name", "Stereotypes");
        row.put("client", client);
        row.put("description", "@STEREO;Name=Foo;FQName=ns::Foo;@ENDSTEREO;");

        processor.process(row);

        // Die AAroNEdge edge ist nicht die Referenz auf das Objekt, an dem der Property gesetzt wird.
        // Da das Objekt serialisiert und wieder deserialisiert wird, bricht hier die Referenzierung.
        AAroNEdge extendedEdge = model.getEdge(uniqueEdgeIdentifier);
        assertEquals("ns::Foo", extendedEdge.getProperty(STRING, "fqStereotype"));
        model.close();
    }

    @Test
    void elementStereotypeWithMatchingNameSetsFqStereotype() throws IOException {
        Model.Builder modelBuilder = new Model.Builder();
        Model model = modelBuilder.build();
        String client = "node-2";
        AAroNNode node = AAroNNode.builder()
                .addProperty("stereotype", STRING, "Foo")
                .build();
        UniqueNodeIdentifier<UUID> uniqueNodeIdentifier = new UniqueNodeIdentifierImpl();
        ObjectGUID objectGUID = new ObjectGUID(client);
        model.addNode(uniqueNodeIdentifier, node);
        model.addNodeIdentifier(objectGUID, uniqueNodeIdentifier);

        XRefProcessor processor = newProcessor(model);

        Map<String, Object> row = stereotypeRow(
                client, "@STEREO;Name=Foo;FQName=ns::Foo;@ENDSTEREO;");

        processor.process(row);

        // Die AAroNNode node ist nicht die Referenz auf das Objekt, an dem der Property gesetzt wird.
        // Da das Objekt serialisiert und wieder deserialisiert wird, bricht hier die Referenzierung.
        AAroNNode extendedNode = model.getNode(uniqueNodeIdentifier);
        assertEquals("ns::Foo", extendedNode.getProperty(STRING, "fqStereotype"));
        model.close();
    }

    @Test
    void elementStereotypeWithNonMatchingDescriptionSetsNothing() throws IOException {
        Model.Builder modelBuilder = new Model.Builder();
        Model model = modelBuilder.build();
        String client = "node-3";
        AAroNNode node = AAroNNode.builder()
                .addProperty("stereotype", STRING, "Foo")
                .build();
        UniqueNodeIdentifier<UUID> uniqueNodeIdentifier = new UniqueNodeIdentifierImpl();
        ObjectGUID objectGUID = new ObjectGUID(client);
        model.addNode(uniqueNodeIdentifier, node);
        model.addNodeIdentifier(objectGUID, uniqueNodeIdentifier);

        XRefProcessor processor = newProcessor(model);

        processor.process(stereotypeRow(client, "kein-stereotyp-token"));

        // Die AAroNNode node ist nicht die Referenz auf das Objekt, an dem der Property gesetzt wird.
        // Da das Objekt serialisiert und wieder deserialisiert wird, bricht hier die Referenzierung.
        AAroNNode extendedNode = model.getNode(uniqueNodeIdentifier);
        assertFalse(extendedNode.getProperties().containsKey("fqStereotype"));
        model.close();
    }
}

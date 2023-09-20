package aaron.sparx.processors;

import aaron.model.*;
import aaron.sparx.identifiers.ConnectorId;
import aaron.sparx.identifiers.DiagramId;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.model.EADiagramLink;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static aaron.model.PropertyType.*;

public class DiagramLinksProcessor extends AbstractProcessor{

    public DiagramLinksProcessor(String sha1, LocalDateTime time, Model model, ImportConext context) {
        super(sha1, time, model, context);
    }

    @Override
    public void process(final Map<String, Object> row) {
        Integer diagramId = EADiagramLink.DIAGRAMID.value(row);
        Integer connectorId = EADiagramLink.ConnectorID.value(row);
//        String geometry = EADiagramLink.GEOMETRY.value(row);
//        String style = EADiagramLink.STYLE.value(row);
        Boolean hidden = EADiagramLink.HIDDEN.value(row);
//        String path = EADiagramLink.PATH.value(row);

        DiagramId diagramIdentifier = new DiagramId(diagramId);
        ConnectorId connectorIdentifier = new ConnectorId(connectorId);

        AAroNNode diagramNode = model.getNode(diagramIdentifier);
        String diagramGuid = diagramNode.getProperty(STRING, "eaGuid");

        AAroNEdge connectorEdge = model.getEdge(connectorIdentifier);
        if (diagramGuid != null && connectorEdge != null) {
            Identifier start = connectorEdge.getStart();
            Identifier end = connectorEdge.getEnd();
            String eaGuid = connectorEdge.getProperty(STRING, "eaGuid");

            AAroNNode startNode = model.getNode(start);
            AAroNNode endNode = model.getNode(end);

            if (startNode != null && endNode != null) {
                AAroNEdge diagramlinkEdge = AAroNEdge.builder()
                        .setType("DIAGRAMLINK")
                        .setStart(start)
                        .setEnd(end)
                        .addProperty("connectorGuid", STRING, eaGuid)
                        .addProperty("diagramGuid", STRING, diagramGuid)
                        .addProperty("hidden", BOOLEAN, hidden)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), diagramlinkEdge);
            }
        }
    }


}

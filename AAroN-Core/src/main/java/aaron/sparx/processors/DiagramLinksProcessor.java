package aaron.sparx.processors;

import aaron.logging.Logger;
import aaron.model.*;
import aaron.sparx.identifiers.ConnectorId;
import aaron.sparx.identifiers.DiagramId;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.model.EADiagramLink;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.model.PropertyType.*;

public class DiagramLinksProcessor extends AbstractProcessor{

    public DiagramLinksProcessor(String sha1, LocalDateTime time, Model model, ImportConext context, Logger logger) {
        super(sha1, time, model, context, logger);
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
        AAroNEdge connectorEdge = model.getEdge(connectorIdentifier);

        if (diagramNode != null && connectorEdge != null) {

            String diagramGuid = diagramNode.getProperty(STRING, "eaGuid");
            String eaGuid = connectorEdge.getProperty(STRING, "eaGuid");

            Identifier start = connectorEdge.getStart();
            Identifier end = connectorEdge.getEnd();
            AAroNNode startNode = model.getNode(start);
            AAroNNode endNode = model.getNode(end);

            if (diagramGuid != null && eaGuid != null && startNode != null && endNode != null) {
                AAroNEdge diagramlinkEdge = AAroNEdge.builder()
                        .setType("DIAGRAMLINK")
                        .setStart(start)
                        .setEnd(end)
                        .addProperty("connectorGuid", STRING, eaGuid)
                        .addProperty("diagramGuid", STRING, diagramGuid)
                        .addProperty("hidden", BOOLEAN, hidden)
                        .build();
                model.addEdge(new ImplizitRelationId(), diagramlinkEdge);
            } else {
                logger.warn("Could not add DiagramLink with diagramId:'{}' and connectorId: '{}'.", diagramId, connectorId);
            }
        }
    }


}

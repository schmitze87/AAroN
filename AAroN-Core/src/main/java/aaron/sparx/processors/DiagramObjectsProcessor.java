package aaron.sparx.processors;

import aaron.model.AAroNEdge;
import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.sparx.identifiers.DiagramId;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.ObjectId;
import aaron.sparx.model.EADiagramObject;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static aaron.model.PropertyType.*;

public class DiagramObjectsProcessor extends AbstractProcessor{

    public DiagramObjectsProcessor(String sha1, LocalDateTime time, Model model, ImportConext context) {
        super(sha1, time, model, context);
    }

    @Override
    public void process(final Map<String, Object> row) {
        Integer diagramId = EADiagramObject.DIAGRAM_ID.value(row);
        Integer objectId = EADiagramObject.OBJECT_ID.value(row);
        Integer rectTop = EADiagramObject.RECT_TOP.value(row);
        Integer rectLeft = EADiagramObject.RECT_LEFT.value(row);
        Integer rectRight = EADiagramObject.RECT_RIGHT.value(row);
        Integer rectBottom = EADiagramObject.RECT_BOTTOM.value(row);
        Integer sequence = EADiagramObject.SEQUENCE.value(row);
        String objectStyle = EADiagramObject.OBJECT_STYLE.value(row);

        DiagramId diagramIdentifier = new DiagramId(diagramId);
        ObjectId objectIdentifier = new ObjectId(objectId);

        AAroNEdge showsEdge = AAroNEdge.builder()
                .setType("SHOWS")
                .setStart(diagramIdentifier)
                .setEnd(objectIdentifier)
                .addProperty("eapHash", STRING, sha1)
                .addProperty("importedAt", LOCALDATETIME, time)
                .addProperty("rectTop", INTEGER, rectTop)
                .addProperty("rectLeft", INTEGER, rectLeft)
                .addProperty("rectRight", INTEGER, rectRight)
                .addProperty("rectBottom", INTEGER, rectBottom)
                .addProperty("sequence", INTEGER, sequence)
                .addProperty("objectStyle", STRING, objectStyle)
                .build();
        model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), showsEdge);
    }


}

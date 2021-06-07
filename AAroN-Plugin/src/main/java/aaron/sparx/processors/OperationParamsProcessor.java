package aaron.sparx.processors;

import aaron.model.AAroNNode;
import aaron.model.AAroNEdge;
import aaron.model.Model;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.OperationId;
import aaron.sparx.identifiers.OperationParamGUID;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.model.PropertyType.*;
import static aaron.sparx.model.EAOperationParams.*;

public class OperationParamsProcessor extends AbstractProcessor{

    public OperationParamsProcessor(String sha1, LocalDateTime time, Model model) {
        super(sha1, time, model);
    }

    @Override
    public void process(final Map<String, Object> row) {
        Integer operationId = OPERATION_ID.value(row);
        String name = NAME.value(row);
        String type = TYPE.value(row);
        String _default = DEFAULT.value(row);
        String notes = NOTES.value(row);
        Integer pos = POS.value(row);
        Boolean _const = CONST.value(row);
        String style = STYLE.value(row);
        String kind = KIND.value(row);
        String classifier = CLASSIFIER.value(row);
        String eaGuid = EA_GUID.value(row);
        String styleEx = STYLE_EX.value(row);

        AAroNNode node = AAroNNode.builder()
                .addLabel("OPERATION_PARAM")
                .addProperty("name", STRING, name)
                .addProperty("type", STRING, type)
                .addProperty("default", STRING, _default)
                .addProperty("notes", STRING, notes)
                .addProperty("pos", INTEGER, pos)
                .addProperty("const", BOOLEAN, _const)
                .addProperty("style", STRING, style)
                .addProperty("kind", STRING, kind)
                .addProperty("eaGuid", STRING, eaGuid)
                .addProperty("styleEx", STRING, styleEx)
                .addProperty("eapHash", STRING, sha1)
                .addProperty("importedAt", LOCALDATETIME, time)
                .build();

        OperationParamGUID operationParamGUID = new OperationParamGUID(eaGuid);
        model.addNode(operationParamGUID, node);

        OperationId operationIdentifier = new OperationId(operationId);
        AAroNEdge edge = AAroNEdge.builder()
                .setType("HAS_PARAM")
                .setStart(operationIdentifier)
                .setEnd(operationParamGUID)
                .addProperty("eapHash", STRING, sha1)
                .addProperty("importedAt", LOCALDATETIME, time)
                .build();
        model.addEdge(new ImplizitRelationId(), edge);
    }
}


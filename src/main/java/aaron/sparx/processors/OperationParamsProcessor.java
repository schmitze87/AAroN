package aaron.sparx.processors;

import aaron.model.AAroNNode;
import aaron.model.Edge;
import aaron.model.Model;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.OperationId;
import aaron.sparx.identifiers.OperationParamGUID;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.sparx.model.EAOperationParams.*;

public class OperationParamsProcessor extends AbstractProcessor{

    public OperationParamsProcessor(String sha1, LocalDateTime time, Model model) {
        super(sha1, time, model);
    }

    @Override
    public <U extends Map<String, Object>> void process(U row) {
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
                .addProperty("name", name)
                .addProperty("type", type)
                .addProperty("default", _default)
                .addProperty("notes", notes)
                .addProperty("pos", pos)
                .addProperty("const", _const)
                .addProperty("style", style)
                .addProperty("kind", kind)
                .addProperty("eaGuid", eaGuid)
                .addProperty("styleEx", styleEx)
                .addProperty("eapHash", sha1)
                .addProperty("importedAt", time)
                .build();

        OperationParamGUID operationParamGUID = new OperationParamGUID(eaGuid);
        model.addNode(operationParamGUID, node);

        OperationId operationIdentifier = new OperationId(operationId);
        Edge edge = Edge.builder()
                .setType("HAS_PARAM")
                .setStart(operationIdentifier)
                .setEnd(operationParamGUID)
                .addProperty("eapHash", sha1)
                .addProperty("importedAt", time)
                .build();
        model.addEdge(new ImplizitRelationId(), edge);
    }
}


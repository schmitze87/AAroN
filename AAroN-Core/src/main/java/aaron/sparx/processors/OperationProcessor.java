package aaron.sparx.processors;

import aaron.model.AAroNEdge;
import aaron.model.AAroNNode;
import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.ObjectId;
import aaron.sparx.identifiers.OperationGUID;
import aaron.sparx.identifiers.OperationId;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static aaron.model.PropertyType.*;
import static aaron.sparx.model.EAOperation.*;

public class OperationProcessor extends AbstractProcessor {

    public OperationProcessor(String sha1, LocalDateTime time, Model model, ImportConext context) {
        super(sha1, time, model, context);
    }

    @Override
    public void process(final Map<String, Object> row) {
        Integer operationId = OPERATION_ID.value(row);
        Integer objectId = OBJECT_ID.value(row);
        String name = NAME.value(row);
        String scope = SCOPE.value(row);
        String type = TYPE.value(row);
        Boolean returnArray = RETURN_ARRAY.value(row);
        String stereotype = STEREOTYPE.value(row);
        Boolean isStatic = IS_STATIC.value(row);
        String concurrency = CONCURRENCY.value(row);
        String notes = NOTES.value(row);
        String behaviour = BEHAVIOUR.value(row);
        Boolean _abstract = ABSTRACT.value(row);
        String genOption = GEN_OPTION.value(row);
        String _synchronized = SYNCHRONIZED.value(row);
        Integer pos = POS.value(row);
        Boolean _const = CONST.value(row);
        String style = STYLE.value(row);
        Boolean pure = PURE.value(row);
        String _throws = THROWS.value(row);
        String classifier = CLASSIFIER.value(row);
        String code = CODE.value(row);
        Boolean isRoot = IS_ROOT.value(row);
        Boolean isLeaf = IS_LEAF.value(row);
        Boolean isQuery = IS_QUERY.value(row);
        String stateFlags = STATE_FLAGS.value(row);
        String eaGuid = GUIDHelper.unwrapGuid(EA_GUID.value(row));
        String styleEx = STYLE_EX.value(row);

        OperationId operationIdentifier = new OperationId(operationId);
        OperationGUID operationGUID = new OperationGUID(eaGuid);
        ObjectId objectIdentifier = new ObjectId(objectId);

        AAroNNode node = AAroNNode.builder().build();
        node.addLabel("Operation");
        node.addProperty("name", STRING, name);
        node.addProperty("scope", STRING, scope);
        node.addProperty("type", STRING, type);
        node.addProperty("returnArray", BOOLEAN, returnArray);
        node.addProperty("stereotype", STRING, stereotype);
        node.addProperty("isStatic", BOOLEAN, isStatic);
        node.addProperty("concurrency", STRING, concurrency);
        node.addProperty("notes", STRING, notes);
        node.addProperty("behaviour", STRING, behaviour);
        node.addProperty("abstract", BOOLEAN, _abstract);
        node.addProperty("genOption", STRING, genOption);
        node.addProperty("synchronized", STRING, _synchronized);
        node.addProperty("pos", INTEGER, pos);
        node.addProperty("const", BOOLEAN, _const);
        node.addProperty("style", STRING, style);
        node.addProperty("pure", BOOLEAN, pure);
        node.addProperty("throws", STRING, _throws);
        node.addProperty("code", STRING, code);
        node.addProperty("isRoot", BOOLEAN, isRoot);
        node.addProperty("isLeaf", BOOLEAN, isLeaf);
        node.addProperty("isQuery", BOOLEAN, isQuery);
        node.addProperty("stateFlags", STRING, stateFlags);
        node.addProperty("eaGuid", STRING, eaGuid);
        node.addProperty("styleEx", STRING, styleEx);
        node.addProperty("eapHash", STRING, sha1);
        node.addProperty("importedAt", LOCALDATETIME, time);

        model.addNode(operationIdentifier, node);
        model.addNode(operationGUID, node);

        //TODO: Classifier

        //RELATIONSHIP TO OBJECT
        AAroNEdge edge = AAroNEdge.builder()
                .setType("HAS_OPERATION")
                .setStart(objectIdentifier)
                .setEnd(operationGUID)
                .addProperty("eapHash", STRING, sha1)
                .addProperty("importedAt", LOCALDATETIME, time)
                .build();
        model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), edge);
    }
}

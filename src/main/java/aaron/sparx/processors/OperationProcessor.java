package aaron.sparx.processors;

import aaron.model.AAroNNode;
import aaron.model.Edge;
import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.ObjectId;
import aaron.sparx.identifiers.OperationGUID;
import aaron.sparx.identifiers.OperationId;

import java.util.Map;
import java.util.UUID;

import static aaron.sparx.model.EAOperation.*;

public class OperationProcessor implements Processor{

    final Model model;

    public OperationProcessor(final Model model) {
        this.model = model;
    }

    @Override
    public <U extends Map<String, Object>> void process(U row) {
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
        Integer _const = CONST.value(row);
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
        ObjectId objectIdentifier= new ObjectId(objectId);

        AAroNNode node = AAroNNode.builder().build();
        node.addLabel("Operation");
        node.addProperty("name", name);
        node.addProperty("scope", scope);
        node.addProperty("type", type);
        node.addProperty("returnArray", returnArray);
        node.addProperty("stereotype", stereotype);
        node.addProperty("isStatic", isStatic);
        node.addProperty("concurrency", concurrency);
        node.addProperty("notes", notes);
        node.addProperty("behaviour", behaviour);
        node.addProperty("abstract", _abstract);
        node.addProperty("genOption", genOption);
        node.addProperty("synchronized", _synchronized);
        node.addProperty("pos", pos);
        node.addProperty("const", _const);
        node.addProperty("style", style);
        node.addProperty("pure", pure);
        node.addProperty("throws", _throws);
        node.addProperty("code", code);
        node.addProperty("isRoot", isRoot);
        node.addProperty("isLeaf", isLeaf);
        node.addProperty("isQuery", isQuery);
        node.addProperty("stateFlags", stateFlags);
        node.addProperty("eaGuid", eaGuid);
        node.addProperty("styleEx", styleEx);

        model.addNode(operationIdentifier, node);
        model.addNode(operationGUID, node);

        //TODO: Classifier

        //RELATIONSHIP TO OBJECT
        Edge edge = Edge.builder()
                .setType("HAS_OPERATION")
                .setStart(objectIdentifier)
                .setEnd(operationGUID)
                .build();
        model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), edge);
    }
}

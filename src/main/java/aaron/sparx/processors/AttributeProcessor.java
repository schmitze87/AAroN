package aaron.sparx.processors;

import aaron.model.AAroNNode;
import aaron.model.Edge;
import aaron.model.Model;
import aaron.sparx.identifiers.AttributeGUID;
import aaron.sparx.identifiers.AttributeId;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.ObjectId;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static aaron.sparx.model.EAAttribute.*;

public class AttributeProcessor extends AbstractProcessor{


    public AttributeProcessor(String sha1, LocalDateTime time, Model model) {
        super(sha1, time, model);
    }

    @Override
    public void process(final Map<String, Object> row) {
        Integer objectId = OBJECT_ID.value(row);
        String name = NAME.value(row);
        String scope = SCOPE.value(row);
        String stereotype = STEREOTYPE.value(row);
        String containment = CONTAINMENT.value(row);
        Boolean isStatic = IS_STATIC.value(row);
        Boolean isCollection = IS_COLLECTION.value(row);
        Boolean isOrdered = IS_ORDERED.value(row);
        Boolean allowDuplicates = ALLOW_DUPLICATES.value(row);
        String lowerBound = LOWER_BOUND.value(row);
        String upperBound = UPPER_BOUND.value(row);
        String container = CONTAINER.value(row);
        String notes = NOTES.value(row);
        String derived = DERIVED.value(row);
        Integer id = ID.value(row);
        Integer pos = POS.value(row);
        String genOptions = GEN_OPTIONS.value(row);
        Integer length = LENGTH.value(row);
        Integer precision = PRECISION.value(row);
        Integer scale = SCALE.value(row);
        Integer _const = CONST.value(row);
        String style = STYLE.value(row);
        String classifier = CLASSIFIER.value(row);
        String _default = DEFAULT.value(row);
        String type = TYPE.value(row);
        String eaGuid = EA_GUID.value(row);
        String styleEx = STYLE_EX.value(row);

        AAroNNode node = AAroNNode.builder()
                .addLabel("ATTRIBUTE")
                .addProperty("name", name)
                .addProperty("scope", scope)
                .addProperty("stereotype", stereotype)
                .addProperty("containment", containment)
                .addProperty("isStatic", isStatic)
                .addProperty("isCollection", isCollection)
                .addProperty("isOrdered", isOrdered)
                .addProperty("allowDuplicates", allowDuplicates)
                .addProperty("lowerBound", lowerBound)
                .addProperty("upperBound", upperBound)
                .addProperty("container", container)
                .addProperty("notes", notes)
                .addProperty("derived", derived)
                .addProperty("pos", pos)
                .addProperty("genOptions", genOptions)
                .addProperty("length", length)
                .addProperty("precision", precision)
                .addProperty("scala", scale)
                .addProperty("const", _const)
                .addProperty("style", style)
                .addProperty("default", _default)
                .addProperty("type", type)
                .addProperty("eaGuid", eaGuid)
                .addProperty("styleEx", styleEx)
                .addProperty("eapHash", sha1)
                .addProperty("importedAt", time)
                .build();

        if (StringUtils.isNotBlank(stereotype)) {
            node.addLabel(stereotype);
        }

        AttributeId attributeId = new AttributeId(id);
        AttributeGUID attributeGUID = new AttributeGUID(eaGuid);
        model.addNode(attributeId, node);
        model.addNode(attributeGUID, node);

        ObjectId objectIdentifier = new ObjectId(objectId);
        Edge objectEdge = Edge.builder()
                .setStart(objectIdentifier)
                .setEnd(attributeGUID)
                .setType("HAS_ATTRIBUTE")
                .addProperty("eapHash", sha1)
                .addProperty("importedAt", time)
                .build();
        model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), objectEdge);

        if (classifier != null && StringUtils.isNumeric(classifier)) {
            int classifierInt = Integer.parseInt(classifier);
            ObjectId classifierId = new ObjectId(classifierInt);
            Edge classifierEdge = Edge.builder()
                    .setStart(attributeGUID)
                    .setEnd(classifierId)
                    .setType("INSTANCE_OF")
                    .addProperty("eapHash", sha1)
                    .addProperty("importedAt", time)
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), classifierEdge);
        }

    }
}

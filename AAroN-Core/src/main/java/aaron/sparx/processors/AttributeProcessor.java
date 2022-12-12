package aaron.sparx.processors;

import aaron.model.AAroNEdge;
import aaron.model.AAroNNode;
import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.sparx.identifiers.AttributeGUID;
import aaron.sparx.identifiers.AttributeId;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.ObjectId;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static aaron.model.PropertyType.*;
import static aaron.sparx.model.EAAttribute.*;

public class AttributeProcessor extends AbstractProcessor {


    public AttributeProcessor(String sha1, LocalDateTime time, Model model, ImportConext context) {
        super(sha1, time, model, context);
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
        Boolean _const = CONST.value(row);
        String style = STYLE.value(row);
        String classifier = CLASSIFIER.value(row);
        String _default = DEFAULT.value(row);
        String type = TYPE.value(row);
        String eaGuid = EA_GUID.value(row);
        String styleEx = STYLE_EX.value(row);

        AAroNNode node = AAroNNode.builder()
                .addLabel("ATTRIBUTE")
                .addProperty("name", STRING, name)
                .addProperty("scope", STRING, scope)
                .addProperty("stereotype", STRING, stereotype)
                .addProperty("containment", STRING, containment)
                .addProperty("isStatic", BOOLEAN, isStatic)
                .addProperty("isCollection", BOOLEAN, isCollection)
                .addProperty("isOrdered", BOOLEAN, isOrdered)
                .addProperty("allowDuplicates", BOOLEAN, allowDuplicates)
                .addProperty("lowerBound", STRING, lowerBound)
                .addProperty("upperBound", STRING, upperBound)
                .addProperty("container", STRING, container)
                .addProperty("notes", STRING, notes)
                .addProperty("derived", STRING, derived)
                .addProperty("pos", INTEGER, pos)
                .addProperty("genOptions", STRING, genOptions)
                .addProperty("length", INTEGER, length)
                .addProperty("precision", INTEGER, precision)
                .addProperty("scala", INTEGER, scale)
                .addProperty("const", BOOLEAN, _const)
                .addProperty("style", STRING, style)
                .addProperty("default", STRING, _default)
                .addProperty("type", STRING, type)
                .addProperty("eaGuid", STRING, eaGuid)
                .addProperty("styleEx", STRING, styleEx)
                .addProperty("eapHash", STRING, sha1)
                .addProperty("importedAt", LOCALDATETIME, time)
                .build();

        if (StringUtils.isNotBlank(stereotype)) {
            node.addLabel(stereotype);
        }

        AttributeId attributeId = new AttributeId(id);
        AttributeGUID attributeGUID = new AttributeGUID(eaGuid);
        model.addNode(attributeId, node);
        model.addNode(attributeGUID, node);

        ObjectId objectIdentifier = new ObjectId(objectId);
        AAroNEdge objectEdge = AAroNEdge.builder()
                .setStart(objectIdentifier)
                .setEnd(attributeGUID)
                .setType("HAS_ATTRIBUTE")
                .addProperty("eapHash", STRING, sha1)
                .addProperty("importedAt", LOCALDATETIME, time)
                .build();
        model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), objectEdge);

        if (StringUtils.isNumeric(classifier)) {
            int classifierInt = Integer.parseInt(classifier);
            ObjectId classifierId = new ObjectId(classifierInt);
            AAroNEdge classifierEdge = AAroNEdge.builder()
                    .setStart(attributeGUID)
                    .setEnd(classifierId)
                    .setType("INSTANCE_OF")
                    .addProperty("eapHash", STRING, sha1)
                    .addProperty("importedAt", LOCALDATETIME, time)
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), classifierEdge);
        }

    }
}

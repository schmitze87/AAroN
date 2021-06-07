package aaron.sparx.processors;

import aaron.model.*;
import aaron.sparx.GUIDHelper;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.ObjectGUID;
import aaron.sparx.identifiers.ObjectId;
import aaron.sparx.identifiers.PackageId;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static aaron.model.PropertyType.*;
import static aaron.sparx.model.EAObject.*;

public class ObjectProcessor extends AbstractProcessor{

    public ObjectProcessor(String sha1, LocalDateTime time, Model model) {
        super(sha1, time, model);
    }

    public void process(final Map<String, Object> row) {
        Integer objectId = OBJECT_ID.value(row);
        String objectType = OBJECT_TYPE.value(row);
        Integer diagramId = DIAGRAM_ID.value(row);
        String name = NAME.value(row);
        String alias = ALIAS.value(row);
        String author = AUTHOR.value(row);
        String version = VERSION.value(row);
        String note = NOTE.value(row);
        Integer packageId = PACKAGE_ID.value(row);
        String stereotype = STEREOTYPE.value(row);
        Integer nType = NTYPE.value(row);
        String complexity = COMPLEXITY.value(row);
        Integer effort = EFFORT.value(row);
        String style = STYLE.value(row);
        Integer backColor = BACKCOLOR.value(row);
        Integer borderStyle = BORDER_STYLE.value(row);
        Integer borderWidth = BORDER_WIDTH.value(row);
        Integer fontColor = FONT_COLOR.value(row);
        Integer borderColor = BORDER_COLOR.value(row);
        LocalDateTime createdDate = CREATED_DATE.value(row);
        LocalDateTime modifiedDate = MODIFIED_DATE.value(row);
        String status = STATUS.value(row);
        String _abstract = ABSTRACT.value(row);
        Integer tagged = TAGGED.value(row);
        String pdata1 = PDATA1.value(row);
        String pdata2 = PDATA2.value(row);
        String pdata3 = PDATA3.value(row);
        String pdata4 = PDATA4.value(row);
        String pdata5 = PDATA5.value(row);
        String concurrency = CONCURRENCY.value(row);
        String visibility = VISIBILITY.value(row);
        String persistence = PERSISTENCE.value(row);
        String genType = GEN_TYPE.value(row);
        String genFile = GEN_FILE.value(row);
        String header1 = HEADER1.value(row);
        String header2 = HEADER2.value(row);
        String phase = PHASE.value(row);
        String scope = SCOPE.value(row);
        String genOption = GEN_OPTION.value(row);
        String genLinks = GEN_LINKS.value(row);
        Integer classifier = CLASSIFIER.value(row);
        String eaGuid = GUIDHelper.unwrapGuid(EA_GUID.value(row));
        Integer parentId = PARENT_ID.value(row);
        String runState = RUN_STATE.value(row);
        String classifierGuid = CLASSIFIER_GUID.value(row);
        Integer tPos = TPOS.value(row);
        Boolean isRoot = IS_ROOT.value(row);
        Boolean isLeaf = IS_LEAF.value(row);
        Boolean isSpec = IS_SPEC.value(row);
        Boolean isActive = IS_ACTIVE.value(row);
        String stateFlags = STATE_FLAGS.value(row);
        String packageFlags = PACKAGE_FLAGS.value(row);
        String multiplicity = MULTIPLICITY.value(row);
        String styleEx = STYLE_EX.value(row);
        String actionFlags = ACTION_FLAGS.value(row);
        String eventFlags = EVENT_FLAGS.value(row);

        AAroNNode node = null;
        if ("Package".equals(objectType) && StringUtils.isNumeric(pdata1)) {
            PackageId identifier = new PackageId(Integer.parseInt(pdata1));
            node = model.getNode(identifier);
        }
        if (node == null) {
            node = AAroNNode.builder().build();
        }
        node.addLabel(objectType);
        node.addLabel(stereotype);
        node.addProperty("objectType", STRING, objectType);
        node.addProperty("name", STRING, name);
        node.addProperty("alias", STRING, alias);
        node.addProperty("author", STRING, author);
        node.addProperty("version", STRING, version);
        node.addProperty("note", STRING, note);
        node.addProperty("stereotype", STRING, stereotype);
        node.addProperty("nType", INTEGER, nType);
        node.addProperty("complexity", STRING, complexity);
        node.addProperty("effort", INTEGER, effort);
        node.addProperty("style", STRING, style);
        node.addProperty("backColor", INTEGER, backColor);
        node.addProperty("borderStyle", INTEGER, borderStyle);
        node.addProperty("borderWidth", INTEGER, borderWidth);
        node.addProperty("fontColor", INTEGER, fontColor);
        node.addProperty("borderColor", INTEGER, borderColor);
        node.addProperty("createdDate", LOCALDATETIME, createdDate);
        node.addProperty("modifiedDate", LOCALDATETIME, modifiedDate);
        node.addProperty("status", STRING, status);
        node.addProperty("abstract", STRING, _abstract);
        node.addProperty("tagged", INTEGER, tagged);
        node.addProperty("concurrency", STRING, concurrency);
        node.addProperty("visibility", STRING, visibility);
        node.addProperty("persistence", STRING, persistence);
        node.addProperty("genType", STRING, genType);
        node.addProperty("genFile", STRING, genFile);
        node.addProperty("header1", STRING, header1);
        node.addProperty("header2", STRING, header2);
        node.addProperty("phase", STRING, phase);
        node.addProperty("scope", STRING, scope);
        node.addProperty("genOption", STRING, genOption);
        node.addProperty("genLinks", STRING, genLinks);
        node.addProperty("eaGuid", STRING, eaGuid);
        node.addProperty("runState", STRING, runState);
        node.addProperty("tPos", INTEGER, tPos);
        node.addProperty("isRoot", BOOLEAN, isRoot);
        node.addProperty("isLeaf", BOOLEAN, isLeaf);
        node.addProperty("isSpec", BOOLEAN, isSpec);
        node.addProperty("isActive", BOOLEAN, isActive);
        node.addProperty("stateFlags", STRING, stateFlags);
        node.addProperty("packageFlags", STRING, packageFlags);
        node.addProperty("multiplicity", STRING, multiplicity);
        node.addProperty("styleEx", STRING, styleEx);
        node.addProperty("actionFlags", STRING, actionFlags);
        node.addProperty("eventFlags", STRING, eventFlags);

        node.addProperty("eapHash", STRING, sha1);
        node.addProperty("importedAt", LOCALDATETIME, time);

        ObjectId objectIdentifier = new ObjectId(objectId);
        ObjectGUID objectGUID = new ObjectGUID(eaGuid);
        model.addNode(objectIdentifier, node);
        model.addNode(objectGUID, node);

        //****************************************
        //Start creating implicit relationships
        //****************************************

        //CONTAINS
        PackageId packageIdIdentifier = new PackageId(packageId);
            AAroNEdge containsEdge = AAroNEdge.builder()
                    .setStart(packageIdIdentifier)
                    .setEnd(objectIdentifier)
                    .setType("CONTAINS")
                    .addProperty("connectorType", STRING, "CONTAINS")
                    .addProperty("eapHash", STRING, sha1)
                    .addProperty("importedAt", LOCALDATETIME, time)
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), containsEdge);

        //BEHAVIOUR
        if ("Action".equals(objectType)) {
            if (GUIDHelper.isEaGuid(pdata1)) {
                ObjectGUID behaviourGUID = new ObjectGUID(GUIDHelper.unwrapGuid(pdata1));
                AAroNEdge behaviourEdge = AAroNEdge.builder()
                        .setStart(objectIdentifier)
                        .setEnd(behaviourGUID)
                        .setType("BEHAVIOUR")
                        .addProperty("connectorType", STRING, "BEHAVIOUR")
                        .addProperty("eapHash", STRING, sha1)
                        .addProperty("importedAt", LOCALDATETIME, time)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), behaviourEdge);
            }
        }

        //CLASSIFIER
        if ("Object".equals(objectType) || "Action".equals(objectType)) {
            if (GUIDHelper.isEaGuid(classifierGuid)) {
                ObjectGUID classifierGUID = new ObjectGUID(GUIDHelper.unwrapGuid(classifierGuid));
                AAroNEdge classifierEdge = AAroNEdge.builder()
                        .setStart(objectIdentifier)
                        .setEnd(classifierGUID)
                        .setType("CLASSIFIER")
                        .addProperty("connectorType", STRING, "CLASSIFIER")
                        .addProperty("eapHash", STRING, sha1)
                        .addProperty("importedAt", LOCALDATETIME, time)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), classifierEdge);
            }
        }

        //INSTANCE_OF
        if ("Object".equals(objectType) || "Part".equals(objectType) || "Port".equals(objectType)) {
            if (GUIDHelper.isEaGuid(pdata1)) {
                ObjectGUID instanceOfGUID = new ObjectGUID(GUIDHelper.unwrapGuid(pdata1));
                AAroNEdge instanceOfEdge = AAroNEdge.builder()
                        .setStart(objectIdentifier)
                        .setEnd(instanceOfGUID)
                        .setType("INSTANCE_OF")
                        .addProperty("connectorType", STRING, "INSTANCE_OF")
                        .addProperty("eapHash", STRING, sha1)
                        .addProperty("importedAt", LOCALDATETIME, time)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), instanceOfEdge);
            }
        }

        //REUSAGE
        if ("Part".equals(objectType) || "Port".equals(objectType)) {
            if (GUIDHelper.isEaGuid(pdata3)) {
                ObjectGUID reusageGUID = new ObjectGUID(GUIDHelper.unwrapGuid(pdata3));
                AAroNEdge reusageEdge = AAroNEdge.builder()
                        .setStart(objectIdentifier)
                        .setEnd(reusageGUID)
                        .setType("REUSAGE")
                        .addProperty("connectorType", STRING, "REUSAGE")
                        .addProperty("eapHash", STRING, sha1)
                        .addProperty("importedAt", LOCALDATETIME, time)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), reusageEdge);
            }
        }

        if (parentId != null && parentId > 0) {
            ObjectId parentIdentifier = new ObjectId(parentId);
            //HAS_PORT
            if ("Port".equals(objectType)) {
                AAroNEdge portEdge = AAroNEdge.builder()
                        .setStart(parentIdentifier)
                        .setEnd(objectIdentifier)
                        .setType("HAS_PORT")
                        .addProperty("connectorType", STRING, "HAS_PORT")
                        .addProperty("eapHash", STRING, sha1)
                        .addProperty("importedAt", LOCALDATETIME, time)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), portEdge);
            }

            //HAS_PART
            if ("Part".equals(objectType)) {
                AAroNEdge partEdge = AAroNEdge.builder()
                        .setStart(parentIdentifier)
                        .setEnd(objectIdentifier)
                        .setType("HAS_PART")
                        .addProperty("connectorType", STRING, "HAS_PART")
                        .addProperty("eapHash", STRING, sha1)
                        .addProperty("importedAt", LOCALDATETIME, time)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()),partEdge);
            }

            //EMBEDS
            AAroNEdge embedsEdge = AAroNEdge.builder()
                    .setStart(parentIdentifier)
                    .setEnd(objectIdentifier)
                    .setType("EMBEDS")
                    .addProperty("connectorType", STRING, "EMBEDS")
                    .addProperty("eapHash", STRING, sha1)
                    .addProperty("importedAt", LOCALDATETIME, time)
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()),embedsEdge);

            //HAS_PARENT
            AAroNEdge parentEdge = AAroNEdge.builder()
                    .setStart(objectIdentifier)
                    .setEnd(parentIdentifier)
                    .setType("HAS_PARENT")
                    .addProperty("connectorType", STRING, "HAS_PARENT")
                    .addProperty("eapHash", STRING, sha1)
                    .addProperty("importedAt", LOCALDATETIME, time)
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()),parentEdge);
        }
    }
}

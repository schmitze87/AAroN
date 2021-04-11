package aaron.sparx.processors;

import aaron.model.AAroNNode;
import aaron.model.Edge;
import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.ObjectGUID;
import aaron.sparx.identifiers.ObjectId;
import aaron.sparx.identifiers.PackageId;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

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
        node.addProperty("objectType", objectType);
        node.addProperty("name", name);
        node.addProperty("alias", alias);
        node.addProperty("author", author);
        node.addProperty("version", version);
        node.addProperty("note", note);
        node.addProperty("stereotype", stereotype);
        node.addProperty("nType", nType);
        node.addProperty("complexity", complexity);
        node.addProperty("effort", effort);
        node.addProperty("style", style);
        node.addProperty("backColor", backColor);
        node.addProperty("borderStyle", borderStyle);
        node.addProperty("borderWidth", borderWidth);
        node.addProperty("fontColor", fontColor);
        node.addProperty("borderColor", borderColor);
        node.addProperty("createdDate", createdDate);
        node.addProperty("modifiedDate", modifiedDate);
        node.addProperty("status", status);
        node.addProperty("abstract", _abstract);
        node.addProperty("tagged", tagged);
        node.addProperty("concurrency", concurrency);
        node.addProperty("visibility", visibility);
        node.addProperty("persistence", persistence);
        node.addProperty("genType", genType);
        node.addProperty("genFile", genFile);
        node.addProperty("header1", header1);
        node.addProperty("header2", header2);
        node.addProperty("phase", phase);
        node.addProperty("scope", scope);
        node.addProperty("genOption", genOption);
        node.addProperty("genLinks", genLinks);
        node.addProperty("eaGuid", eaGuid);
        node.addProperty("runState", runState);
        node.addProperty("tPos", tPos);
        node.addProperty("isRoot", isRoot);
        node.addProperty("isLeaf", isLeaf);
        node.addProperty("isSpec", isSpec);
        node.addProperty("isActive", isActive);
        node.addProperty("stateFlags", stateFlags);
        node.addProperty("packageFlags", packageFlags);
        node.addProperty("multiplicity", multiplicity);
        node.addProperty("styleEx", styleEx);
        node.addProperty("actionFlags", actionFlags);
        node.addProperty("eventFlags", eventFlags);

        node.addProperty("eapHash", sha1);
        node.addProperty("importedAt", time);

        ObjectId objectIdentifier = new ObjectId(objectId);
        ObjectGUID objectGUID = new ObjectGUID(eaGuid);
        model.addNode(objectIdentifier, node);
        model.addNode(objectGUID, node);

        //****************************************
        //Start creating implicit relationships
        //****************************************

        //CONTAINS
        PackageId packageIdIdentifier = new PackageId(packageId);
            Edge containsEdge = Edge.builder()
                    .setStart(packageIdIdentifier)
                    .setEnd(objectIdentifier)
                    .setType("CONTAINS")
                    .addProperty("connectorType", "CONTAINS")
                    .addProperty("eapHash", sha1)
                    .addProperty("importedAt", time)
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), containsEdge);

        //BEHAVIOUR
        if ("Action".equals(objectType)) {
            if (GUIDHelper.isEaGuid(pdata1)) {
                ObjectGUID behaviourGUID = new ObjectGUID(GUIDHelper.unwrapGuid(pdata1));
                Edge behaviourEdge = Edge.builder()
                        .setStart(objectIdentifier)
                        .setEnd(behaviourGUID)
                        .setType("BEHAVIOUR")
                        .addProperty("connectorType", "BEHAVIOUR")
                        .addProperty("eapHash", sha1)
                        .addProperty("importedAt", time)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), behaviourEdge);
            }
        }

        //CLASSIFIER
        if ("Object".equals(objectType) || "Action".equals(objectType)) {
            if (GUIDHelper.isEaGuid(classifierGuid)) {
                ObjectGUID classifierGUID = new ObjectGUID(GUIDHelper.unwrapGuid(classifierGuid));
                Edge classifierEdge = Edge.builder()
                        .setStart(objectIdentifier)
                        .setEnd(classifierGUID)
                        .setType("CLASSIFIER")
                        .addProperty("connectorType", "CLASSIFIER")
                        .addProperty("eapHash", sha1)
                        .addProperty("importedAt", time)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), classifierEdge);
            }
        }

        //INSTANCE_OF
        if ("Object".equals(objectType) || "Part".equals(objectType) || "Port".equals(objectType)) {
            if (GUIDHelper.isEaGuid(pdata1)) {
                ObjectGUID instanceOfGUID = new ObjectGUID(GUIDHelper.unwrapGuid(pdata1));
                Edge instanceOfEdge = Edge.builder()
                        .setStart(objectIdentifier)
                        .setEnd(instanceOfGUID)
                        .setType("INSTANCE_OF")
                        .addProperty("connectorType", "INSTANCE_OF")
                        .addProperty("eapHash", sha1)
                        .addProperty("importedAt", time)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), instanceOfEdge);
            }
        }

        //REUSAGE
        if ("Part".equals(objectType) || "Port".equals(objectType)) {
            if (GUIDHelper.isEaGuid(pdata3)) {
                ObjectGUID reusageGUID = new ObjectGUID(GUIDHelper.unwrapGuid(pdata3));
                Edge reusageEdge = Edge.builder()
                        .setStart(objectIdentifier)
                        .setEnd(reusageGUID)
                        .setType("REUSAGE")
                        .addProperty("connectorType", "REUSAGE")
                        .addProperty("eapHash", sha1)
                        .addProperty("importedAt", time)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), reusageEdge);
            }
        }

        if (parentId != null && parentId > 0) {
            ObjectId parentIdentifier = new ObjectId(parentId);
            //HAS_PORT
            if ("Port".equals(objectType)) {
                Edge portEdge = Edge.builder()
                        .setStart(parentIdentifier)
                        .setEnd(objectIdentifier)
                        .setType("HAS_PORT")
                        .addProperty("connectorType", "HAS_PORT")
                        .addProperty("eapHash", sha1)
                        .addProperty("importedAt", time)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), portEdge);
            }

            //HAS_PART
            if ("Part".equals(objectType)) {
                Edge partEdge = Edge.builder()
                        .setStart(parentIdentifier)
                        .setEnd(objectIdentifier)
                        .setType("HAS_PART")
                        .addProperty("connectorType", "HAS_PART")
                        .addProperty("eapHash", sha1)
                        .addProperty("importedAt", time)
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()),partEdge);
            }

            //EMBEDS
            Edge embedsEdge = Edge.builder()
                    .setStart(parentIdentifier)
                    .setEnd(objectIdentifier)
                    .setType("EMBEDS")
                    .addProperty("connectorType", "EMBEDS")
                    .addProperty("eapHash", sha1)
                    .addProperty("importedAt", time)
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()),embedsEdge);

            //HAS_PARENT
            Edge parentEdge = Edge.builder()
                    .setStart(objectIdentifier)
                    .setEnd(parentIdentifier)
                    .setType("HAS_PARENT")
                    .addProperty("connectorType", "HAS_PARENT")
                    .addProperty("eapHash", sha1)
                    .addProperty("importedAt", time)
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()),parentEdge);
        }
    }
}

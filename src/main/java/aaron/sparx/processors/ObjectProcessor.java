package aaron.sparx.processors;

import aaron.model.Edge;
import aaron.model.Model;
import aaron.model.AAroNNode;
import aaron.sparx.GUIDHelper;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.ObjectGUID;
import aaron.sparx.identifiers.ObjectId;
import aaron.sparx.identifiers.PackageId;
import aaron.sparx.model.EAObject;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class ObjectProcessor implements Processor{

    private final Model model;

    public ObjectProcessor(final Model model) {
        this.model = model;
    }

    public <U extends Map<String, Object>> void process(final U row) {
        Integer objectId = EAObject.OBJECT_ID.value(row);
        String objectType = EAObject.OBJECT_TYPE.value(row);
        Integer diagramId = EAObject.DIAGRAM_ID.value(row);
        String name = EAObject.NAME.value(row);
        String alias = EAObject.ALIAS.value(row);
        String author = EAObject.AUTHOR.value(row);
        String version = EAObject.VERSION.value(row);
        String note = EAObject.NOTE.value(row);
        Integer packageId = EAObject.PACKAGE_ID.value(row);
        String stereotype = EAObject.STEREOTYPE.value(row);
        Integer nType = EAObject.NTYPE.value(row);
        String complexity = EAObject.COMPLEXITY.value(row);
        Integer effort = EAObject.EFFORT.value(row);
        String style = EAObject.STYLE.value(row);
        Integer backColor = EAObject.BACKCOLOR.value(row);
        Integer borderStyle = EAObject.BORDER_STYLE.value(row);
        Integer borderWidth = EAObject.BORDER_WIDTH.value(row);
        Integer fontColor = EAObject.FONT_COLOR.value(row);
        Integer borderColor = EAObject.BORDER_COLOR.value(row);
        LocalDateTime createdDate = EAObject.CREATED_DATE.value(row);
        LocalDateTime modifiedDate = EAObject.MODIFIED_DATE.value(row);
        String status = EAObject.STATUS.value(row);
        String _abstract = EAObject.ABSTRACT.value(row);
        Integer tagged = EAObject.TAGGED.value(row);
        String pdata1 = EAObject.PDATA1.value(row);
        String pdata2 = EAObject.PDATA2.value(row);
        String pdata3 = EAObject.PDATA3.value(row);
        String pdata4 = EAObject.PDATA4.value(row);
        String pdata5 = EAObject.PDATA5.value(row);
        String concurrency = EAObject.CONCURRENCY.value(row);
        String visibility = EAObject.VISIBILITY.value(row);
        String persistence = EAObject.PERSISTENCE.value(row);
        String genType = EAObject.GEN_TYPE.value(row);
        String genFile = EAObject.GEN_FILE.value(row);
        String header1 = EAObject.HEADER1.value(row);
        String header2 = EAObject.HEADER2.value(row);
        String phase = EAObject.PHASE.value(row);
        String scope = EAObject.SCOPE.value(row);
        String genOption = EAObject.GEN_OPTION.value(row);
        String genLinks = EAObject.GEN_LINKS.value(row);
        Integer classifier = EAObject.CLASSIFIER.value(row);
        String eaGuid = GUIDHelper.unwrapGuid(EAObject.EA_GUID.value(row));
        Integer parentId = EAObject.PARENT_ID.value(row);
        String runState = EAObject.RUN_STATE.value(row);
        String classifierGuid = EAObject.CLASSIFIER_GUID.value(row);
        Integer tPos = EAObject.TPOS.value(row);
        Boolean isRoot = EAObject.IS_ROOT.value(row);
        Boolean isLeaf = EAObject.IS_LEAF.value(row);
        Boolean isSpec = EAObject.IS_SPEC.value(row);
        Boolean isActive = EAObject.IS_ACTIVE.value(row);
        String stateFlags = EAObject.STATE_FLAGS.value(row);
        String packageFlags = EAObject.PACKAGE_FLAGS.value(row);
        String multiplicity = EAObject.MULTIPLICITY.value(row);
        String styleEx = EAObject.STYLE_EX.value(row);
        String actionFlags = EAObject.ACTION_FLAGS.value(row);
        String eventFlags = EAObject.EVENT_FLAGS.value(row);

        AAroNNode node;
        if ("Package".equals(objectType) && StringUtils.isNumeric(pdata1)) {
            PackageId identifier = new PackageId(Integer.parseInt(pdata1));
            node = model.getNode(identifier);
        } else {
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
                        .build();
                model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()),partEdge);
            }

            //EMBEDS
            Edge embedsEdge = Edge.builder()
                    .setStart(parentIdentifier)
                    .setEnd(objectIdentifier)
                    .setType("EMBEDS")
                    .addProperty("connectorType", "EMBEDS")
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()),embedsEdge);

            //HAS_PARENT
            Edge parentEdge = Edge.builder()
                    .setStart(objectIdentifier)
                    .setEnd(parentIdentifier)
                    .setType("HAS_PARENT")
                    .addProperty("connectorType", "HAS_PARENT")
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()),parentEdge);
        }
    }
}

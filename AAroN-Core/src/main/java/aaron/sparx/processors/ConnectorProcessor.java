package aaron.sparx.processors;

import aaron.model.AAroNEdge;
import aaron.model.AAroNNode;
import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.identifiers.ConnectorGUID;
import aaron.sparx.identifiers.ConnectorId;
import aaron.sparx.identifiers.DiagramId;
import aaron.sparx.identifiers.ObjectId;
import aaron.sparx.model.EAConnector;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.model.PropertyType.*;

public class ConnectorProcessor extends AbstractProcessor {


    public ConnectorProcessor(String sha1, LocalDateTime time, Model model, ImportConext context) {
        super(sha1, time, model, context);
    }

    @Override
    public void process(final Map<String, Object> row) {
        Integer connectorId = EAConnector.CONNECTOR_ID.value(row);
        String name = EAConnector.NAME.value(row);
        String direction = EAConnector.DIRECTION.value(row);
        String notes = EAConnector.NOTES.value(row);
        String connectorType = EAConnector.CONNECTOR_TYPE.value(row);
        String subType = EAConnector.SUB_TYPE.value(row);
        String sourceCard = EAConnector.SOURCE_CARD.value(row);
        String sourceAccess = EAConnector.SOURCE_ACCESS.value(row);
        String sourceElement = EAConnector.SOURCE_ELEMENT.value(row);
        String destCard = EAConnector.DEST_CARD.value(row);
        String destAccess = EAConnector.DEST_ACCESS.value(row);
        String destElement = EAConnector.DEST_ELEMENT.value(row);
        String sourceRole = EAConnector.SOURCE_ROLE.value(row);
        String sourceRoleType = EAConnector.SOURCE_ROLE_TYPE.value(row);
        String sourceRoleNote = EAConnector.SOURCE_ROLE_NOTE.value(row);
        String sourceContainment = EAConnector.SOURCE_CONTAINMENT.value(row);
        Integer sourceIsAggregate = EAConnector.SOURCE_IS_AGGREGATE.value(row);
        Integer sourceIsOrdered = EAConnector.SOURCE_IS_ORDERED.value(row);
        String sourceQualifier = EAConnector.SOURCE_QUALIFIER.value(row);
        String destRole = EAConnector.DEST_ROLE.value(row);
        String destRoleType = EAConnector.DEST_ROLE_TYPE.value(row);
        String destRoleNote = EAConnector.DEST_ROLE_NOTE.value(row);
        String destContainment = EAConnector.DEST_CONTAINMENT.value(row);
        Integer destIsAggregate = EAConnector.DEST_IS_AGGREGATE.value(row);
        Integer destIsOrdered = EAConnector.DEST_IS_ORDERED.value(row);
        String destQualifier = EAConnector.DEST_QUALIFIER.value(row);
        Integer startObjectId = EAConnector.START_OBJECT_ID.value(row);
        Integer endObjectId = EAConnector.END_OBJECT_ID.value(row);
        String topStartLabel = EAConnector.TOP_START_LABEL.value(row);
        String topMidLabel = EAConnector.TOP_MID_LABEL.value(row);
        String topEndLabel = EAConnector.TOP_END_LABEL.value(row);
        String btmStartLabel = EAConnector.BTM_START_LABEL.value(row);
        String btmMidLabel = EAConnector.BTM_MID_LABEL.value(row);
        String btmEndLabel = EAConnector.BTM_END_LABEL.value(row);
        Integer startEdge = EAConnector.START_EDGE.value(row);
        Integer endEdge = EAConnector.END_EDGE.value(row);
        Integer ptStartX = EAConnector.PT_START_X.value(row);
        Integer ptStartY = EAConnector.PT_START_Y.value(row);
        Integer ptEndX = EAConnector.PT_END_X.value(row);
        Integer ptEndY = EAConnector.PT_END_Y.value(row);
        Integer seqNo = EAConnector.SEQ_NO.value(row);
        Integer headStyle = EAConnector.HEAD_STYLE.value(row);
        Integer lineStyle = EAConnector.LINE_STYLE.value(row);
        Integer routeStyle = EAConnector.ROUTE_STYLE.value(row);
        Integer isBold = EAConnector.IS_BOLD.value(row);
        Integer lineColor = EAConnector.LINE_COLOR.value(row);
        String stereotype = EAConnector.STEREOTYPE.value(row);
        String virtualInheritance = EAConnector.VIRTUAL_INHERITANCE.value(row);
        String linkAccess = EAConnector.LINK_ACCESS.value(row);
        String pdata1 = EAConnector.PDATA1.value(row);
        String pdata2 = EAConnector.PDATA2.value(row);
        String pdata3 = EAConnector.PDATA3.value(row);
        String pdata4 = EAConnector.PDATA4.value(row);
        String pdata5 = EAConnector.PDATA5.value(row);
        Integer diagramId = EAConnector.DIAGRAM_ID.value(row);
        String eaGuid = GUIDHelper.unwrapGuid(EAConnector.EA_GUID.value(row));
        String sourceConstraint = EAConnector.SOURCE_CONSTRAINT.value(row);
        String destConstraint = EAConnector.DEST_CONSTRAINT.value(row);
        Boolean sourceIsNavigable = EAConnector.SOURCE_IS_NAVIGABLE.value(row);
        Boolean destIsNavigable = EAConnector.DEST_IS_NAVIGABLE.value(row);
        Boolean isRoot = EAConnector.IS_ROOT.value(row);
        Boolean isLeaf = EAConnector.IS_LEAF.value(row);
        Boolean isSpec = EAConnector.IS_SPEC.value(row);
        String sourceChangeable = EAConnector.SOURCE_CHANGEABLE.value(row);
        String destChangeable = EAConnector.DEST_CHANGEABLE.value(row);
        String sourceTS = EAConnector.SOURCE_TS.value(row);
        String destTS = EAConnector.DEST_TS.value(row);
        String stateFlags = EAConnector.STATE_FLAGS.value(row);
        String actionFlags = EAConnector.ACTION_FLAGS.value(row);
        Boolean isSignal = EAConnector.IS_SIGNAL.value(row);
        Boolean isStimulus = EAConnector.IS_STIMULUS.value(row);
        String dispatchAction = EAConnector.DISPATCH_ACTION.value(row);
        Integer target2 = EAConnector.TARGET2.value(row);
        String styleEx = EAConnector.STYLE_EX.value(row);
        String sourceStereotype = EAConnector.SOURCE_STEREOTYPE.value(row);
        String destStereotype = EAConnector.DEST_STEREOTYPE.value(row);
        String sourceStyle = EAConnector.SOURCE_STYLE.value(row);
        String destStyle = EAConnector.DEST_STYLE.value(row);
        String eventFlags = EAConnector.EVENT_FLAGS.value(row);

        AAroNEdge edge = AAroNEdge.builder().build();

        //Set start and end of edge
        ObjectId start = new ObjectId(startObjectId);
        ObjectId end = new ObjectId(endObjectId);
        if (start != null && end != null) {
            edge.setStart(start);
            edge.setEnd(end);
        } else {
            //start or end is missing
            //TODO: ExceptionHandling
            return;
        }

        //set edge type
        if (StringUtils.isNotBlank(stereotype)) {
            edge.setType(stereotype);
        } else {
            edge.setType(connectorType);
        }

        //Set properties
        edge.addProperty("name", STRING, name);
        edge.addProperty("direction", STRING, direction);
        edge.addProperty("notes", STRING, notes);
        edge.addProperty("connectorType", STRING, connectorType);
        edge.addProperty("subType", STRING, subType);
        edge.addProperty("sourceCardinality", STRING, sourceCard);
        edge.addProperty("sourceAccess", STRING, sourceAccess);
        edge.addProperty("sourceElement", STRING, sourceElement);
        edge.addProperty("destCardinality", STRING, destCard);
        edge.addProperty("destAccess", STRING, destAccess);
        edge.addProperty("destElement", STRING, destElement);
        edge.addProperty("sourceRole", STRING, sourceRole);
        edge.addProperty("sourceRoleType", STRING, sourceRoleType);
        edge.addProperty("sourceRoleNote", STRING, sourceRoleNote);
        edge.addProperty("sourceContainment", STRING, sourceContainment);
        edge.addProperty("sourceIsAggregate", INTEGER, sourceIsAggregate);
        edge.addProperty("sourceIsOrdered", INTEGER, sourceIsOrdered);
        edge.addProperty("sourceQualifier", STRING, sourceQualifier);
        edge.addProperty("destRole", STRING, destRole);
        edge.addProperty("destRoleType", STRING, destRoleType);
        edge.addProperty("destRoleNote", STRING, destRoleNote);
        edge.addProperty("destContainment", STRING, destContainment);
        edge.addProperty("destIsAggregate", INTEGER, destIsAggregate);
        edge.addProperty("destIsOrdered", INTEGER, destIsOrdered);
        edge.addProperty("destQualifier", STRING, destQualifier);
        edge.addProperty("topStartLabel", STRING, topStartLabel);
        edge.addProperty("topMidLabel", STRING, topMidLabel);
        edge.addProperty("topEndLabel", STRING, topEndLabel);
        edge.addProperty("btmStartLabel", STRING, btmStartLabel);
        edge.addProperty("btmMidLabel", STRING, btmMidLabel);
        edge.addProperty("btmEndLabel", STRING, btmEndLabel);
        edge.addProperty("startEdge", INTEGER, startEdge);
        edge.addProperty("endEdge", INTEGER, endEdge);
        edge.addProperty("ptStartX", INTEGER, ptStartX);
        edge.addProperty("ptStartY", INTEGER, ptStartY);
        edge.addProperty("ptEndX", INTEGER, ptEndX);
        edge.addProperty("ptEndY", INTEGER, ptEndY);
        edge.addProperty("seqNo", INTEGER, seqNo);
        edge.addProperty("headStyle", INTEGER, headStyle);
        edge.addProperty("lineStyle", INTEGER, lineStyle);
        edge.addProperty("routeStyle", INTEGER, routeStyle);
        edge.addProperty("isBold", INTEGER, isBold);
        edge.addProperty("lineColor", INTEGER, lineColor);
        edge.addProperty("stereotype", STRING, stereotype);
        edge.addProperty("virtualInheritance", STRING, virtualInheritance);
        edge.addProperty("linkAccess", STRING, linkAccess);

        //TODO: process CONNECTOR PDATA Fields
//        edge.addProperty("pdata1", pdata1);
//        edge.addProperty("pdata2", pdata2);
//        edge.addProperty("pdata3", pdata3);
//        edge.addProperty("pdata4", pdata4);
//        edge.addProperty("pdata5", pdata5);

        DiagramId diagramIdentifier = new DiagramId(diagramId);
        AAroNNode diagramNode = model.getNode(diagramIdentifier);
        if (diagramNode != null) {
            edge.addProperty("diagram", STRING, diagramNode.getProperty(STRING, "eaGuid"));
        }

        edge.addProperty("eaGuid", STRING, eaGuid);
        edge.addProperty("sourceConstraint", STRING, sourceConstraint);
        edge.addProperty("destIsNavigable", BOOLEAN, destIsNavigable);
        edge.addProperty("isRoot", BOOLEAN, isRoot);
        edge.addProperty("isLeaf", BOOLEAN, isLeaf);
        edge.addProperty("isSpec", BOOLEAN, isSpec);
        edge.addProperty("sourceChangeable", STRING, sourceChangeable);
        edge.addProperty("destChangeable", STRING, destChangeable);
        edge.addProperty("sourceTS", STRING, sourceTS);
        edge.addProperty("destTS", STRING, destTS);
        edge.addProperty("stateFlags", STRING, stateFlags);
        edge.addProperty("actionFlags", STRING, actionFlags);
        edge.addProperty("isSignal", BOOLEAN, isSignal);
        edge.addProperty("isStimulus", BOOLEAN, isStimulus);
        edge.addProperty("dispatchAction", STRING, dispatchAction);
        edge.addProperty("target2", INTEGER, target2);
        edge.addProperty("styleEx", STRING, styleEx);
        edge.addProperty("sourceStereotype", STRING, sourceStereotype);
        edge.addProperty("destStereotype", STRING, destStereotype);
        edge.addProperty("sourceStyle", STRING, sourceStyle);
        edge.addProperty("destStyle", STRING, destStyle);
        edge.addProperty("eventFlags", STRING, eventFlags);

        edge.addProperty("eapHash", STRING, sha1);
        edge.addProperty("importedAt", LOCALDATETIME, time);

        ConnectorId connectorIdentifier = new ConnectorId(connectorId);
        ConnectorGUID connectorGUIDIdentifier = new ConnectorGUID(eaGuid);
        model.addEdge(connectorIdentifier, edge);
        model.addEdge(connectorGUIDIdentifier, edge);
    }
}

package aaron.sparx.processors;

import aaron.model.AAroNNode;
import aaron.model.AAroNEdge;
import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.identifiers.*;
import aaron.sparx.model.EADiagram;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static aaron.model.PropertyType.*;

public class DiagramProcessor extends AbstractProcessor{


    public DiagramProcessor(String sha1, LocalDateTime time, Model model) {
        super(sha1, time, model);
    }

    @Override
    public void process(final Map<String, Object> row) {
        Integer diagramId = EADiagram.DIAGRAM_ID.value(row);
        Integer packageId = EADiagram.PACKAGE_ID.value(row);
        Integer parentId = EADiagram.PARENT_ID.value(row);
        String diagramType = EADiagram.DIAGRAM_TYPE.value(row);
        String name = EADiagram.NAME.value(row);
        String version = EADiagram.VERSION.value(row);
        String author = EADiagram.AUTHOR.value(row);
        Integer showDetails = EADiagram.SHOW_DETAILS.value(row);
        String notes = EADiagram.NOTES.value(row);
        String stereotype = EADiagram.STEREOTYPE.value(row);
        Boolean attPub = EADiagram.ATT_PUB.value(row);
        Boolean attPri = EADiagram.ATT_PRI.value(row);
        Boolean attPro = EADiagram.ATT_PRO.value(row);
        String orientation = EADiagram.ORIENTATION.value(row);
        Integer cx = EADiagram.CX.value(row);
        Integer cy = EADiagram.CY.value(row);
        Integer scale = EADiagram.SCALE.value(row);
        LocalDateTime createdDate = EADiagram.CREATED_DATE.value(row);
        LocalDateTime modifiedDate = EADiagram.MODIFIED_DATE.value(row);
        String htmlPath = EADiagram.HTML_PATH.value(row);
        Boolean showForeign = EADiagram.SHOW_FOREIGN.value(row);
        Boolean showBorder = EADiagram.SHOW_BORDER.value(row);
        Boolean showPackageContents = EADiagram.SHOW_PACKAGE_CONTENTS.value(row);
        String pdata = EADiagram.PDATA.value(row);
        Boolean locked = EADiagram.LOCKED.value(row);
        String eaGuid = GUIDHelper.unwrapGuid(EADiagram.EA_GUID.value(row));
        Integer tPos = EADiagram.TPOS.value(row);
        String swimlanes = EADiagram.SWIMLANES.value(row);
        String styleEx = EADiagram.STYLE_EX.value(row);

        String mdgDiagramType = getMDGDiagramType(styleEx);

        AAroNNode diagramNode = AAroNNode.builder()
                .addLabel("Diagram")
                .addLabel(diagramType)
                .addProperty("diagramType", STRING, diagramType)
                .addProperty("name", STRING, name)
                .addProperty("version", STRING, version)
                .addProperty("author", STRING, author)
                .addProperty("showDetails", INTEGER, showDetails)
                .addProperty("notes", STRING, notes)
                .addProperty("stereotype", STRING, stereotype)
                .addProperty("attPub", BOOLEAN, attPub)
                .addProperty("attPri", BOOLEAN, attPri)
                .addProperty("attPro", BOOLEAN, attPro)
                .addProperty("orientation", STRING, orientation)
                .addProperty("cx", INTEGER, cx)
                .addProperty("cy", INTEGER, cy)
                .addProperty("scale", INTEGER, scale)
                .addProperty("createdDate", LOCALDATETIME, createdDate)
                .addProperty("modifiedDate", LOCALDATETIME,modifiedDate)
                .addProperty("htmlPath", STRING, htmlPath)
                .addProperty("showForeign", BOOLEAN, showForeign)
                .addProperty("showBorder", BOOLEAN, showBorder)
                .addProperty("showPackageContents", BOOLEAN, showPackageContents)
                .addProperty("pdata", STRING, pdata)
                .addProperty("locked", BOOLEAN, locked)
                .addProperty("eaGuid", STRING, eaGuid)
                .addProperty("tPos", INTEGER, tPos)
                .addProperty("swimlanes", STRING, swimlanes)
                .addProperty("styleEx", STRING, styleEx)
                .addProperty("eapHash", STRING, sha1)
                .addProperty("importedAt", LOCALDATETIME, time)
                .build();

        if (mdgDiagramType != null) {
            diagramNode.addLabel(mdgDiagramType);
        }

        DiagramId diagramIdentifier = new DiagramId(diagramId);
        ObjectGUID guidIdentifier = new ObjectGUID(eaGuid);
        model.addNode(diagramIdentifier, diagramNode);
        model.addNode(guidIdentifier, diagramNode);

        //CONTAINS Edge
        PackageId packageIdentifier = new PackageId(packageId);
        AAroNEdge containsEdge = AAroNEdge.builder()
                .setType("CONTAINS")
                .setStart(packageIdentifier)
                .setEnd(diagramIdentifier)
                .addProperty("eapHash", STRING, sha1)
                .addProperty("importedAt", LOCALDATETIME, time)
                .build();
        model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), containsEdge);

        if (parentId != null && parentId > 0) {
            ObjectId parentIdentifier = new ObjectId(parentId);
            //HAS_PARENT Edge
            AAroNEdge parentEdge = AAroNEdge.builder()
                    .setType("HAS_PARENT")
                    .setStart(diagramIdentifier)
                    .setEnd(parentIdentifier)
                    .addProperty("eapHash", STRING, sha1)
                    .addProperty("importedAt", LOCALDATETIME, time)
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), parentEdge);
        }
    }

    private String getMDGDiagramType(final String styleEx) {
        if (styleEx != null) {
            Map<String, String> styleProps = new HashMap<>();
            String[] split = styleEx.split(";");
            for (String s : split) {
                String[] entry = s.split("=");
                if (entry.length == 2) {
                    styleProps.put(entry[0], entry[1]);
                }
            }
            return styleProps.get("MDGDgm");
        }
        return null;
    }
}

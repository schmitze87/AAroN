package aaron.sparx.processors;

import aaron.model.Edge;
import aaron.model.Model;
import aaron.model.AAroNNode;
import aaron.sparx.GUIDHelper;
import aaron.sparx.identifiers.*;
import aaron.sparx.model.EADiagram;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DiagramProcessor implements Processor {

    private final Model model;

    public DiagramProcessor(final Model model) {
        this.model = model;
    }

    @Override
    public <U extends Map<String, Object>> void process(U row) {
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
                .addLabel(mdgDiagramType)
                .addProperty("diagramType", diagramType)
                .addProperty("name", name)
                .addProperty("version", version)
                .addProperty("author", author)
                .addProperty("showDetails", showDetails)
                .addProperty("notes", notes)
                .addProperty("stereotype", stereotype)
                .addProperty("attPub", attPub)
                .addProperty("attPri", attPri)
                .addProperty("attPro", attPro)
                .addProperty("orientation", orientation)
                .addProperty("cx", cx)
                .addProperty("cy", cy)
                .addProperty("scale", scale)
                .addProperty("createdDate", createdDate)
                .addProperty("modifiedDate", modifiedDate)
                .addProperty("htmlPath", htmlPath)
                .addProperty("showForeign", showForeign)
                .addProperty("showBorder", showBorder)
                .addProperty("showPackageContents", showPackageContents)
                .addProperty("pdata", pdata)
                .addProperty("locked", locked)
                .addProperty("eaGuid", eaGuid)
                .addProperty("tPos", tPos)
                .addProperty("swimlanes", swimlanes)
                .addProperty("styleEx", styleEx)
                .build();

        DiagramId diagramIdentifier = new DiagramId(diagramId);
        ObjectGUID guidIdentifier = new ObjectGUID(eaGuid);
        model.addNode(diagramIdentifier, diagramNode);
        model.addNode(guidIdentifier, diagramNode);

        //CONTAINS Edge
        PackageId packageIdentifier = new PackageId(packageId);
        Edge containsEdge = Edge.builder()
                .setType("CONTAINS")
                .setStart(packageIdentifier)
                .setEnd(diagramIdentifier)
                .build();
        model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), containsEdge);

        if (parentId != null && parentId > 0) {
            ObjectId parentIdentifier = new ObjectId(parentId);
            //HAS_PARENT Edge
            Edge parentEdge = Edge.builder()
                    .setType("HAS_PARENT")
                    .setStart(diagramIdentifier)
                    .setEnd(parentIdentifier)
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), parentEdge);
        }
    }

    private String getMDGDiagramType(final String styleEx) {
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
}

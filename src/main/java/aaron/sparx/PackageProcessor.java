package aaron.sparx;

import aaron.neo4j.model.Edge;
import aaron.neo4j.model.Identifier;
import aaron.neo4j.model.Model;
import aaron.neo4j.model.AAroNNode;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.PackageId;
import aaron.sparx.model.EAPackage;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class PackageProcessor implements Processor{

    private final Model model;

    public PackageProcessor(final Model model) {
        this.model = model;
    }

    @Override
    public <U extends Map<String, Object>> void process(U row) {
        String name = EAPackage.NAME.value(row);
        LocalDateTime createdOn = EAPackage.CREATED_ON.value(row);
        LocalDateTime modifiedOn = EAPackage.MODIFIED_ON.value(row);
        String notes = EAPackage.NOTES.value(row);
        Integer packageId = EAPackage.PACKAGE_ID.value(row);
        String eaGuid = GUIDHelper.unwrapGuid(EAPackage.EA_GUID.value(row));
        Integer parentId = EAPackage.PARENT_ID.value(row);

        AAroNNode node;
        PackageId identifier = new PackageId(packageId);
        node = model.getNode(identifier);
        if (node == null) {
            node = AAroNNode.builder().build();
        }
        node.addLabel("Package");
        node.addProperty("name", name);
        node.addProperty("createdOn", createdOn);
        node.addProperty("modifiedOn", modifiedOn);
        node.addProperty("notes", notes);
        node.addProperty("packageId", packageId);
        node.addProperty("eaGuid", eaGuid);

        model.addNode(identifier, node);

        if (parentId > 0) {
            PackageId parentPackageIdentifier = new PackageId(parentId);
            Edge containsEdge = Edge.builder()
                    .setStart(parentPackageIdentifier)
                    .setEnd(identifier)
                    .setType("CONTAINS")
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), containsEdge);
        }
    }
}

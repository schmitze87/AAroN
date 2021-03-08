package aaron.sparx.processors;

import aaron.model.AAroNNode;
import aaron.model.Edge;
import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.PackageId;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static aaron.sparx.model.EAPackage.*;

public class PackageProcessor extends  AbstractProcessor{

    public PackageProcessor(String sha1, LocalDateTime time, Model model) {
        super(sha1, time, model);
    }

    @Override
    public <U extends Map<String, Object>> void process(U row) {
        String name = NAME.value(row);
        LocalDateTime createdOn = CREATED_ON.value(row);
        LocalDateTime modifiedOn = MODIFIED_ON.value(row);
        String notes = NOTES.value(row);
        Integer packageId = PACKAGE_ID.value(row);
        String eaGuid = GUIDHelper.unwrapGuid(EA_GUID.value(row));
        Integer parentId = PARENT_ID.value(row);

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
        node.addProperty("eapHash", sha1);
        node.addProperty("importedAt", time);

        model.addNode(identifier, node);

        if (parentId > 0) {
            PackageId parentPackageIdentifier = new PackageId(parentId);
            Edge containsEdge = Edge.builder()
                    .setStart(parentPackageIdentifier)
                    .setEnd(identifier)
                    .setType("CONTAINS")
                    .addProperty("eapHash", sha1)
                    .addProperty("importedAt", time)
                    .build();
            model.addEdge(new ImplizitRelationId(UUID.randomUUID().toString()), containsEdge);
        }
    }
}

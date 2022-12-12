package aaron.sparx.processors;

import aaron.model.AAroNEdge;
import aaron.model.AAroNNode;
import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.identifiers.ImplizitRelationId;
import aaron.sparx.identifiers.PackageId;
import aaron.sparx.identifiers.ProjectGUID;

import java.time.LocalDateTime;
import java.util.Map;

import static aaron.model.PropertyType.LOCALDATETIME;
import static aaron.model.PropertyType.STRING;
import static aaron.sparx.model.EAPackage.*;

public class PackageProcessor extends AbstractProcessor {

    public PackageProcessor(String sha1, LocalDateTime time, Model model, ImportConext context) {
        super(sha1, time, model, context);
    }

    @Override
    public void process(final Map<String, Object> row) {
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
        node.addProperty("name", STRING, name);
        node.addProperty("createdOn", LOCALDATETIME, createdOn);
        node.addProperty("modifiedOn", LOCALDATETIME, modifiedOn);
        node.addProperty("notes", STRING, notes);
//        node.addProperty("packageId", INTEGER, packageId);
        node.addProperty("eaGuid", STRING, eaGuid);
        node.addProperty("eapHash", STRING, sha1);
        node.addProperty("importedAt", LOCALDATETIME, time);

        model.addNode(identifier, node);

        if (parentId == 0) {
            ProjectGUID projectGUIDIdentifier = new ProjectGUID(context.getProjectGuid());
            AAroNEdge containsEdge = AAroNEdge.builder()
                    .setStart(projectGUIDIdentifier)
                    .setEnd(identifier)
                    .setType("CONTAINS")
                    .addProperty("eapHash", STRING, sha1)
                    .addProperty("importedAt", LOCALDATETIME, time)
                    .build();
            model.addEdge(new ImplizitRelationId(), containsEdge);
        }

        // Handling of the following is done in ObjectProcessor
//        if (parentId > 0) {
//            PackageId parentPackageIdentifier = new PackageId(parentId);
//            AAroNEdge containsEdge = AAroNEdge.builder()
//                    .setStart(parentPackageIdentifier)
//                    .setEnd(identifier)
//                    .setType("CONTAINS")
//                    .addProperty("eapHash", STRING, sha1)
//                    .addProperty("importedAt", LOCALDATETIME, time)
//                    .build();
//            aaron.model.addEdge(new ImplizitRelationId(), containsEdge);
//        }
    }
}

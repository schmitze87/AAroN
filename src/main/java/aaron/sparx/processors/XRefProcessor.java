package aaron.sparx.processors;

import aaron.model.Edge;
import aaron.model.Model;
import aaron.model.AAroNNode;
import aaron.sparx.GUIDHelper;
import aaron.sparx.identifiers.ConnectorGUID;
import aaron.sparx.identifiers.ObjectGUID;
import aaron.sparx.model.EAObject;
import aaron.sparx.model.EAXref;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XRefProcessor implements Processor{

    private final static Pattern xrefStereotypePattern =
            Pattern.compile("@STEREO;Name=(?<Name>\\w+);(?:FQName=(?<FQName>(?<ns>[\\w\\d\\.-]+)::(?<s>[\\w\\d]+));)?@ENDSTEREO;|@STEREO;Name=(?<Name2>\\w+);GUID=(?<GUID>\\{[0-9A-Fa-f]{8}-(?:[0-9A-Fa-f]{4}-){3}[0-9A-Fa-f]{12}\\});@ENDSTEREO;");


    private final Model model;

    public XRefProcessor(final Model model) {
        this.model = model;
    }

    @Override
    public <U extends Map<String, Object>> void process(U row) {
        String xrefId = GUIDHelper.unwrapGuid(EAXref.XREF_ID.value(row));
        String name = EAXref.NAME.value(row);
        String type = EAXref.TYPE.value(row);
        String visibility = EAXref.VISIBILITY.value(row);
        String namespace = EAXref.NAMESPACE.value(row);
        String requirement = EAXref.REQUIREMENT.value(row);
        String constraint = EAXref.CONSTRAINT.value(row);
        String behaviour = EAXref.BEHAVIOR.value(row);
        String partition = EAXref.PARTITION.value(row);
        String description = EAXref.DESCRIPTION.value(row);
        String client = GUIDHelper.unwrapGuid(EAXref.CLIENT.value(row));
        String supplier = EAXref.SUPPLIER.value(row);
        String link = EAXref.LINK.value(row);

        Type typeEnum = Type.getByName(type);
        Name nameEnum = Name.getByName(name);
        BEHAVIOUR behaviourEnum = BEHAVIOUR.getByName(behaviour);
        switch (typeEnum) {
            case ELEMENT_PROPERTY:
                ObjectGUID objectGUID = new ObjectGUID(client);
                AAroNNode node = model.getNode(objectGUID);
                if (node != null) {
                    if (nameEnum == Name.STEREOTYPES) {
                        SetFQStereotype(node, description);
                    }
                }
                break;
            case CONNECTOR_PROPERTY:
                ConnectorGUID connectorGUID = new ConnectorGUID(client);
                Edge edge = model.getEdge(connectorGUID);
                if (edge != null) {
                    if (nameEnum == Name.STEREOTYPES) {
                        SetFQStereotype(edge, description);
                    }
                    if (nameEnum == Name.MOF_PROPERTIES && behaviourEnum == BEHAVIOUR.CONVEYED) {
                        String[] collect = Arrays.stream(description.split(",")).map(s -> GUIDHelper.unwrapGuid(s)).toArray(String[]::new);
                        edge.addProperty("conveyed", collect);
                    }
                    if (nameEnum == Name.MOF_PROPERTIES && behaviourEnum == BEHAVIOUR.ABSTRACTION) {
                        String[] collect = Arrays.stream(description.split(",")).map(s -> GUIDHelper.unwrapGuid(s)).toArray(String[]::new);
                        edge.addProperty("informationFlowsRealized", collect);
                    }
                }
                break;
            case ATTRIBUTE_PROPERTY:
                break;
            case OPERATION_PROPERTY:
                break;
            case UNKNOWN:
                break;
            default:
                return;
        }
    }

    private void SetFQStereotype(final AAroNNode node, final String description) {
        Matcher m = xrefStereotypePattern.matcher(description);
        if (m.matches()) {
            String stereotype = (String) node.getProperties().get(EAObject.STEREOTYPE.getName());
            String xrefStereotype = m.group("Name");
            String xrefFQStereotype = m.group("FQName");
            if (StringUtils.equals(stereotype, xrefStereotype)) {
                node.addProperty("fqStereotype", xrefFQStereotype);
            }
        }
    }

    private void SetFQStereotype(final Edge edge, final String description) {
        Matcher m = xrefStereotypePattern.matcher(description);
        if (m.matches()) {
            String stereotype = (String) edge.getProperties().get("stereotype");
            String xrefStereotype = m.group("Name");
            String xrefFQStereotype = m.group("FQName");
            if (StringUtils.equals(stereotype, xrefStereotype)) {
                edge.addProperty("fqStereotype", xrefStereotype);
            }
        }
    }

    private enum Name {
        CUSTOM_PROPERTIES("CustomProperties"),
        DEFAULT_DIAGRAM("DefaultDiagram"),
        MOF_PROPERTIES("MOFProps"),
        STEREOTYPES("Stereotypes");

        private final String name;

        Name(final String name) {
            this.name = name;
        }

        static Name getByName(final String name) {
            for (Name t : Name.values()) {
                if (t.name.equals(name)) {
                    return t;
                } else {
                    continue;
                }
            }
            return null;
        }
    }

    private enum Type {
        ELEMENT_PROPERTY("element property"),
        CONNECTOR_PROPERTY("connector property"),
        ATTRIBUTE_PROPERTY("attribute property"),
        OPERATION_PROPERTY("operation property"),
        UNKNOWN("");

        private final String name;

        Type(final String name) {
            this.name = name;
        }

        static Type getByName(final String name) {
            for (Type t : Type.values()) {
                if (t.name.equals(name)) {
                    return t;
                } else {
                    continue;
                }
            }
            return UNKNOWN;
        }
    }

    private enum BEHAVIOUR {
        CONVEYED("conveyed"),
        ABSTRACTION("abstraction"),
        UNKNOWN("");

        private final String name;

        BEHAVIOUR(final String name) {
            this.name = name;
        }

        static BEHAVIOUR getByName(final String name) {
            for (BEHAVIOUR behaviour : BEHAVIOUR.values()) {
                if (behaviour.name.equals(name)) {
                    return behaviour;
                } else {
                    continue;
                }
            }
            return UNKNOWN;
        }
    }
}

package aaron.sparx.processors;

import aaron.model.AAroNNode;
import aaron.model.AAroNEdge;
import aaron.model.Model;
import aaron.sparx.GUIDHelper;
import aaron.sparx.identifiers.ConnectorGUID;
import aaron.sparx.identifiers.ObjectGUID;
import aaron.sparx.model.EAObject;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static aaron.model.PropertyType.STRING;
import static aaron.model.PropertyType.STRING_ARRAY;
import static aaron.sparx.model.EAXref.*;

public class XRefProcessor extends AbstractProcessor{

    private final static Pattern xrefStereotypePattern =
            Pattern.compile("@STEREO;Name=(?<Name>\\w+);(?:FQName=(?<FQName>(?<ns>[\\w\\d\\.-]+)::(?<s>[\\w\\d]+));)?@ENDSTEREO;|@STEREO;Name=(?<Name2>\\w+);GUID=(?<GUID>\\{[0-9A-Fa-f]{8}-(?:[0-9A-Fa-f]{4}-){3}[0-9A-Fa-f]{12}\\});@ENDSTEREO;");

    public XRefProcessor(String sha1, LocalDateTime time, Model model) {
        super(sha1, time, model);
    }

    @Override
    public void process(final Map<String, Object> row) {
        String xrefId = GUIDHelper.unwrapGuid(XREF_ID.value(row));
        String name = NAME.value(row);
        String type = TYPE.value(row);
        String visibility = VISIBILITY.value(row);
        String namespace = NAMESPACE.value(row);
        String requirement = REQUIREMENT.value(row);
        String constraint = CONSTRAINT.value(row);
        String behaviour = BEHAVIOR.value(row);
        String partition = PARTITION.value(row);
        String description = DESCRIPTION.value(row);
        String client = GUIDHelper.unwrapGuid(CLIENT.value(row));
        String supplier = SUPPLIER.value(row);
        String link = LINK.value(row);

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
                AAroNEdge edge = model.getEdge(connectorGUID);
                if (edge != null) {
                    if (nameEnum == Name.STEREOTYPES) {
                        SetFQStereotype(edge, description);
                    }
                    if (nameEnum == Name.MOF_PROPERTIES && behaviourEnum == BEHAVIOUR.CONVEYED) {
                        String[] collect = Arrays.stream(description.split(",")).map(s -> GUIDHelper.unwrapGuid(s)).toArray(String[]::new);
                        edge.addProperty("conveyed", STRING_ARRAY, collect);
                    }
                    if (nameEnum == Name.MOF_PROPERTIES && behaviourEnum == BEHAVIOUR.ABSTRACTION) {
                        String[] collect = Arrays.stream(description.split(",")).map(s -> GUIDHelper.unwrapGuid(s)).toArray(String[]::new);
                        edge.addProperty("informationFlowsRealized", STRING_ARRAY, collect);
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
            String stereotype = (String) node.getProperty(STRING, EAObject.STEREOTYPE.getName());
            String xrefStereotype = m.group("Name");
            String xrefFQStereotype = m.group("FQName");
            if (StringUtils.equals(stereotype, xrefStereotype)) {
                node.addProperty("fqStereotype", STRING, xrefFQStereotype);
            }
        }
    }

    private void SetFQStereotype(final AAroNEdge edge, final String description) {
        Matcher m = xrefStereotypePattern.matcher(description);
        if (m.matches()) {
            String stereotype = (String) edge.getProperty(STRING, "stereotype");
            String xrefStereotype = m.group("Name");
            String xrefFQStereotype = m.group("FQName");
            if (StringUtils.equals(stereotype, xrefStereotype)) {
                edge.addProperty("fqStereotype", STRING, xrefStereotype);
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

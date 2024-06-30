package aaron.sparx;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "TaggedValueMode")
@XmlEnum
public enum TaggedValueMode {
    AS_PROPERTY,
    AS_NODE,
    NONE
}

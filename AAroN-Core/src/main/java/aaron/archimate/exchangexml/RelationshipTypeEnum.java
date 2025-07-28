//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.02.08 um 08:56:54 AM CET 
//


package aaron.archimate.exchangexml;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für RelationshipTypeEnum.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <pre>
 * &lt;simpleType name="RelationshipTypeEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="Composition"/&gt;
 *     &lt;enumeration value="Aggregation"/&gt;
 *     &lt;enumeration value="Assignment"/&gt;
 *     &lt;enumeration value="Realization"/&gt;
 *     &lt;enumeration value="Serving"/&gt;
 *     &lt;enumeration value="Access"/&gt;
 *     &lt;enumeration value="Influence"/&gt;
 *     &lt;enumeration value="Triggering"/&gt;
 *     &lt;enumeration value="Flow"/&gt;
 *     &lt;enumeration value="Specialization"/&gt;
 *     &lt;enumeration value="Association"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "RelationshipTypeEnum")
@XmlEnum
public enum RelationshipTypeEnum {

    @XmlEnumValue("Composition")
    COMPOSITION("Composition"),
    @XmlEnumValue("Aggregation")
    AGGREGATION("Aggregation"),
    @XmlEnumValue("Assignment")
    ASSIGNMENT("Assignment"),
    @XmlEnumValue("Realization")
    REALIZATION("Realization"),
    @XmlEnumValue("Serving")
    SERVING("Serving"),
    @XmlEnumValue("Access")
    ACCESS("Access"),
    @XmlEnumValue("Influence")
    INFLUENCE("Influence"),
    @XmlEnumValue("Triggering")
    TRIGGERING("Triggering"),
    @XmlEnumValue("Flow")
    FLOW("Flow"),
    @XmlEnumValue("Specialization")
    SPECIALIZATION("Specialization"),
    @XmlEnumValue("Association")
    ASSOCIATION("Association");
    private final String value;

    RelationshipTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RelationshipTypeEnum fromValue(String v) {
        for (RelationshipTypeEnum c : RelationshipTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

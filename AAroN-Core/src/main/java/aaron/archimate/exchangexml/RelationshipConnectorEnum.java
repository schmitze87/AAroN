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
 * <p>Java-Klasse für RelationshipConnectorEnum.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <pre>
 * &lt;simpleType name="RelationshipConnectorEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="AndJunction"/&gt;
 *     &lt;enumeration value="OrJunction"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "RelationshipConnectorEnum")
@XmlEnum
public enum RelationshipConnectorEnum {

    @XmlEnumValue("AndJunction")
    AND_JUNCTION("AndJunction"),
    @XmlEnumValue("OrJunction")
    OR_JUNCTION("OrJunction");
    private final String value;

    RelationshipConnectorEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RelationshipConnectorEnum fromValue(String v) {
        for (RelationshipConnectorEnum c : RelationshipConnectorEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

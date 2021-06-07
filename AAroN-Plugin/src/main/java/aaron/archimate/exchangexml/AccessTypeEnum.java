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
 * <p>Java-Klasse für AccessTypeEnum.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <pre>
 * &lt;simpleType name="AccessTypeEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="Access"/&gt;
 *     &lt;enumeration value="Read"/&gt;
 *     &lt;enumeration value="Write"/&gt;
 *     &lt;enumeration value="ReadWrite"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AccessTypeEnum")
@XmlEnum
public enum AccessTypeEnum {

    @XmlEnumValue("Access")
    ACCESS("Access"),
    @XmlEnumValue("Read")
    READ("Read"),
    @XmlEnumValue("Write")
    WRITE("Write"),
    @XmlEnumValue("ReadWrite")
    READ_WRITE("ReadWrite");
    private final String value;

    AccessTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AccessTypeEnum fromValue(String v) {
        for (AccessTypeEnum c: AccessTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

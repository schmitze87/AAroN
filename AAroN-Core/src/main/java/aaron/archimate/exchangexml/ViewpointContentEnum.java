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
 * <p>Java-Klasse für ViewpointContentEnum.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <pre>
 * &lt;simpleType name="ViewpointContentEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="Details"/&gt;
 *     &lt;enumeration value="Coherence"/&gt;
 *     &lt;enumeration value="Overview"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "ViewpointContentEnum")
@XmlEnum
public enum ViewpointContentEnum {

    @XmlEnumValue("Details")
    DETAILS("Details"),
    @XmlEnumValue("Coherence")
    COHERENCE("Coherence"),
    @XmlEnumValue("Overview")
    OVERVIEW("Overview");
    private final String value;

    ViewpointContentEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ViewpointContentEnum fromValue(String v) {
        for (ViewpointContentEnum c : ViewpointContentEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

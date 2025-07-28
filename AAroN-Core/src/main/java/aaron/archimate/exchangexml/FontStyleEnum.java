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
 * <p>Java-Klasse für FontStyleEnum.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <pre>
 * &lt;simpleType name="FontStyleEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="plain"/&gt;
 *     &lt;enumeration value="bold"/&gt;
 *     &lt;enumeration value="italic"/&gt;
 *     &lt;enumeration value="underline"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "FontStyleEnum")
@XmlEnum
public enum FontStyleEnum {

    @XmlEnumValue("plain")
    PLAIN("plain"),
    @XmlEnumValue("bold")
    BOLD("bold"),
    @XmlEnumValue("italic")
    ITALIC("italic"),
    @XmlEnumValue("underline")
    UNDERLINE("underline");
    private final String value;

    FontStyleEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FontStyleEnum fromValue(String v) {
        for (FontStyleEnum c : FontStyleEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

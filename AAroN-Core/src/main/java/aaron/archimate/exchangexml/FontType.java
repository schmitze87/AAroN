//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.02.08 um 08:56:54 AM CET 
//


package aaron.archimate.exchangexml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Font type.
 *
 *
 * <p>Java-Klasse für FontType complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="FontType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="color" type="{http://www.opengroup.org/xsd/archimate/3.0/}RGBColorType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="size" type="{http://www.opengroup.org/xsd/archimate/3.0/}nonNegativeHalfGranularityDecimal" /&gt;
 *       &lt;attribute name="style" type="{http://www.opengroup.org/xsd/archimate/3.0/}FontStyleType" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FontType", propOrder = {
        "color"
})
public class FontType {

    protected RGBColorType color;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "size")
    protected BigDecimal size;
    @XmlAttribute(name = "style")
    protected List<FontStyleEnum> style;

    /**
     * Ruft den Wert der color-Eigenschaft ab.
     *
     * @return possible object is
     * {@link RGBColorType }
     */
    public RGBColorType getColor() {
        return color;
    }

    /**
     * Legt den Wert der color-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link RGBColorType }
     */
    public void setColor(RGBColorType value) {
        this.color = value;
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der size-Eigenschaft ab.
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getSize() {
        return size;
    }

    /**
     * Legt den Wert der size-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setSize(BigDecimal value) {
        this.size = value;
    }

    /**
     * Gets the value of the style property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the style property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStyle().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FontStyleEnum }
     */
    public List<FontStyleEnum> getStyle() {
        if (style == null) {
            style = new ArrayList<>();
        }
        return this.style;
    }

}

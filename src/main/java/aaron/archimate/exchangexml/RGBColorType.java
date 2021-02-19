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


/**
 * 
 *                 RGB Color type.
 *                 The r, g, b attributes range from 0 - 255.
 *                 The a (alpha) transparency attribute is optional. 0 = full transparency, 100 = opaque.
 *             
 * 
 * <p>Java-Klasse für RGBColorType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="RGBColorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="r" use="required" type="{http://www.opengroup.org/xsd/archimate/3.0/}RGBValueType" /&gt;
 *       &lt;attribute name="g" use="required" type="{http://www.opengroup.org/xsd/archimate/3.0/}RGBValueType" /&gt;
 *       &lt;attribute name="b" use="required" type="{http://www.opengroup.org/xsd/archimate/3.0/}RGBValueType" /&gt;
 *       &lt;attribute name="a"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte"&gt;
 *             &lt;minInclusive value="0"/&gt;
 *             &lt;maxInclusive value="100"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RGBColorType")
public class RGBColorType {

    @XmlAttribute(name = "r", required = true)
    protected short r;
    @XmlAttribute(name = "g", required = true)
    protected short g;
    @XmlAttribute(name = "b", required = true)
    protected short b;
    @XmlAttribute(name = "a")
    protected Short a;

    /**
     * Ruft den Wert der r-Eigenschaft ab.
     * 
     */
    public short getR() {
        return r;
    }

    /**
     * Legt den Wert der r-Eigenschaft fest.
     * 
     */
    public void setR(short value) {
        this.r = value;
    }

    /**
     * Ruft den Wert der g-Eigenschaft ab.
     * 
     */
    public short getG() {
        return g;
    }

    /**
     * Legt den Wert der g-Eigenschaft fest.
     * 
     */
    public void setG(short value) {
        this.g = value;
    }

    /**
     * Ruft den Wert der b-Eigenschaft ab.
     * 
     */
    public short getB() {
        return b;
    }

    /**
     * Legt den Wert der b-Eigenschaft fest.
     * 
     */
    public void setB(short value) {
        this.b = value;
    }

    /**
     * Ruft den Wert der a-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getA() {
        return a;
    }

    /**
     * Legt den Wert der a-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setA(Short value) {
        this.a = value;
    }

}

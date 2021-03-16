//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.02.08 um 08:56:54 AM CET 
//


package aaron.archimate.exchangexml;

import jakarta.xml.bind.annotation.*;

import java.math.BigInteger;


/**
 * 
 *                 Graphical node type. It can contain child node types.
 *             
 * 
 * <p>Java-Klasse für ViewNodeType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ViewNodeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengroup.org/xsd/archimate/3.0/}ViewConceptType"&gt;
 *       &lt;attGroup ref="{http://www.opengroup.org/xsd/archimate/3.0/}LocationGroup"/&gt;
 *       &lt;attGroup ref="{http://www.opengroup.org/xsd/archimate/3.0/}SizeGroup"/&gt;
 *       &lt;anyAttribute namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ViewNodeType")
@XmlSeeAlso({
    Label.class,
    Container.class
})
public abstract class ViewNodeType
    extends ViewConceptType
{

    @XmlAttribute(name = "x", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger x;
    @XmlAttribute(name = "y", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger y;
    @XmlAttribute(name = "w", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger w;
    @XmlAttribute(name = "h", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger h;

    /**
     * Ruft den Wert der x-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getX() {
        return x;
    }

    /**
     * Legt den Wert der x-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setX(BigInteger value) {
        this.x = value;
    }

    /**
     * Ruft den Wert der y-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getY() {
        return y;
    }

    /**
     * Legt den Wert der y-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setY(BigInteger value) {
        this.y = value;
    }

    /**
     * Ruft den Wert der w-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getW() {
        return w;
    }

    /**
     * Legt den Wert der w-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setW(BigInteger value) {
        this.w = value;
    }

    /**
     * Ruft den Wert der h-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getH() {
        return h;
    }

    /**
     * Legt den Wert der h-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setH(BigInteger value) {
        this.h = value;
    }

}

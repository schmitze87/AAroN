//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.02.08 um 08:56:54 AM CET 
//


package aaron.archimate.exchangexml;

import java.math.BigInteger;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 A Style type for a node and a connection.
 *                 It is a container for style properties such as color, font.
 *             
 * 
 * <p>Java-Klasse für StyleType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="StyleType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="lineColor" type="{http://www.opengroup.org/xsd/archimate/3.0/}RGBColorType" minOccurs="0"/&gt;
 *         &lt;element name="fillColor" type="{http://www.opengroup.org/xsd/archimate/3.0/}RGBColorType" minOccurs="0"/&gt;
 *         &lt;element name="font" type="{http://www.opengroup.org/xsd/archimate/3.0/}FontType" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *       &lt;attribute name="lineWidth" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StyleType", propOrder = {

})
public class StyleType {

    protected RGBColorType lineColor;
    protected RGBColorType fillColor;
    protected FontType font;
    @XmlAttribute(name = "lineWidth")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger lineWidth;

    /**
     * Ruft den Wert der lineColor-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RGBColorType }
     *     
     */
    public RGBColorType getLineColor() {
        return lineColor;
    }

    /**
     * Legt den Wert der lineColor-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RGBColorType }
     *     
     */
    public void setLineColor(RGBColorType value) {
        this.lineColor = value;
    }

    /**
     * Ruft den Wert der fillColor-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RGBColorType }
     *     
     */
    public RGBColorType getFillColor() {
        return fillColor;
    }

    /**
     * Legt den Wert der fillColor-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RGBColorType }
     *     
     */
    public void setFillColor(RGBColorType value) {
        this.fillColor = value;
    }

    /**
     * Ruft den Wert der font-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link FontType }
     *     
     */
    public FontType getFont() {
        return font;
    }

    /**
     * Legt den Wert der font-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link FontType }
     *     
     */
    public void setFont(FontType value) {
        this.font = value;
    }

    /**
     * Ruft den Wert der lineWidth-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLineWidth() {
        return lineWidth;
    }

    /**
     * Legt den Wert der lineWidth-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLineWidth(BigInteger value) {
        this.lineWidth = value;
    }

}

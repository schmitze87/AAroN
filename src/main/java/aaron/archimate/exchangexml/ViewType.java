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
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 Container for View type.
 * 
 *                 Note that View itself is abstract so one must have a derived type. the xml example must look like
 *                 a tag name "view" with an attribute of xsi:type="Diagram", if Diagram is a derived type from ViewType.
 *             
 * 
 * <p>Java-Klasse für ViewType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ViewType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengroup.org/xsd/archimate/3.0/}NamedReferenceableType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://www.opengroup.org/xsd/archimate/3.0/}PropertiesGroup"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="viewpoint" type="{http://www.opengroup.org/xsd/archimate/3.0/}ViewpointTypeType" /&gt;
 *       &lt;attribute name="viewpointRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" /&gt;
 *       &lt;anyAttribute namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ViewType", propOrder = {
    "properties"
})
@XmlSeeAlso({
    Diagram.class
})
public abstract class ViewType
    extends NamedReferenceableType
{

    protected PropertiesType properties;
    @XmlAttribute(name = "viewpoint")
    protected String viewpoint;
    @XmlAttribute(name = "viewpointRef")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object viewpointRef;

    /**
     * Ruft den Wert der properties-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PropertiesType }
     *     
     */
    public PropertiesType getProperties() {
        return properties;
    }

    /**
     * Legt den Wert der properties-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertiesType }
     *     
     */
    public void setProperties(PropertiesType value) {
        this.properties = value;
    }

    /**
     * Ruft den Wert der viewpoint-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getViewpoint() {
        return viewpoint;
    }

    /**
     * Legt den Wert der viewpoint-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setViewpoint(String value) {
        this.viewpoint = value;
    }

    /**
     * Ruft den Wert der viewpointRef-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getViewpointRef() {
        return viewpointRef;
    }

    /**
     * Legt den Wert der viewpointRef-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setViewpointRef(Object value) {
        this.viewpointRef = value;
    }

}

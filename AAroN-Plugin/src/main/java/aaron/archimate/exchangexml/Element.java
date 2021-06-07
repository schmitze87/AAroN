//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.02.08 um 08:56:54 AM CET 
//


package aaron.archimate.exchangexml;

import jakarta.xml.bind.annotation.*;


/**
 * 
 *                 Node type to allow an Element in a Artifact.
 * 
 *                 The node's label, documentation and properties may be determined
 *                 (i.e inherited) from those in the referenced ArchiMate element. Otherwise the node's label, documentation and properties
 *                 can be provided and will be additional to (or over-ride) those contained in the referenced ArchiMate element.
 *             
 * 
 * <p>Java-Klasse für Element complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Element"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengroup.org/xsd/archimate/3.0/}Container"&gt;
 *       &lt;attribute name="elementRef" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" /&gt;
 *       &lt;anyAttribute namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Element")
public class Element
    extends Container
{

    @XmlAttribute(name = "elementRef", required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object elementRef;

    /**
     * Ruft den Wert der elementRef-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getElementRef() {
        return elementRef;
    }

    /**
     * Legt den Wert der elementRef-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setElementRef(Object value) {
        this.elementRef = value;
    }

}

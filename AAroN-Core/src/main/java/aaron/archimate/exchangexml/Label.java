//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.02.08 um 08:56:54 AM CET 
//


package aaron.archimate.exchangexml;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Node type to allow a Label in a Artifact. the "label" element holds the info for the Note.
 *
 *
 * <p>Java-Klasse für Label complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="Label"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengroup.org/xsd/archimate/3.0/}ViewNodeType"&gt;
 *       &lt;attribute name="conceptRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" /&gt;
 *       &lt;attribute name="xpathPart" type="{http://www.opengroup.org/xsd/archimate/3.0/}XPATH_2.0_Expression" /&gt;
 *       &lt;anyAttribute namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Label")
public class Label
        extends ViewNodeType {

    @XmlAttribute(name = "conceptRef")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object conceptRef;
    @XmlAttribute(name = "xpathPart")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String xpathPart;

    /**
     * Ruft den Wert der conceptRef-Eigenschaft ab.
     *
     * @return possible object is
     * {@link Object }
     */
    public Object getConceptRef() {
        return conceptRef;
    }

    /**
     * Legt den Wert der conceptRef-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link Object }
     */
    public void setConceptRef(Object value) {
        this.conceptRef = value;
    }

    /**
     * Ruft den Wert der xpathPart-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getXpathPart() {
        return xpathPart;
    }

    /**
     * Legt den Wert der xpathPart-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setXpathPart(String value) {
        this.xpathPart = value;
    }

}

//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.02.08 um 08:56:54 AM CET 
//


package aaron.archimate.exchangexml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ViewsType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ViewsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengroup.org/xsd/archimate/3.0/}ViewsType"&gt;
 *       &lt;redefine&gt;
 *         &lt;complexType name="ViewsType"&gt;
 *           &lt;complexContent&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *               &lt;sequence&gt;
 *                 &lt;element name="viewpoints" type="{http://www.opengroup.org/xsd/archimate/3.0/}ViewpointsType" minOccurs="0"/&gt;
 *               &lt;/sequence&gt;
 *             &lt;/restriction&gt;
 *           &lt;/complexContent&gt;
 *         &lt;/complexType&gt;
 *       &lt;/redefine&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="diagrams" type="{http://www.opengroup.org/xsd/archimate/3.0/}DiagramsType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ViewsType", propOrder = {
    "diagrams"
})
public class ViewsType
    extends OriginalViewsType
{

    protected DiagramsType diagrams;

    /**
     * Ruft den Wert der diagrams-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DiagramsType }
     *     
     */
    public DiagramsType getDiagrams() {
        return diagrams;
    }

    /**
     * Legt den Wert der diagrams-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DiagramsType }
     *     
     */
    public void setDiagrams(DiagramsType value) {
        this.diagrams = value;
    }

}

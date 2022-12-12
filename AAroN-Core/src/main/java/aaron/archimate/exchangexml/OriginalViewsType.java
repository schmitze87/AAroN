//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.02.08 um 08:56:54 AM CET 
//


package aaron.archimate.exchangexml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This is a container for all of the Views in the aaron.model.
 *
 *
 * <p>Java-Klasse für ViewsType complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="ViewsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="viewpoints" type="{http://www.opengroup.org/xsd/archimate/3.0/}ViewpointsType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "viewpoints"
})
@XmlSeeAlso({
        ViewsType.class
})
public class OriginalViewsType {

    protected ViewpointsType viewpoints;

    /**
     * Ruft den Wert der viewpoints-Eigenschaft ab.
     *
     * @return possible object is
     * {@link ViewpointsType }
     */
    public ViewpointsType getViewpoints() {
        return viewpoints;
    }

    /**
     * Legt den Wert der viewpoints-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link ViewpointsType }
     */
    public void setViewpoints(ViewpointsType value) {
        this.viewpoints = value;
    }

}

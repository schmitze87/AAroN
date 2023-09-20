//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.02.08 um 08:56:54 AM CET 
//


package aaron.archimate.exchangexml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


/**
 * This is a container for all of the Viewpoints in the aaron.model.
 *
 *
 * <p>Java-Klasse für ViewpointsType complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="ViewpointsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="viewpoint" type="{http://www.opengroup.org/xsd/archimate/3.0/}ViewpointType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ViewpointsType", propOrder = {
        "viewpoint"
})
public class ViewpointsType {

    @XmlElement(required = true)
    protected List<ViewpointType> viewpoint;

    /**
     * Gets the value of the viewpoint property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the viewpoint property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getViewpoint().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ViewpointType }
     */
    public List<ViewpointType> getViewpoint() {
        if (viewpoint == null) {
            viewpoint = new ArrayList<>();
        }
        return this.viewpoint;
    }

}

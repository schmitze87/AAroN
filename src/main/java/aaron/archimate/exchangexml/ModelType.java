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
 * <p>Java-Klasse für ModelType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ModelType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengroup.org/xsd/archimate/3.0/}ModelType"&gt;
 *       &lt;redefine&gt;
 *         &lt;complexType name="ModelType"&gt;
 *           &lt;complexContent&gt;
 *             &lt;extension base="{http://www.opengroup.org/xsd/archimate/3.0/}NamedReferenceableType"&gt;
 *               &lt;sequence&gt;
 *                 &lt;group ref="{http://www.opengroup.org/xsd/archimate/3.0/}PropertiesGroup"/&gt;
 *                 &lt;element name="metadata" type="{http://www.opengroup.org/xsd/archimate/3.0/}MetadataType" minOccurs="0"/&gt;
 *                 &lt;element name="elements" type="{http://www.opengroup.org/xsd/archimate/3.0/}ElementsType" minOccurs="0"/&gt;
 *                 &lt;element name="relationships" type="{http://www.opengroup.org/xsd/archimate/3.0/}RelationshipsType" minOccurs="0"/&gt;
 *                 &lt;element name="organizations" type="{http://www.opengroup.org/xsd/archimate/3.0/}OrganizationsType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;element name="propertyDefinitions" type="{http://www.opengroup.org/xsd/archimate/3.0/}PropertyDefinitionsType" minOccurs="0"/&gt;
 *               &lt;/sequence&gt;
 *               &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;anyAttribute namespace='##other'/&gt;
 *             &lt;/extension&gt;
 *           &lt;/complexContent&gt;
 *         &lt;/complexType&gt;
 *       &lt;/redefine&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="views" type="{http://www.opengroup.org/xsd/archimate/3.0/}ViewsType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModelType", propOrder = {
    "views"
})
public class ModelType
    extends OriginalModelType
{

    protected ViewsType views;

    /**
     * Ruft den Wert der views-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ViewsType }
     *     
     */
    public ViewsType getViews() {
        return views;
    }

    /**
     * Legt den Wert der views-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ViewsType }
     *     
     */
    public void setViews(ViewsType value) {
        this.views = value;
    }

}

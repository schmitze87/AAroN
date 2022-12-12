//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.02.08 um 08:56:54 AM CET 
//


package aaron.archimate.exchangexml;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * This is the root aaron.model type.
 * It is a container for the elements, relationships, diagrams and organizations of the aaron.model.
 *
 *
 * <p>Java-Klasse für ModelType complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="ModelType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengroup.org/xsd/archimate/3.0/}NamedReferenceableType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://www.opengroup.org/xsd/archimate/3.0/}PropertiesGroup"/&gt;
 *         &lt;element name="metadata" type="{http://www.opengroup.org/xsd/archimate/3.0/}MetadataType" minOccurs="0"/&gt;
 *         &lt;element name="elements" type="{http://www.opengroup.org/xsd/archimate/3.0/}ElementsType" minOccurs="0"/&gt;
 *         &lt;element name="relationships" type="{http://www.opengroup.org/xsd/archimate/3.0/}RelationshipsType" minOccurs="0"/&gt;
 *         &lt;element name="organizations" type="{http://www.opengroup.org/xsd/archimate/3.0/}OrganizationsType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="propertyDefinitions" type="{http://www.opengroup.org/xsd/archimate/3.0/}PropertyDefinitionsType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "properties",
        "metadata",
        "elements",
        "relationships",
        "organizations",
        "propertyDefinitions"
})
@XmlSeeAlso({
        ModelType.class
})
public class OriginalModelType
        extends NamedReferenceableType {

    protected PropertiesType properties;
    protected MetadataType metadata;
    protected ElementsType elements;
    protected RelationshipsType relationships;
    protected List<OrganizationsType> organizations;
    protected PropertyDefinitionsType propertyDefinitions;
    @XmlAttribute(name = "version")
    protected String version;

    /**
     * Ruft den Wert der properties-Eigenschaft ab.
     *
     * @return possible object is
     * {@link PropertiesType }
     */
    public PropertiesType getProperties() {
        return properties;
    }

    /**
     * Legt den Wert der properties-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link PropertiesType }
     */
    public void setProperties(PropertiesType value) {
        this.properties = value;
    }

    /**
     * Ruft den Wert der metadata-Eigenschaft ab.
     *
     * @return possible object is
     * {@link MetadataType }
     */
    public MetadataType getMetadata() {
        return metadata;
    }

    /**
     * Legt den Wert der metadata-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link MetadataType }
     */
    public void setMetadata(MetadataType value) {
        this.metadata = value;
    }

    /**
     * Ruft den Wert der elements-Eigenschaft ab.
     *
     * @return possible object is
     * {@link ElementsType }
     */
    public ElementsType getElements() {
        return elements;
    }

    /**
     * Legt den Wert der elements-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link ElementsType }
     */
    public void setElements(ElementsType value) {
        this.elements = value;
    }

    /**
     * Ruft den Wert der relationships-Eigenschaft ab.
     *
     * @return possible object is
     * {@link RelationshipsType }
     */
    public RelationshipsType getRelationships() {
        return relationships;
    }

    /**
     * Legt den Wert der relationships-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link RelationshipsType }
     */
    public void setRelationships(RelationshipsType value) {
        this.relationships = value;
    }

    /**
     * Gets the value of the organizations property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the organizations property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrganizations().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrganizationsType }
     */
    public List<OrganizationsType> getOrganizations() {
        if (organizations == null) {
            organizations = new ArrayList<OrganizationsType>();
        }
        return this.organizations;
    }

    /**
     * Ruft den Wert der propertyDefinitions-Eigenschaft ab.
     *
     * @return possible object is
     * {@link PropertyDefinitionsType }
     */
    public PropertyDefinitionsType getPropertyDefinitions() {
        return propertyDefinitions;
    }

    /**
     * Legt den Wert der propertyDefinitions-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link PropertyDefinitionsType }
     */
    public void setPropertyDefinitions(PropertyDefinitionsType value) {
        this.propertyDefinitions = value;
    }

    /**
     * Ruft den Wert der version-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVersion() {
        return version;
    }

    /**
     * Legt den Wert der version-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVersion(String value) {
        this.version = value;
    }

}

//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.02.08 um 08:56:54 AM CET 
//


package aaron.archimate.exchangexml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


/**
 * An instance of the meta-data element contains data structures that declare descriptive information
 * about a meta-data element's parent only.
 * <p>
 * One or more different meta-data models may be declared as child extensions of a meta-data element.
 *
 *
 * <p>Java-Klasse für MetadataType complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="MetadataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;group ref="{http://www.opengroup.org/xsd/archimate/3.0/}SchemaInfoGroup"/&gt;
 *         &lt;element name="schemaInfo" type="{http://www.opengroup.org/xsd/archimate/3.0/}SchemaInfoType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetadataType", propOrder = {
        "schema",
        "schemaversion",
        "any",
        "schemaInfo"
})
public class MetadataType {

    protected String schema;
    protected String schemaversion;
    @XmlAnyElement(lax = true)
    protected List<Object> any;
    protected List<SchemaInfoType> schemaInfo;

    /**
     * Ruft den Wert der schema-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Legt den Wert der schema-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSchema(String value) {
        this.schema = value;
    }

    /**
     * Ruft den Wert der schemaversion-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSchemaversion() {
        return schemaversion;
    }

    /**
     * Legt den Wert der schemaversion-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSchemaversion(String value) {
        this.schemaversion = value;
    }

    /**
     * Gets the value of the any property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<>();
        }
        return this.any;
    }

    /**
     * Gets the value of the schemaInfo property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the schemaInfo property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSchemaInfo().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SchemaInfoType }
     */
    public List<SchemaInfoType> getSchemaInfo() {
        if (schemaInfo == null) {
            schemaInfo = new ArrayList<>();
        }
        return this.schemaInfo;
    }

}

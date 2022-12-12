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
 * A Property instance type declaring the reference to a Property definition and containing the Property value.
 *
 *
 * <p>Java-Klasse für PropertyType complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="PropertyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="value" type="{http://www.opengroup.org/xsd/archimate/3.0/}LangStringType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="propertyDefinitionRef" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PropertyType", propOrder = {
        "value"
})
public class PropertyType {

    @XmlElement(required = true)
    protected List<LangStringType> value;
    @XmlAttribute(name = "propertyDefinitionRef", required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object propertyDefinitionRef;

    /**
     * Gets the value of the value property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the value property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValue().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LangStringType }
     */
    public List<LangStringType> getValue() {
        if (value == null) {
            value = new ArrayList<LangStringType>();
        }
        return this.value;
    }

    /**
     * Ruft den Wert der propertyDefinitionRef-Eigenschaft ab.
     *
     * @return possible object is
     * {@link Object }
     */
    public Object getPropertyDefinitionRef() {
        return propertyDefinitionRef;
    }

    /**
     * Legt den Wert der propertyDefinitionRef-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link Object }
     */
    public void setPropertyDefinitionRef(Object value) {
        this.propertyDefinitionRef = value;
    }

}

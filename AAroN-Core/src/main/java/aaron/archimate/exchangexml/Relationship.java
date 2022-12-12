//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.02.08 um 08:56:54 AM CET 
//


package aaron.archimate.exchangexml;

import jakarta.xml.bind.annotation.*;


/**
 * Connector for a Relationship.
 *
 *
 * <p>Java-Klasse für Relationship complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="Relationship"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengroup.org/xsd/archimate/3.0/}SourcedConnectionType"&gt;
 *       &lt;attribute name="relationshipRef" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" /&gt;
 *       &lt;anyAttribute namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Relationship")
@XmlSeeAlso({
        NestingRelationship.class
})
public class Relationship
        extends SourcedConnectionType {

    @XmlAttribute(name = "relationshipRef", required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object relationshipRef;

    /**
     * Ruft den Wert der relationshipRef-Eigenschaft ab.
     *
     * @return possible object is
     * {@link Object }
     */
    public Object getRelationshipRef() {
        return relationshipRef;
    }

    /**
     * Legt den Wert der relationshipRef-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link Object }
     */
    public void setRelationshipRef(Object value) {
        this.relationshipRef = value;
    }

}

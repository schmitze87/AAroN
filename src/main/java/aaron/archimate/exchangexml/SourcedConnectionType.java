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
 * 
 *                 Abstract Type requiring a source and target.
 *             
 * 
 * <p>Java-Klasse für SourcedConnectionType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SourcedConnectionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.opengroup.org/xsd/archimate/3.0/}ConnectionType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://www.opengroup.org/xsd/archimate/3.0/}LabelGroup" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;group ref="{http://www.opengroup.org/xsd/archimate/3.0/}DocumentationGroup" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="style" type="{http://www.opengroup.org/xsd/archimate/3.0/}StyleType" minOccurs="0"/&gt;
 *         &lt;element name="viewRef" type="{http://www.opengroup.org/xsd/archimate/3.0/}ReferenceType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="sourceAttachment" type="{http://www.opengroup.org/xsd/archimate/3.0/}LocationType" minOccurs="0"/&gt;
 *         &lt;element name="bendpoint" type="{http://www.opengroup.org/xsd/archimate/3.0/}LocationType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="targetAttachment" type="{http://www.opengroup.org/xsd/archimate/3.0/}LocationType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.opengroup.org/xsd/archimate/3.0/}IdentifierGroup"/&gt;
 *       &lt;attribute name="source" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" /&gt;
 *       &lt;attribute name="target" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" /&gt;
 *       &lt;anyAttribute namespace='##other'/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SourcedConnectionType")
@XmlSeeAlso({
    Relationship.class
})
public abstract class SourcedConnectionType
    extends ConnectionType
{


}

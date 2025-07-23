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
 * Abstract Type requiring a source and target.
 *
 *
 * <p>Java-Klasse für SourcedConnectionType complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="SourcedConnectionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}ConnectionType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}LabelGroup" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;group ref="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}DocumentationGroup" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="style" type="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}StyleType" minOccurs="0"/&gt;
 *         &lt;element name="viewRef" type="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}ReferenceType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="sourceAttachment" type="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}LocationType" minOccurs="0"/&gt;
 *         &lt;element name="bendpoint" type="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}LocationType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="targetAttachment" type="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}LocationType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}IdentifierGroup"/&gt;
 *       &lt;attribute name="source" use="required" type="{<a href="http://www.w3.org/2001/XMLSchema">...</a>}IDREF" /&gt;
 *       &lt;attribute name="target" use="required" type="{<a href="http://www.w3.org/2001/XMLSchema">...</a>}IDREF" /&gt;
 *       &lt;anyAttribute namespace='##other'/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SourcedConnectionType")
@XmlSeeAlso({
        Relationship.class
})
public abstract class SourcedConnectionType
        extends ConnectionType {


}

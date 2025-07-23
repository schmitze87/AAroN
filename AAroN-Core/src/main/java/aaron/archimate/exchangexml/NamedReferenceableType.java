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
 * ObjectType which requires a Name and an ID.
 *
 *
 * <p>Java-Klasse für NamedReferenceableType complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="NamedReferenceableType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}ReferenceableType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}NameGroup" maxOccurs="unbounded"/&gt;
 *         &lt;group ref="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}DocumentationGroup"/&gt;
 *         &lt;group ref="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}grp.any"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{<a href="http://www.opengroup.org/xsd/archimate/3.0/">...</a>}IdentifierGroup"/&gt;
 *       &lt;anyAttribute namespace='##other'/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NamedReferenceableType")
@XmlSeeAlso({
        OriginalModelType.class,
        PropertyDefinitionType.class,
        ViewpointType.class,
        ViewType.class
})
public abstract class NamedReferenceableType
        extends ReferenceableType {


}

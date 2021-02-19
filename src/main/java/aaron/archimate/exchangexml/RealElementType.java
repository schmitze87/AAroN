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
 *                 Forces Real Elements to have Names.
 *             
 * 
 * <p>Java-Klasse für RealElementType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="RealElementType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.opengroup.org/xsd/archimate/3.0/}ElementType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://www.opengroup.org/xsd/archimate/3.0/}NameGroup" maxOccurs="unbounded"/&gt;
 *         &lt;group ref="{http://www.opengroup.org/xsd/archimate/3.0/}DocumentationGroup"/&gt;
 *         &lt;group ref="{http://www.opengroup.org/xsd/archimate/3.0/}grp.any"/&gt;
 *         &lt;group ref="{http://www.opengroup.org/xsd/archimate/3.0/}ConceptGroup"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.opengroup.org/xsd/archimate/3.0/}ConceptAttributeGroup"/&gt;
 *       &lt;attGroup ref="{http://www.opengroup.org/xsd/archimate/3.0/}IdentifierGroup"/&gt;
 *       &lt;anyAttribute namespace='##other'/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RealElementType")
@XmlSeeAlso({
    BusinessActor.class,
    BusinessRole.class,
    BusinessCollaboration.class,
    BusinessInterface.class,
    BusinessProcess.class,
    BusinessFunction.class,
    BusinessInteraction.class,
    BusinessEvent.class,
    BusinessService.class,
    BusinessObject.class,
    Contract.class,
    Representation.class,
    Product.class,
    ApplicationComponent.class,
    ApplicationCollaboration.class,
    ApplicationInterface.class,
    ApplicationFunction.class,
    ApplicationInteraction.class,
    ApplicationProcess.class,
    ApplicationEvent.class,
    ApplicationService.class,
    DataObject.class,
    Node.class,
    Device.class,
    SystemSoftware.class,
    TechnologyCollaboration.class,
    TechnologyInterface.class,
    Path.class,
    CommunicationNetwork.class,
    TechnologyFunction.class,
    TechnologyProcess.class,
    TechnologyInteraction.class,
    TechnologyEvent.class,
    TechnologyService.class,
    Artifact.class,
    Equipment.class,
    Facility.class,
    DistributionNetwork.class,
    Material.class,
    Stakeholder.class,
    Driver.class,
    Assessment.class,
    Goal.class,
    Outcome.class,
    Principle.class,
    Requirement.class,
    Constraint.class,
    Meaning.class,
    Value.class,
    Resource.class,
    Capability.class,
    CourseOfAction.class,
    ValueStream.class,
    WorkPackage.class,
    Deliverable.class,
    ImplementationEvent.class,
    Plateau.class,
    Gap.class
})
public abstract class RealElementType
    extends ElementType
{


}

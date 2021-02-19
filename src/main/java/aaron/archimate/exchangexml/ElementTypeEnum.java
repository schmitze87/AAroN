//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.02.08 um 08:56:54 AM CET 
//


package aaron.archimate.exchangexml;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ElementTypeEnum.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <pre>
 * &lt;simpleType name="ElementTypeEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="BusinessActor"/&gt;
 *     &lt;enumeration value="BusinessRole"/&gt;
 *     &lt;enumeration value="BusinessCollaboration"/&gt;
 *     &lt;enumeration value="BusinessInterface"/&gt;
 *     &lt;enumeration value="BusinessProcess"/&gt;
 *     &lt;enumeration value="BusinessFunction"/&gt;
 *     &lt;enumeration value="BusinessInteraction"/&gt;
 *     &lt;enumeration value="BusinessEvent"/&gt;
 *     &lt;enumeration value="BusinessService"/&gt;
 *     &lt;enumeration value="BusinessObject"/&gt;
 *     &lt;enumeration value="Contract"/&gt;
 *     &lt;enumeration value="Representation"/&gt;
 *     &lt;enumeration value="Product"/&gt;
 *     &lt;enumeration value="ApplicationComponent"/&gt;
 *     &lt;enumeration value="ApplicationCollaboration"/&gt;
 *     &lt;enumeration value="ApplicationInterface"/&gt;
 *     &lt;enumeration value="ApplicationFunction"/&gt;
 *     &lt;enumeration value="ApplicationInteraction"/&gt;
 *     &lt;enumeration value="ApplicationProcess"/&gt;
 *     &lt;enumeration value="ApplicationEvent"/&gt;
 *     &lt;enumeration value="ApplicationService"/&gt;
 *     &lt;enumeration value="DataObject"/&gt;
 *     &lt;enumeration value="Node"/&gt;
 *     &lt;enumeration value="Device"/&gt;
 *     &lt;enumeration value="SystemSoftware"/&gt;
 *     &lt;enumeration value="TechnologyCollaboration"/&gt;
 *     &lt;enumeration value="TechnologyInterface"/&gt;
 *     &lt;enumeration value="Path"/&gt;
 *     &lt;enumeration value="CommunicationNetwork"/&gt;
 *     &lt;enumeration value="TechnologyFunction"/&gt;
 *     &lt;enumeration value="TechnologyProcess"/&gt;
 *     &lt;enumeration value="TechnologyInteraction"/&gt;
 *     &lt;enumeration value="TechnologyEvent"/&gt;
 *     &lt;enumeration value="TechnologyService"/&gt;
 *     &lt;enumeration value="Artifact"/&gt;
 *     &lt;enumeration value="Equipment"/&gt;
 *     &lt;enumeration value="Facility"/&gt;
 *     &lt;enumeration value="DistributionNetwork"/&gt;
 *     &lt;enumeration value="Material"/&gt;
 *     &lt;enumeration value="Stakeholder"/&gt;
 *     &lt;enumeration value="Driver"/&gt;
 *     &lt;enumeration value="Assessment"/&gt;
 *     &lt;enumeration value="Goal"/&gt;
 *     &lt;enumeration value="Outcome"/&gt;
 *     &lt;enumeration value="Principle"/&gt;
 *     &lt;enumeration value="Requirement"/&gt;
 *     &lt;enumeration value="Constraint"/&gt;
 *     &lt;enumeration value="Meaning"/&gt;
 *     &lt;enumeration value="Value"/&gt;
 *     &lt;enumeration value="Resource"/&gt;
 *     &lt;enumeration value="Capability"/&gt;
 *     &lt;enumeration value="CourseOfAction"/&gt;
 *     &lt;enumeration value="ValueStream"/&gt;
 *     &lt;enumeration value="WorkPackage"/&gt;
 *     &lt;enumeration value="Deliverable"/&gt;
 *     &lt;enumeration value="ImplementationEvent"/&gt;
 *     &lt;enumeration value="Plateau"/&gt;
 *     &lt;enumeration value="Gap"/&gt;
 *     &lt;enumeration value="Grouping"/&gt;
 *     &lt;enumeration value="Location"/&gt;
 *     &lt;enumeration value="AndJunction"/&gt;
 *     &lt;enumeration value="OrJunction"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ElementTypeEnum")
@XmlEnum
public enum ElementTypeEnum {

    @XmlEnumValue("BusinessActor")
    BUSINESS_ACTOR("BusinessActor"),
    @XmlEnumValue("BusinessRole")
    BUSINESS_ROLE("BusinessRole"),
    @XmlEnumValue("BusinessCollaboration")
    BUSINESS_COLLABORATION("BusinessCollaboration"),
    @XmlEnumValue("BusinessInterface")
    BUSINESS_INTERFACE("BusinessInterface"),
    @XmlEnumValue("BusinessProcess")
    BUSINESS_PROCESS("BusinessProcess"),
    @XmlEnumValue("BusinessFunction")
    BUSINESS_FUNCTION("BusinessFunction"),
    @XmlEnumValue("BusinessInteraction")
    BUSINESS_INTERACTION("BusinessInteraction"),
    @XmlEnumValue("BusinessEvent")
    BUSINESS_EVENT("BusinessEvent"),
    @XmlEnumValue("BusinessService")
    BUSINESS_SERVICE("BusinessService"),
    @XmlEnumValue("BusinessObject")
    BUSINESS_OBJECT("BusinessObject"),
    @XmlEnumValue("Contract")
    CONTRACT("Contract"),
    @XmlEnumValue("Representation")
    REPRESENTATION("Representation"),
    @XmlEnumValue("Product")
    PRODUCT("Product"),
    @XmlEnumValue("ApplicationComponent")
    APPLICATION_COMPONENT("ApplicationComponent"),
    @XmlEnumValue("ApplicationCollaboration")
    APPLICATION_COLLABORATION("ApplicationCollaboration"),
    @XmlEnumValue("ApplicationInterface")
    APPLICATION_INTERFACE("ApplicationInterface"),
    @XmlEnumValue("ApplicationFunction")
    APPLICATION_FUNCTION("ApplicationFunction"),
    @XmlEnumValue("ApplicationInteraction")
    APPLICATION_INTERACTION("ApplicationInteraction"),
    @XmlEnumValue("ApplicationProcess")
    APPLICATION_PROCESS("ApplicationProcess"),
    @XmlEnumValue("ApplicationEvent")
    APPLICATION_EVENT("ApplicationEvent"),
    @XmlEnumValue("ApplicationService")
    APPLICATION_SERVICE("ApplicationService"),
    @XmlEnumValue("DataObject")
    DATA_OBJECT("DataObject"),
    @XmlEnumValue("Node")
    NODE("Node"),
    @XmlEnumValue("Device")
    DEVICE("Device"),
    @XmlEnumValue("SystemSoftware")
    SYSTEM_SOFTWARE("SystemSoftware"),
    @XmlEnumValue("TechnologyCollaboration")
    TECHNOLOGY_COLLABORATION("TechnologyCollaboration"),
    @XmlEnumValue("TechnologyInterface")
    TECHNOLOGY_INTERFACE("TechnologyInterface"),
    @XmlEnumValue("Path")
    PATH("Path"),
    @XmlEnumValue("CommunicationNetwork")
    COMMUNICATION_NETWORK("CommunicationNetwork"),
    @XmlEnumValue("TechnologyFunction")
    TECHNOLOGY_FUNCTION("TechnologyFunction"),
    @XmlEnumValue("TechnologyProcess")
    TECHNOLOGY_PROCESS("TechnologyProcess"),
    @XmlEnumValue("TechnologyInteraction")
    TECHNOLOGY_INTERACTION("TechnologyInteraction"),
    @XmlEnumValue("TechnologyEvent")
    TECHNOLOGY_EVENT("TechnologyEvent"),
    @XmlEnumValue("TechnologyService")
    TECHNOLOGY_SERVICE("TechnologyService"),
    @XmlEnumValue("Artifact")
    ARTIFACT("Artifact"),
    @XmlEnumValue("Equipment")
    EQUIPMENT("Equipment"),
    @XmlEnumValue("Facility")
    FACILITY("Facility"),
    @XmlEnumValue("DistributionNetwork")
    DISTRIBUTION_NETWORK("DistributionNetwork"),
    @XmlEnumValue("Material")
    MATERIAL("Material"),
    @XmlEnumValue("Stakeholder")
    STAKEHOLDER("Stakeholder"),
    @XmlEnumValue("Driver")
    DRIVER("Driver"),
    @XmlEnumValue("Assessment")
    ASSESSMENT("Assessment"),
    @XmlEnumValue("Goal")
    GOAL("Goal"),
    @XmlEnumValue("Outcome")
    OUTCOME("Outcome"),
    @XmlEnumValue("Principle")
    PRINCIPLE("Principle"),
    @XmlEnumValue("Requirement")
    REQUIREMENT("Requirement"),
    @XmlEnumValue("Constraint")
    CONSTRAINT("Constraint"),
    @XmlEnumValue("Meaning")
    MEANING("Meaning"),
    @XmlEnumValue("Value")
    VALUE("Value"),
    @XmlEnumValue("Resource")
    RESOURCE("Resource"),
    @XmlEnumValue("Capability")
    CAPABILITY("Capability"),
    @XmlEnumValue("CourseOfAction")
    COURSE_OF_ACTION("CourseOfAction"),
    @XmlEnumValue("ValueStream")
    VALUE_STREAM("ValueStream"),
    @XmlEnumValue("WorkPackage")
    WORK_PACKAGE("WorkPackage"),
    @XmlEnumValue("Deliverable")
    DELIVERABLE("Deliverable"),
    @XmlEnumValue("ImplementationEvent")
    IMPLEMENTATION_EVENT("ImplementationEvent"),
    @XmlEnumValue("Plateau")
    PLATEAU("Plateau"),
    @XmlEnumValue("Gap")
    GAP("Gap"),
    @XmlEnumValue("Grouping")
    GROUPING("Grouping"),
    @XmlEnumValue("Location")
    LOCATION("Location"),
    @XmlEnumValue("AndJunction")
    AND_JUNCTION("AndJunction"),
    @XmlEnumValue("OrJunction")
    OR_JUNCTION("OrJunction");
    private final String value;

    ElementTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ElementTypeEnum fromValue(String v) {
        for (ElementTypeEnum c: ElementTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

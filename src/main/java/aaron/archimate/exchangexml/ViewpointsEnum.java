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
 * <p>Java-Klasse für ViewpointsEnum.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <pre>
 * &lt;simpleType name="ViewpointsEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="Organization"/&gt;
 *     &lt;enumeration value="Application Platform"/&gt;
 *     &lt;enumeration value="Application Structure"/&gt;
 *     &lt;enumeration value="Information Structure"/&gt;
 *     &lt;enumeration value="Technology"/&gt;
 *     &lt;enumeration value="Layered"/&gt;
 *     &lt;enumeration value="Physical"/&gt;
 *     &lt;enumeration value="Product"/&gt;
 *     &lt;enumeration value="Application Usage"/&gt;
 *     &lt;enumeration value="Technology Usage"/&gt;
 *     &lt;enumeration value="Business Process Cooperation"/&gt;
 *     &lt;enumeration value="Application Cooperation"/&gt;
 *     &lt;enumeration value="Service Realization"/&gt;
 *     &lt;enumeration value="Implementation and Deployment"/&gt;
 *     &lt;enumeration value="Goal Realization"/&gt;
 *     &lt;enumeration value="Goal Contribution"/&gt;
 *     &lt;enumeration value="Principles"/&gt;
 *     &lt;enumeration value="Requirements Realization"/&gt;
 *     &lt;enumeration value="Motivation"/&gt;
 *     &lt;enumeration value="Strategy"/&gt;
 *     &lt;enumeration value="Capability Map"/&gt;
 *     &lt;enumeration value="Outcome Realization"/&gt;
 *     &lt;enumeration value="Resource Map"/&gt;
 *     &lt;enumeration value="Value Stream"/&gt;
 *     &lt;enumeration value="Project"/&gt;
 *     &lt;enumeration value="Migration"/&gt;
 *     &lt;enumeration value="Implementation and Migration"/&gt;
 *     &lt;enumeration value="Stakeholder"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ViewpointsEnum")
@XmlEnum
public enum ViewpointsEnum {

    @XmlEnumValue("Organization")
    ORGANIZATION("Organization"),
    @XmlEnumValue("Application Platform")
    APPLICATION_PLATFORM("Application Platform"),
    @XmlEnumValue("Application Structure")
    APPLICATION_STRUCTURE("Application Structure"),
    @XmlEnumValue("Information Structure")
    INFORMATION_STRUCTURE("Information Structure"),
    @XmlEnumValue("Technology")
    TECHNOLOGY("Technology"),
    @XmlEnumValue("Layered")
    LAYERED("Layered"),
    @XmlEnumValue("Physical")
    PHYSICAL("Physical"),
    @XmlEnumValue("Product")
    PRODUCT("Product"),
    @XmlEnumValue("Application Usage")
    APPLICATION_USAGE("Application Usage"),
    @XmlEnumValue("Technology Usage")
    TECHNOLOGY_USAGE("Technology Usage"),
    @XmlEnumValue("Business Process Cooperation")
    BUSINESS_PROCESS_COOPERATION("Business Process Cooperation"),
    @XmlEnumValue("Application Cooperation")
    APPLICATION_COOPERATION("Application Cooperation"),
    @XmlEnumValue("Service Realization")
    SERVICE_REALIZATION("Service Realization"),
    @XmlEnumValue("Implementation and Deployment")
    IMPLEMENTATION_AND_DEPLOYMENT("Implementation and Deployment"),
    @XmlEnumValue("Goal Realization")
    GOAL_REALIZATION("Goal Realization"),
    @XmlEnumValue("Goal Contribution")
    GOAL_CONTRIBUTION("Goal Contribution"),
    @XmlEnumValue("Principles")
    PRINCIPLES("Principles"),
    @XmlEnumValue("Requirements Realization")
    REQUIREMENTS_REALIZATION("Requirements Realization"),
    @XmlEnumValue("Motivation")
    MOTIVATION("Motivation"),
    @XmlEnumValue("Strategy")
    STRATEGY("Strategy"),
    @XmlEnumValue("Capability Map")
    CAPABILITY_MAP("Capability Map"),
    @XmlEnumValue("Outcome Realization")
    OUTCOME_REALIZATION("Outcome Realization"),
    @XmlEnumValue("Resource Map")
    RESOURCE_MAP("Resource Map"),
    @XmlEnumValue("Value Stream")
    VALUE_STREAM("Value Stream"),
    @XmlEnumValue("Project")
    PROJECT("Project"),
    @XmlEnumValue("Migration")
    MIGRATION("Migration"),
    @XmlEnumValue("Implementation and Migration")
    IMPLEMENTATION_AND_MIGRATION("Implementation and Migration"),
    @XmlEnumValue("Stakeholder")
    STAKEHOLDER("Stakeholder");
    private final String value;

    ViewpointsEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ViewpointsEnum fromValue(String v) {
        for (ViewpointsEnum c: ViewpointsEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

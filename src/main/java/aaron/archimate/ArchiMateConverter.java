package aaron.archimate;

import aaron.Converter;
import aaron.archimate.exchangexml.*;
import aaron.archimate.identifier.ElementId;
import aaron.archimate.identifier.RelationshipId;
import aaron.model.Edge;
import aaron.model.Identifier;
import aaron.model.Model;
import aaron.model.AAroNNode;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ArchiMateConverter implements Converter {

    private final Map<String, Object> elementsMap;
    private final Map<String, Object> relationshipMaps;
    private final Model graphModel;

    public ArchiMateConverter() {
        elementsMap = new HashMap<>();
        relationshipMaps = new HashMap<>();
        graphModel = new Model();
    }

    public Model convert(final File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ModelType.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement o = (JAXBElement) unmarshaller.unmarshal(file);
            ModelType model = (ModelType) o.getValue();
            processElements(model.getElements());
            processRelationships(model.getRelationships());
            processOrganisations(model.getOrganizations());
            return graphModel;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void processElements(final ElementsType elementsType) {
        if (elementsType != null) {
            List<ElementType> elementTypeList = elementsType.getElement();
            for (ElementType elementType : elementTypeList) {
                processElement(elementType);
            }
        }
    }

    private void processElement(final ElementType element) {
        String type = element.getClass().getSimpleName();
        String identifier = element.getIdentifier();
        String name = getName(element.getNameGroup());
        String documentation = getDocumentation(element.getDocumentation());

        AAroNNode.Builder builder = AAroNNode.builder();
        builder.addLabel(type);
        builder.addProperty("name", name);
        builder.addProperty("documentation", documentation);

        processProperties(element.getProperties(), builder::addProperty);
        AAroNNode node = builder.build();
        graphModel.addNode(new ElementId(identifier), node);
    }

    private void processRelationships(final RelationshipsType relationshipsType) {
        if (relationshipsType != null) {
            List<RelationshipType> relationshipTypes = relationshipsType.getRelationship();
            for (RelationshipType relationshipType : relationshipTypes) {
                processRelationship(relationshipType);
            }
        }
    }

    private void processRelationship(final RelationshipType relationshipType) {
        String identifier = relationshipType.getIdentifier();
        String documentation = getDocumentation(relationshipType.getDocumentation());
        String name = getName(relationshipType.getNameGroup());
        ElementType source = (ElementType) relationshipType.getSource();
        ElementType target = (ElementType) relationshipType.getTarget();
        String type = relationshipType.getClass().getSimpleName();
        PropertiesType properties = relationshipType.getProperties();

        Identifier start = new ElementId(source.getIdentifier());
        Identifier end = new ElementId(target.getIdentifier());

        Edge.Builder builder = Edge.builder();
        builder.setType(type);
        builder.setStart(start);
        builder.setEnd(end);
        builder.addProperty("documentation", documentation);
        builder.addProperty("name", name);
        processProperties(properties, builder::addProperty);
        Edge edge = builder.build();
        graphModel.addEdge(new RelationshipId(identifier), edge);
        return ;
    }

    private void processOrganisations(final List<OrganizationsType> organizations) {
        if (organizations != null && !organizations.isEmpty()) {
            for (OrganizationsType organizationsType : organizations) {
                List<OrganizationType> itemList = organizationsType.getItem();
                for (OrganizationType organizationType : itemList) {
                    processOrganization(organizationType);
                }
            }
        }
    }

    private Identifier processOrganization(final OrganizationType organizationType) {
        AAroNNode node;
        Identifier identifier;
        Object identifierRef = organizationType.getIdentifierRef();
        if (identifierRef != null) {
            identifier = new ElementId(organizationType.getIdentifier());
            node = graphModel.getNode(identifier);
        } else {
            String name = getName(organizationType.getLabelGroup());
            AAroNNode.Builder builder = AAroNNode.builder();
            builder.addLabel("Organization");
            builder.addProperty("name", name);
            node = builder.build();
            identifier = new ElementId(name);
            graphModel.addNode(identifier, node);
        }
        List<OrganizationType> itemList = organizationType.getItem();
        for (OrganizationType subOrganizationType : itemList) {
            Identifier subNodeIdentifier = processOrganization(subOrganizationType);
            Edge containsEdge = Edge.builder()
//                    .addProperty("identifier", UUID.randomUUID().toString())
                    .setStart(identifier)
                    .setEnd(subNodeIdentifier)
                    .setType("CONTAINS")
                    .build();
            graphModel.addEdge(new RelationshipId(UUID.randomUUID().toString()), containsEdge);
        }
        return identifier;
    }

    /**
     * HELPERS
     */

    @FunctionalInterface
    private interface ProcessProperty {
        void process(String name, Object value);
    }

    private void processProperties(final PropertiesType propertiesType, ProcessProperty processor) {
        if (propertiesType != null) {
            List<PropertyType> propertyTypeList = propertiesType.getProperty();
            for (PropertyType propertyType : propertyTypeList) {
                PropertyDefinitionType propertyDefinitionRef = (PropertyDefinitionType) propertyType.getPropertyDefinitionRef();
                String propName = getName(propertyDefinitionRef.getNameGroup());
                List<LangStringType> valueList = propertyType.getValue();
                if (valueList != null && !valueList.isEmpty()) {
                    String value = valueList.get(0).getValue();
                    processor.process(propName, value);
                }
            }
        }
    }

    private String getName(final List<LangStringType> nameGroup) {
        if (nameGroup != null && !nameGroup.isEmpty()) {
            return nameGroup.get(0).getValue();
        } else {
            return null;
        }
    }

    private String getDocumentation(final List<PreservedLangStringType> documentationList) {
        if (documentationList != null && !documentationList.isEmpty()) {
            return documentationList.get(0).getValue();
        } else {
            return null;
        }
    }
}

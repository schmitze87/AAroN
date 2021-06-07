package aaron.archimate;

import aaron.model.Converter;
import aaron.archimate.exchangexml.*;
import aaron.archimate.processors.DiagramProcessor;
import aaron.archimate.processors.ElementProcessor;
import aaron.archimate.processors.OrganisationProcessor;
import aaron.archimate.processors.RelationshipProcessor;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import aaron.model.Model;
import aaron.model.Processor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            processViews(model.getViews());
            return graphModel;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void processViews(final ViewsType views) {
        if (views != null) {
            DiagramsType diagrams = views.getDiagrams();
            if (diagrams != null) {
                Processor processor = new DiagramProcessor(graphModel);
                List<Diagram> diagramList = diagrams.getView();
                for (Diagram d: diagramList) {
                    processor.process(d);
                }
            }
        }
    }

    private void processElements(final ElementsType elementsType) {
        if (elementsType != null) {
            Processor elementProcessor = new ElementProcessor(graphModel);
            List<ElementType> elementTypeList = elementsType.getElement();
            for (ElementType elementType : elementTypeList) {
                elementProcessor.process(elementType);
            }
        }
    }

    private void processRelationships(final RelationshipsType relationshipsType) {
        if (relationshipsType != null) {
            Processor relationshipProcessor = new RelationshipProcessor(graphModel);
            List<RelationshipType> relationshipTypes = relationshipsType.getRelationship();
            for (RelationshipType relationshipType : relationshipTypes) {
                relationshipProcessor.process(relationshipType);
            }
        }
    }

    private void processOrganisations(final List<OrganizationsType> organizations) {
        if (organizations != null && !organizations.isEmpty()) {
            for (OrganizationsType organizationsType : organizations) {
                Processor processor = new OrganisationProcessor(graphModel);
                List<OrganizationType> itemList = organizationsType.getItem();
                for (OrganizationType organizationType : itemList) {
                    processor.process(organizationType);
                }
            }
        }
    }

}

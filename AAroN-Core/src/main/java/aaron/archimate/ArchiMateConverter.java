package aaron.archimate;

import aaron.archimate.exchangexml.*;
import aaron.archimate.processors.DiagramProcessor;
import aaron.archimate.processors.ElementProcessor;
import aaron.archimate.processors.OrganisationProcessor;
import aaron.archimate.processors.RelationshipProcessor;
import aaron.model.Converter;
import aaron.model.Model;
import aaron.model.Processor;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.List;

public class ArchiMateConverter implements Converter {

    private final Model graphModel;
    private final File file;

    public ArchiMateConverter(final File file) {
        this.file = file;
        graphModel = new Model();
    }

    public Model convert() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ModelType.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ModelType model = unmarshaller.unmarshal(new StreamSource(file), ModelType.class).getValue();
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
                Processor<Diagram> processor = new DiagramProcessor(graphModel);
                List<Diagram> diagramList = diagrams.getView();
                for (Diagram d : diagramList) {
                    processor.process(d);
                }
            }
        }
    }

    private void processElements(final ElementsType elementsType) {
        if (elementsType != null) {
            Processor<ElementType> elementProcessor = new ElementProcessor(graphModel);
            List<ElementType> elementTypeList = elementsType.getElement();
            for (ElementType elementType : elementTypeList) {
                elementProcessor.process(elementType);
            }
        }
    }

    private void processRelationships(final RelationshipsType relationshipsType) {
        if (relationshipsType != null) {
            Processor<RelationshipType> relationshipProcessor = new RelationshipProcessor(graphModel);
            List<RelationshipType> relationshipTypes = relationshipsType.getRelationship();
            for (RelationshipType relationshipType : relationshipTypes) {
                relationshipProcessor.process(relationshipType);
            }
        }
    }

    private void processOrganisations(final List<OrganizationsType> organizations) {
        if (organizations != null && !organizations.isEmpty()) {
            for (OrganizationsType organizationsType : organizations) {
                Processor<OrganizationType> processor = new OrganisationProcessor(graphModel);
                List<OrganizationType> itemList = organizationsType.getItem();
                for (OrganizationType organizationType : itemList) {
                    processor.process(organizationType);
                }
            }
        }
    }

}

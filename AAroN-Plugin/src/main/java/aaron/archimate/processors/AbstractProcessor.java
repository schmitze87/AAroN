package aaron.archimate.processors;

import aaron.archimate.exchangexml.*;
import aaron.model.Model;
import aaron.model.Processor;

import java.util.List;

import static aaron.model.PropertyType.STRING;

public abstract class AbstractProcessor<T> implements Processor<T> {

    protected final Model model;

    public AbstractProcessor(final Model model) {
        this.model = model;
    }

    @FunctionalInterface
    protected interface ProcessProperty {
        void process(String name, aaron.model.PropertyType type, Object value);
    }

    protected void processProperties(final PropertiesType propertiesType, ProcessProperty processor) {
        if (propertiesType != null) {
            List<PropertyType> propertyTypeList = propertiesType.getProperty();
            for (PropertyType propertyType : propertyTypeList) {
                PropertyDefinitionType propertyDefinitionRef = (PropertyDefinitionType) propertyType.getPropertyDefinitionRef();
                String propName = getName(propertyDefinitionRef.getNameGroup());
                List<LangStringType> valueList = propertyType.getValue();
                if (valueList != null && !valueList.isEmpty()) {
                    String value = valueList.get(0).getValue();
                    processor.process(propName, STRING, value);
                }
            }
        }
    }

    protected String getName(final List<LangStringType> nameGroup) {
        if (nameGroup != null && !nameGroup.isEmpty()) {
            return nameGroup.get(0).getValue();
        } else {
            return null;
        }
    }

    protected String getDocumentation(final List<PreservedLangStringType> documentationList) {
        if (documentationList != null && !documentationList.isEmpty()) {
            return documentationList.get(0).getValue();
        } else {
            return null;
        }
    }
}

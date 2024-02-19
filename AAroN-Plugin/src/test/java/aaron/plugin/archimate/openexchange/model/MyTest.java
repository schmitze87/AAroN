package aaron.plugin.archimate.openexchange.model;

import aaron.archimate.ArchiMateConverter;
import aaron.archimate.exchangexml.ModelType;
import aaron.model.Model;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.net.URL;

class MyTest {

    @Test
    void firstTest() {
        URL resource = ClassLoader.getSystemResource("sample1.xml");
        File file = new File(resource.getFile());
        Assertions.assertNotNull(file);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ModelType.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<ModelType> o = unmarshaller.unmarshal(new StreamSource(file), ModelType.class);
            Assertions.assertNotNull(o);
            ModelType model = o.getValue();
            System.out.println(model.getNameGroup().stream().findFirst().get().getValue());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Test
    void secondTest() {
        URL resource = ClassLoader.getSystemResource("archisurance.xml");
        File file = new File(resource.getFile());
        Assertions.assertNotNull(file);
        ArchiMateConverter converter = new ArchiMateConverter(file);
        Model graphModel = converter.convert();
        System.out.println(graphModel.toString());
    }

}
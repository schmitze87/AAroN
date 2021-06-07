package aaron.sparx.model;

public interface EATextObject extends EAObject {

    boolean isElementLink();

    Long linkedObject();
}

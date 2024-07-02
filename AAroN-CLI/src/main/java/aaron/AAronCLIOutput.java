package aaron;

import java.util.ArrayList;
import java.util.List;

public class AAronCLIOutput {

    private List<String> nodesToImport = new ArrayList<>();
    private List<String> edgesToImport = new ArrayList<>();

    public List<String> getNodesToImport() {
        return nodesToImport;
    }

    public void setNodesToImport(List<String> nodesToImport) {
        this.nodesToImport = nodesToImport;
    }

    public List<String> getEdgesToImport() {
        return edgesToImport;
    }

    public void setEdgesToImport(List<String> edgesToImport) {
        this.edgesToImport = edgesToImport;
    }
}

package aaron;

import java.util.ArrayList;
import java.util.List;

public class AAronCLIOutput {

    private List<String> nodesToImport = new ArrayList<>();
    private List<String> edgesToImport = new ArrayList<>();
    private int totalNodesCount;
    private int totalEdgesCount;
    private int totalPropertiesCount;



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

    public int getTotalNodesCount() {
        return totalNodesCount;
    }

    public void setTotalNodesCount(int totalNodesCount) {
        this.totalNodesCount = totalNodesCount;
    }

    public int getTotalEdgesCount() {
        return totalEdgesCount;
    }

    public void setTotalEdgesCount(int totalEdgesCount) {
        this.totalEdgesCount = totalEdgesCount;
    }

    public int getTotalPropertiesCount() {
        return totalPropertiesCount;
    }

    public void setTotalPropertiesCount(int totalPropertiesCount) {
        this.totalPropertiesCount = totalPropertiesCount;
    }
}

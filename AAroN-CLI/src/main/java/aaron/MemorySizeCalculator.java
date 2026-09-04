package aaron;

public class MemorySizeCalculator {

    /**
     * In den klassischen Store-Formaten benötigt Neo4j ungefähr: Node = 15 Byte, Relationship = 34 Byte, Property = 41 Byte. <br/>
     *
     * N = Anzahl der Knoten <br/>
     * R = Anzahl der Kanten (Relationships) <br/>
     * P = Anzahl der Properties <br/>
     * S = Durchschnittliche Property Größe <br/>
     * L = Durchschnittliche Anzahl Label pro Knoten <br/>
     *
     * @param nodesCount Anzahl der Knoten
     * @param edgesCount Anzahl der Kanten (Relationships)
     * @param propertiesCount Anzahl der Properties
     * @param averagePropertySize Durchschnittliche Property Größe in Bytes
     * @param averageLabelCount Durchschnittliche Anzahl Label pro Knoten
     * @return Empfohlene RAM Größe in MB
     */
    public static int calculateMemory(final int nodesCount, final int edgesCount, final int propertiesCount, final double averagePropertySize, final double averageLabelCount) {
        int result = 0;
        // Costs in Bytes
        int nodeCost = 15;
        int edgeCost = 34;
        int propertiesCost = 41;
        int labelCost = 4;
        double empiricalFactor = 1.5;

        double ramUsageInBytes = empiricalFactor * ((nodeCost*nodesCount) + (edgeCost*edgesCount) + (propertiesCount*(propertiesCost + averagePropertySize)) + (averageLabelCount*labelCost));
        double ramUsageInMegabytes = ramUsageInBytes / (1024*1024);
        result = roundUpToNearest1024(ramUsageInMegabytes);
        return result;
    }

    private static int roundUpToNearest1024(double number) {
        return (int) Math.ceil(number / 1024) * 1024;
    }

}

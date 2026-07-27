package aaron.plugin.sparx;

import org.neo4j.graphdb.*;
import org.neo4j.procedure.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConnectorFunctions {

    @Context
    public Transaction tx;

    @UserFunction(name = "aaron.sparx.conveyedItems")
    @Description("aaron.sparx.conveyedItems(relationship) - gives a list of all the nodes conveyed by this relationship.")
    public List<Node> conveyedItems(@Name("relationship") Relationship relationship) {
        if (relationship.hasProperty("conveyed")) {
            Object conveyed = relationship.getProperty("conveyed");
            if (conveyed instanceof String[]) {
                Stream.Builder<Node> builder = Stream.builder();
                String[] conveyedItems = (String[]) conveyed;
                for (String conveyedItem : conveyedItems) {
                    ResourceIterator<Node> nodes = tx.findNodes(Label.label("t_object"), "eaGuid", conveyedItem);
                    nodes.stream().forEach(builder::add);
                    nodes.close();
                }
                return builder.build().distinct().collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    @UserFunction(name = "aaron.sparx.informationFlowsRealized")
    @Description("aaron.sparx.informationFlowsRealized(relationship) - gives a list of all the relationships realized by the given relationship.")
    public List<Relationship> informationFlowsRealized(@Name("relationship") Relationship relationship) {
        if (relationship.hasProperty("informationFlowsRealized")) {
            Object o = relationship.getProperty("conveyed");
            if (o instanceof String[]) {
                String[] informationFlowsRealized = (String[]) o;

                return tx.getAllRelationships().stream().filter(r -> {
                    if (r.hasProperty("eaGuid")) {
                        String eaGuid = (String) r.getProperty("eaGuid");
                        return Arrays.asList(informationFlowsRealized).contains(eaGuid);
                    }
                    return false;
                }).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    @UserFunction(name = "aaron.sparx.relationshipConnectors")
    @Description("aaron.sparx.relationshipConnectors(relationship) - gives a list of all the relationships realized by the given relationship.")
    public List<Relationship> relationshipConnectors(@Name("relationship") Relationship relationship) {
        Object eaGuid = relationship.getProperty("eaGuid");
        if (eaGuid != null) {
            String connectorGuid = (String) eaGuid;
            List<Relationship> relationships = new ArrayList<>();
            tx.findNodes(Label.label("ProxyConnector"), "connectorGuid", connectorGuid).stream().forEach(n -> {
                n.getRelationships(Direction.INCOMING).stream().filter(r -> {
                    return ! (r.isType(RelationshipType.withName("SHOWS"))
                            || r.isType(RelationshipType.withName("CONTAINS"))
                            || r.isType(RelationshipType.withName("DIAGRAMLINK")));
                }).forEach(relationships::add);
                n.getRelationships(Direction.OUTGOING).stream().filter(r -> {
                    return ! (r.isType(RelationshipType.withName("SHOWS"))
                            || r.isType(RelationshipType.withName("CONTAINS"))
                            || r.isType(RelationshipType.withName("DIAGRAMLINK")));
                }).forEach(relationships::add);
            });
            return relationships;
        }
        return Collections.emptyList();
    }

    @UserFunction(name = "aaron.sparx.resolveProxyConnector")
    @Description("aaron.sparx.resolveProxyConnector(node) - resolves the ProxyConnector Node to it's corresponding relationship.")
    public Relationship resolveProxyConnector(@Name("proxyConnectorNode") Node node) {
        if (node.hasLabel(Label.label("ProxyConnector"))) {
            Object connectorGuidProperty = node.getProperty("connectorGuid");
            Object connectorTypeProperty = node.getProperty("connectorType");
            if (connectorGuidProperty != null && connectorTypeProperty != null) {
                String connectorGuid = (String) connectorGuidProperty;
                String connectorType = (String) connectorTypeProperty;
                return tx.findRelationship(RelationshipType.withName(connectorType), "eaGuid", connectorGuid);
            }
        }
        return null;
    }

}

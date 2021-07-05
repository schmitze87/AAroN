//package aaron.sparx;
//
//import org.neo4j.graphdb.*;
//import org.neo4j.procedure.*;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//public class ConnectorFunctions {
//
//    @Context
//    public Transaction tx;
//
//    @UserFunction(name = "aaron.sparx.conveyedItems")
//    @Description("aaron.sparx.conveyedItems(relationship) - gives a list of all the nodes conveyed by this relationship.")
//    public List<Node> conveyedItems(@Name("relationship") Relationship relationship) {
//        if (relationship.hasProperty("conveyed")) {
//            Object conveyed = relationship.getProperty("conveyed");
//            if (conveyed instanceof String[]) {
//                Stream.Builder<Node> builder = Stream.<Node>builder();
//                String[] conveyedItems = (String[]) conveyed;
//                for (String conveyedItem : conveyedItems) {
//                    for (Label label : tx.getAllLabelsInUse()) {
//                        ResourceIterator<Node> nodes = tx.findNodes(Label.label("t_object"), "eaGuid", conveyedItem);
//                        nodes.stream().forEach(builder::add);
//                        nodes.close();
//                    }
//                }
//                return builder.build().distinct().collect(Collectors.toList());
//            }
//        }
//        return Collections.EMPTY_LIST;
//    }
//
//    @UserFunction(name = "aaron.sparx.informationFlowsRealized")
//    @Description("aaron.sparx.informationFlowsRealized(relationship) - gives a list of all the relationships realized by the given relationship.")
//    public List<Relationship> informationFlowsRealized(@Name("relationship") Relationship relationship) {
//        if (relationship.hasProperty("informationFlowsRealized")) {
//            Object o = relationship.getProperty("conveyed");
//            if (o instanceof String[]) {
//                String[] informationFlowsRealized = (String[]) o;
//
//                return tx.getAllRelationships().stream().filter(r -> {
//                    if (r.hasProperty("eaGuid")) {
//                        String eaGuid = (String) r.getProperty("eaGuid");
//                        return Arrays.stream(informationFlowsRealized).anyMatch(i -> i.equals(eaGuid));
//                    }
//                    return false;
//                }).collect(Collectors.toList());
//            }
//        }
//        return Collections.EMPTY_LIST;
//    }
//}

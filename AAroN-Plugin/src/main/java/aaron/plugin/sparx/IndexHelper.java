//package aaron.sparx;
//
//import org.neo4j.graphdb.Transaction;
//import org.neo4j.graphdb.schema.IndexDefinition;
//import org.neo4j.graphdb.schema.IndexType;
//import org.neo4j.procedure.Context;
//import org.neo4j.procedure.Mode;
//import org.neo4j.procedure.Procedure;
//
//public class IndexHelper {
//
//    @Context
//    public Transaction tx;
//
//    @Procedure(name="aaron.sparx.createIndices", mode= Mode.SCHEMA)
//    public void createIndices() {
//        tx.getAllRelationshipTypes().forEach(rt -> {
//            IndexDefinition indexDefinition = tx.schema().indexFor(rt)
//                    .on("eaGuid")
//                    .withIndexType(IndexType.FULLTEXT)
//                    .create();
//        });
//        tx.getAllLabels().forEach(label -> {
//            IndexDefinition indexDefinition = tx.schema().indexFor(label).on("eaGuid").create();
//        });
//        //TODO: Check the state of the indices
//    }
//}

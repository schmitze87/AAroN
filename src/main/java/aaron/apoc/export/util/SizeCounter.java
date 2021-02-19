package aaron.apoc.export.util;

/**
 * This file was taken from APOC
 * https://github.com/neo4j-contrib/neo4j-apoc-procedures
 *
 * It is licensed under Apache-2.0 License
 *
 * @author mh
 * @since 22.05.16
 */
interface SizeCounter {
    long getNewLines();
    long getCount();
    long getTotal();
    long getPercent();
}

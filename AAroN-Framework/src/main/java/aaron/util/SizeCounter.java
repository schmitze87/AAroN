package aaron.util;

/**
 * This file was taken from APOC
 * <a href="https://github.com/neo4j-contrib/neo4j-apoc-procedures">...</a>
 * <p>
 * It is licensed under Apache-2.0 License
 *
 * @author mh
 * @since 22.05.16
 */
public interface SizeCounter {
    long getNewLines();
    long getCount();
    long getTotal();
    long getPercent();
}

package aaron.apoc.export.util;

import aaron.apoc.result.ProgressInfo;

/**
 * This file was taken from APOC
 * https://github.com/neo4j-contrib/neo4j-apoc-procedures
 *
 * It is licensed under Apache-2.0 License
 *
 * @author mh
 * @since 22.05.16
 */
public interface Reporter {
    void progress(String msg);
    void update(long nodes, long rels, long properties);

    void done();

    ProgressInfo getTotal();

    void nextRow();
}

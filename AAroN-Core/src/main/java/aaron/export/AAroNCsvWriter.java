package aaron.export;

import aaron.model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class AAroNCsvWriter {

    private final static Logger LOG = LoggerFactory.getLogger(AAroNCsvWriter.class);

    private int nodeIdCounter;
    private char delimiter = ',';
    private char quote = '"';

    private AAroNCsvWriter() {
        nodeIdCounter = 0;
    }

    public static void write(final Model model, final File nodesFile, final File edgesFile) throws IOException {
        AAroNCsvWriter writer = new AAroNCsvWriter();
        Set<CSVHeader> nodeHeaders = new HashSet<>();
        CSVHeader[] nodeCSVHeader;
        Set<CSVHeader> edgeHeaders = new HashSet<>();
        CSVHeader[] edgeCSVHeader;
        if (nodesFile != null) {
            if (nodesFile.exists() && !nodesFile.canWrite()) {
                LOG.error("can not write to nodes file. Check file permission");
            } else {
                model.getNodes().values().stream().forEach(n -> {
                            n.getProperties().entrySet().forEach(entry -> {
                                nodeHeaders.add(new CSVHeader(entry.getKey(), entry.getValue()));
                            });
                        });
                nodeCSVHeader = createNodeCSVHeader(nodeHeaders, model);
                CSVFormat csvFormat = CSVFormat.Builder.create()
                        .setDelimiter(writer.delimiter)
                        .setQuote(writer.quote)
                        .setRecordSeparator("\r\n")
                        .setIgnoreEmptyLines(true)
                        .setAllowDuplicateHeaderNames(false)
                        .setAutoFlush(true)
                        .setEscape('\\')
                        .setHeader(Arrays.stream(nodeCSVHeader).map(CSVHeader::toString).toArray(String[]::new))
                        .build();
                CSVPrinter nodePrinter = csvFormat.print(nodesFile, StandardCharsets.UTF_8);
                model.getNodes().values().stream().forEach(n -> {
                    Object[] record = writer.createNodeRecord(nodeCSVHeader, n);
                    try {
                        nodePrinter.printRecord(record);
                    } catch (IOException e) {
                        LOG.error("Error writing record to file {}", nodesFile.getAbsolutePath());
                        LOG.error("Record was: {}", record);
                        LOG.error("For node {}", n);
                        LOG.error("Exception", e);
                        e.printStackTrace();
                    }
                });
                nodePrinter.close(true);
            }
        }
        if (edgesFile != null) {
            if (edgesFile.exists() && !edgesFile.canWrite()) {
                LOG.error("can not write to edges file. Check file permission");
            } else {
                model.getEdges().values().stream().forEach(e -> {
                            e.getProperties().entrySet().forEach(entry -> {
                                edgeHeaders.add(new CSVHeader(entry.getKey(), entry.getValue()));
                            });
                        });
                edgeCSVHeader = createEdgeCSVHeader(nodeHeaders, model);
                CSVFormat csvFormat = CSVFormat.Builder.create()
                        .setDelimiter(writer.delimiter)
                        .setQuote(writer.quote)
                        .setRecordSeparator("\r\n")
                        .setIgnoreEmptyLines(true)
                        .setAllowDuplicateHeaderNames(false)
                        .setAutoFlush(true)
                        .setEscape('\\')
                        .setHeader(Arrays.stream(edgeCSVHeader).map(CSVHeader::toString).toArray(String[]::new))
                        .build();
                CSVPrinter edgePrinter = csvFormat.print(edgesFile, StandardCharsets.UTF_8);
                model.getEdges().values().stream().forEach(edge -> {
                    Object[] record = writer.createEdgeRecord(model, edgeCSVHeader, edge);
                    try {
                        if (record != null)
                            edgePrinter.printRecord(record);
                    } catch (IOException e) {
                        LOG.error("Error writing record to file {}", edgesFile.getAbsolutePath());
                        LOG.error("Record was: {}", record);
                        LOG.error("For edge {}", edge);
                        LOG.error("Exception", e);
                        e.printStackTrace();
                    }
                });
                edgePrinter.close(true);
            }
        }
    }

    private static CSVHeader[] createNodeCSVHeader(final Set<CSVHeader> headerSet, Model model) {
        String idSpace = null;
        ImportConext context = model.getContext();
        if (context != null) {
            idSpace = context.getFileHash();
        }
        List<CSVHeader> header = new ArrayList<>();
        if (idSpace != null) {
            header.add(new CSVHeader(null, "ID(" + idSpace + ")"));
        } else {
            header.add(new CSVHeader(null, "ID"));
        }
        header.add(new CSVHeader(null, "LABEL"));
        addPropertiesForHeader(header, headerSet);
        return header.toArray(new CSVHeader[header.size()]);
    }

    private static CSVHeader[] createEdgeCSVHeader(final Set<CSVHeader> headerSet, Model model) {
        String idSpace = null;
        ImportConext context = model.getContext();
        if (context != null) {
            idSpace = context.getFileHash();
        }
        List<CSVHeader> header = new ArrayList<>();
        if (idSpace != null) {
            header.add(new CSVHeader(null, "START_ID(" + idSpace + ")"));
        } else {
            header.add(new CSVHeader(null, "START_ID"));
        }
        header.add(new CSVHeader(null, "TYPE"));
        if (idSpace != null) {
            header.add(new CSVHeader(null, "END_ID(" + idSpace + ")"));
        } else {
            header.add(new CSVHeader(null, "END_ID"));
        }
        addPropertiesForHeader(header, headerSet);
        return header.toArray(new CSVHeader[header.size()]);
    }

    private Object[] createNodeRecord(final CSVHeader[] headers, AAroNNode node) {
        List<String> record = new ArrayList<>();
        node.setId(this.nodeIdCounter++);
        record.add(Integer.toString(node.getId()));
        record.add(node.getLabels().stream().collect(Collectors.joining(";")));
        addPropertiesToNodeRecord(record, headers, node);
        return record.toArray(new String[0]);
    }

    private Object[] createEdgeRecord(final Model model, final CSVHeader[] headers, final AAroNEdge edge) {
        List<String> record = new ArrayList<>();
        AAroNNode startNode = model.getNode(edge.getStart());
        AAroNNode endNode = model.getNode(edge.getEnd());

        if(startNode != null && startNode.getId() != null && endNode != null && endNode.getId() != null) {
            record.add(String.valueOf(startNode.getId()));
            record.add(edge.getType());
            record.add(String.valueOf(endNode.getId()));
            addPropertiesToEdgeRecord(record, headers, edge);
            return record.toArray(new String[0]);
        }
        return null;
    }

    private void addPropertiesToNodeRecord(final List<String> record, final CSVHeader[] headers, final WithProperties withProperties) {
        addPropertiesToRecord(record, headers, withProperties, 2);
    }

    private void addPropertiesToEdgeRecord(final List<String> record, final CSVHeader[] headers, final WithProperties withProperties) {
        addPropertiesToRecord(record, headers, withProperties, 3);
    }

    private void addPropertiesToRecord(final List<String> record, final CSVHeader[] headers, final WithProperties withProperties, int skip) {
        Arrays.stream(headers).skip(skip).forEach(h -> {
            Property property = withProperties.getProperties().get(h.name);
            if (property != null) {
                Object value = property.getValue();
                if (value == null) {
                    record.add(null);
                } else {
                    if (value.getClass().isArray()) {
                        Object[] objects = (Object[]) value;
                        String list = Arrays.stream(objects).map(Object::toString).collect(Collectors.joining(","));
                        record.add(list);
                    } else {
                        record.add(value.toString());
                    }
                }
            } else {
                record.add(null);
            }
        });
    }

    private static void addPropertiesForHeader(final List<CSVHeader> header, final Set<CSVHeader> headerSet) {
        if (headerSet != null) {
            headerSet.stream().forEach(csvHeader -> {
                header.add(csvHeader);
            });
        }
    }

    private static class CSVHeader {
        public String name;
        public String type;

        CSVHeader(String name, String type) {
            this.name = name;
            this.type = type;
        }

        CSVHeader(String name, Property property) {
            this.name = name;
            this.type = property.getType().getCsvValue();
        }

        @Override
        public String toString(){
            return (name != null ? name : "") + ":" + type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CSVHeader csvHeader = (CSVHeader) o;
            return Objects.equals(name, csvHeader.name) && Objects.equals(type, csvHeader.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, type);
        }
    }
}

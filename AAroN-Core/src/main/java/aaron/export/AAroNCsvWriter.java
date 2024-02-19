package aaron.export;

import aaron.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AAroNCsvWriter {

    private static final Logger LOG = LoggerFactory.getLogger(AAroNCsvWriter.class);

    private static final String COMMA = ",";
    private static final String DEFAULT_SEPARATOR = COMMA;
    private static final String DOUBLE_QUOTES = "\"";
    private static final String EMBEDDED_DOUBLE_QUOTES = "\"\"";
    private static final String NEW_LINE_UNIX = "\n";
    private static final String NEW_LINE_WINDOWS = "\r\n";
    private static final String EMPTY_STRING = "";

    private int nodeIdCounter;

    private AAroNCsvWriter() {
        nodeIdCounter = 0;
    }

    public String convertToCsvFormat(final String[] line) {
        return convertToCsvFormat(line, DEFAULT_SEPARATOR);
    }

    public String convertToCsvFormat(final String[] line, final String separator) {
        return convertToCsvFormat(line, separator, true);
    }

    // if quote = true, all fields are enclosed in double quotes
    public String convertToCsvFormat(
            final String[] line,
            final String separator,
            final boolean quote) {

        return Stream.of(line)                              // convert String[] to stream
                .map(l -> formatCsvField(l, quote))         // format CSV field
                .collect(Collectors.joining(separator));    // join with a separator

    }

    // put your extra login here
    private String formatCsvField(final String field, final boolean quote) {
        String result = field;
        if (result == null) {
            return EMPTY_STRING;
        }
        if (result.contains(COMMA)
                || result.contains(DOUBLE_QUOTES)
                || result.contains(NEW_LINE_UNIX)
                || result.contains(NEW_LINE_WINDOWS)) {
            // if field contains double quotes, replace it with two double quotes \"\"
            result = result.replace(DOUBLE_QUOTES, EMBEDDED_DOUBLE_QUOTES);

            // must wrap by or enclosed with double quotes
            result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;
        } else {
            // should all fields enclosed in double quotes
            if (quote) {
                result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;
            }
        }
        return result;
    }

    // a standard FileWriter, CSV is a normal text file
    private void writeToCsvFile(List<String[]> list, File file) throws IOException {
        List<String> collect = list.stream()
                .map(this::convertToCsvFormat)
                .collect(Collectors.toList());
        // CSV is a normal text file, need a writer
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            for (String line : collect) {
                bw.write(line);
                bw.newLine();
            }
        }
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
                List<String[]> nodesData = new ArrayList<>();
                model.getNodes().values().stream().distinct().forEach(n -> n.getProperties().entrySet().forEach(entry -> nodeHeaders.add(new CSVHeader(entry.getKey(), entry.getValue()))));
                nodeCSVHeader = createNodeCSVHeader(nodeHeaders, model);
                nodesData.add(Arrays.stream(nodeCSVHeader).map(CSVHeader::toString).toArray(String[]::new));
                model.getNodes().values().stream().distinct().forEach(n -> {
                    String[] nodeRrecord = writer.createNodeRecord(nodeCSVHeader, n);
                    nodesData.add(nodeRrecord);
                });
                writer.writeToCsvFile(nodesData, nodesFile);
            }
        }
        if (edgesFile != null) {
            if (edgesFile.exists() && !edgesFile.canWrite()) {
                LOG.error("can not write to edges file. Check file permission");
            } else {
                List<String[]> edgesData = new ArrayList<>();
                model.getEdges().values().stream().distinct().forEach(e -> e.getProperties().entrySet().forEach(entry -> edgeHeaders.add(new CSVHeader(entry.getKey(), entry.getValue()))));
                edgeCSVHeader = createEdgeCSVHeader(edgeHeaders, model);
                edgesData.add(Arrays.stream(edgeCSVHeader).map(CSVHeader::toString).toArray(String[]::new));
                model.getEdges().values().stream().distinct().forEach(edge -> {
                    String[] edgeRecord = writer.createEdgeRecord(model, edgeCSVHeader, edge);
                    if (edgeRecord != null)
                        edgesData.add(edgeRecord);
                });
                writer.writeToCsvFile(edgesData, edgesFile);
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

    private String[] createNodeRecord(final CSVHeader[] headers, AAroNNode node) {
        List<String> csvRecord = new ArrayList<>();
        node.setId(this.nodeIdCounter++);
        csvRecord.add(Integer.toString(node.getId()));
        csvRecord.add(String.join(";", node.getLabels()));
        addPropertiesToNodeRecord(csvRecord, headers, node);
        return csvRecord.toArray(new String[0]);
    }

    private String[] createEdgeRecord(final Model model, final CSVHeader[] headers, final AAroNEdge edge) {
        List<String> csvRecord = new ArrayList<>();
        AAroNNode startNode = model.getNode(edge.getStart());
        AAroNNode endNode = model.getNode(edge.getEnd());

        if(startNode != null && startNode.getId() != null && endNode != null && endNode.getId() != null) {
            csvRecord.add(String.valueOf(startNode.getId()));
            csvRecord.add(edge.getType());
            csvRecord.add(String.valueOf(endNode.getId()));
            addPropertiesToEdgeRecord(csvRecord, headers, edge);
            return csvRecord.toArray(new String[0]);
        }
        return null;
    }

    private void addPropertiesToNodeRecord(final List<String> csvRecord, final CSVHeader[] headers, final WithProperties withProperties) {
        addPropertiesToRecord(csvRecord, headers, withProperties, 2);
    }

    private void addPropertiesToEdgeRecord(final List<String> csvRecord, final CSVHeader[] headers, final WithProperties withProperties) {
        addPropertiesToRecord(csvRecord, headers, withProperties, 3);
    }

    private void addPropertiesToRecord(final List<String> csvRecord, final CSVHeader[] headers, final WithProperties withProperties, int skip) {
        Arrays.stream(headers).skip(skip).forEach(h -> {
            Property property = withProperties.getProperties().get(h.name);
            if (property != null) {
                Object value = property.getValue();
                if (value == null) {
                    csvRecord.add(null);
                } else {
                    if (value.getClass().isArray()) {
                        Object[] objects = (Object[]) value;
                        String list = Arrays.stream(objects).filter(Objects::nonNull).map(Object::toString).collect(Collectors.joining(","));
                        csvRecord.add(list);
                    } else if (value instanceof LocalDateTime) {
                        LocalDateTime dateTime = (LocalDateTime) value;
                        String format = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
                        csvRecord.add(format);
                    } else {
                        csvRecord.add(value.toString());
                    }
                }
            } else {
                csvRecord.add(null);
            }
        });
    }

    private static void addPropertiesForHeader(final List<CSVHeader> header, final Set<CSVHeader> headerSet) {
        if (headerSet != null) {
            headerSet.stream().forEach(header::add);
        }
    }

    private static class CSVHeader {

        private final String name;
        private final String type;

        CSVHeader(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        CSVHeader(String name, Property<?> property) {
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
